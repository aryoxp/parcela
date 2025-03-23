package ap.mobile.composablemap.abc

import ap.mobile.composablemap.Parcel
import java.util.TreeMap

class Colony(
  val parcels: List<Parcel>,
  numForager: Int = 5,
  numOnlooker: Int = 5,
  val forageLimit: Int = 50,
  val cycleLimit: Int = 30,
  val progress: (progress: Float) -> Unit,
  val startAtParcel: Parcel? = null,
) {
  private val bees = mutableListOf<Bee>()
  private val foods = mutableSetOf<Food>()

  companion object {
    val distances = TreeMap<Int, TreeMap<Int, Double>>()
  }

  data class Delivery(
    val parcels: List<Parcel>,
    val distance: Float = 0.0f,
    val duration: Float = 0.0f
  )

  init {
    repeat(numForager) {
      val bee = Bee(Bee.Type.SCOUT, forageLimit = forageLimit)
      bees.add(bee)
      // initialize foods in dancing altar
      foods.add(bee.scout(parcels, startAtParcel))
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
  }

  fun compute() : Delivery {
    var bestFood: Food? = null
    var bestCycle = 0
    for (cycle in 1..cycleLimit) {
      println("Cycle $cycle")
      // Employed Phase
      val processedAltar = mutableListOf<Food>()
      for (bee in bees) {
        if (bee.type == Bee.Type.EMPLOYED) {
          print("${bee.type.name}.")
          val food = foods.first().also { foods.remove(it) }
          processedAltar.add(bee.takeAndOptimizeFood(food))
          bee.becomeScout()
        }
      }
      // Dancing Phase
      processedAltar.sortBy { it.nectar }
      var altarBestFood = processedAltar.first()

      // Onlooker Phase
      for (bee in bees) {
        if (bee.type == Bee.Type.ONLOOKER) {
          print("${bee.type.name}.")
          var food = processedAltar.first().also { processedAltar.remove(it) }
          food = bee.lookupFood(food)
          if (food.nectar < altarBestFood.nectar)
            altarBestFood = food
        }
      }

      // Finding best food for all time
      if (bestFood == null) {
        bestFood = altarBestFood
        bestCycle = cycle
      } else if (altarBestFood.nectar < bestFood.nectar) {
        bestFood = altarBestFood
        bestCycle = cycle
      }

      // Scout Phase
      foods.clear()
      for (bee in bees) {
        if (bee.type == Bee.Type.SCOUT) {
          foods.add(bee.scout(parcels, startAtParcel))
          bee.becomeEmployed()
        }
      }
      println("Altar Best Food ${altarBestFood.nectar}")
      println("Best Food ${bestCycle}/${cycle}: ${bestFood.nectar}")

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