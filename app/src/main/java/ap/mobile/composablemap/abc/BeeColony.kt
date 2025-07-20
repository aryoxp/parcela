package ap.mobile.composablemap.abc

import ap.mobile.composablemap.optimizer.IOptimizer
import ap.mobile.composablemap.Parcel
import ap.mobile.composablemap.optimizer.Delivery
import kotlinx.coroutines.delay
import java.util.TreeMap

class BeeColony (
  val parcels: List<Parcel>,
  numForager: Int = 5,
  numOnlooker: Int = 5,
  val forageLimit: Int = 50,
  val cycleLimit: Int = 30,
  val progress: (progress: Float) -> Unit,
  val report: (cycle: Int, fitness: Double) -> Unit,
  val startAtParcel: Parcel? = null,
) : IOptimizer {
  private val bees = mutableListOf<Bee>()
  // private val foods = mutableSetOf<Food>()
  private val processedAltar = mutableListOf<Food>()

  override var bestCycle: Int = 0
    private set
  override var fitness: Double = 0.0
    private set

  companion object {
    val distances = TreeMap<Int, TreeMap<Int, Double>>()
  }

  init {
    repeat(numForager) {
      val bee = Bee(Bee.Type.SCOUT, forageLimit = forageLimit)
      bees.add(bee)
      // initialize foods in dancing altar
      processedAltar.add(bee.scout(parcels, startAtParcel))
      bee.becomeEmployed()
    }
    repeat(numOnlooker) {
      bees.add(Bee(Bee.Type.ONLOOKER, forageLimit = 5))
    }
    for (pa in parcels) {
      val destMap = TreeMap<Int, Double>()
      for (pb in parcels) {
        val d = Food.distance(pa, pb)
        destMap[pb.id] = d
      }
      distances[pa.id] = destMap
    }
    processedAltar.sortWith(compareBy(Food::nectar))
  }

  override suspend fun compute(): Delivery {
    var bestFood: Food? = null
    var bestCycle = 0
    for (cycle in 1..cycleLimit) {
      // println("\nCycle $cycle")
      // Employed Phase

      for (bee in bees) {
        if (bee.type == Bee.Type.EMPLOYED) {
          // print("${bee.type.name}.")
          val food = processedAltar.first().also { processedAltar.remove(it) }
          processedAltar.add(bee.takeAndOptimizeFood(food))
          bee.becomeScout()
        }
      }
      // Dancing Phase
      processedAltar.sortBy { it.nectar }
      var altarBestFood = processedAltar.first()
      // println("Altar Best Food nectar: ${altarBestFood.nectar}")

      // Onlooker Phase
      for (bee in bees) {
        if (bee.type == Bee.Type.ONLOOKER) {
          // print("${bee.type.name}.")
          var food = processedAltar.first().also { processedAltar.remove(it) }
          // print("Food nectar before lookup: ${food.nectar} ")
          food = bee.lookupFood(food)
          // println("After lookup: ${food.nectar} ")
          processedAltar.add(food)
          if (food.nectar < altarBestFood.nectar) {
            altarBestFood = food
            // println("Updating food from lookup: ${food.nectar} ")
          }
        }
      }

      processedAltar.sortWith(compareBy(Food::nectar))

      // Finding best food for all time
      if (bestFood == null) {
        bestFood = altarBestFood
        bestCycle = cycle
      } else if (altarBestFood.nectar < bestFood.nectar) {
        bestFood = altarBestFood
        bestCycle = cycle
        this.bestCycle = bestCycle
      }

      // Scout Phase
      // foods.clear()
      for (bee in bees) {
        if (bee.type == Bee.Type.SCOUT) {
          // foods.add(bee.scout(parcels, startAtParcel))
          val food = bee.scout(parcels, startAtParcel)
          if (food.nectar < processedAltar.last().nectar) {
            // println("\nScout find better food during scout.")
            processedAltar.removeAt(processedAltar.lastIndex)
            processedAltar.add(food)
          }
          bee.becomeEmployed()
        }
      }
      // println("Altar Best Food ${altarBestFood.nectar}")
      // println("Best Food ${bestCycle}/${cycle}: ${bestFood.nectar}")
      this.fitness = altarBestFood.nectar
      report(cycle, altarBestFood.nectar)
      progress(cycle.toFloat() / cycleLimit.toFloat())
      // delay(10)
    }

    val delivery = Delivery(
      parcels = bestFood?.getParcels() ?: emptyList(),
      distance = bestFood?.nectar?.times(110.574)?.toFloat() ?: 0.0f,
      duration = bestFood?.getDuration() ?: 0.0f
    )
    return delivery
  }

}