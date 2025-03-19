package ap.mobile.composablemap.abc

import ap.mobile.composablemap.Parcel
import kotlinx.coroutines.delay

class Colony(
  val parcels: List<Parcel>,
  val numForager: Int = 5,
  val numOnlooker: Int = 5,
  val forageLimit: Int = 5,
  val cycleLimit: Int = 30,
  val progress: (progress: Float) -> Unit,
) {
  private val bees = mutableListOf<Bee>()
  private val delivery = mutableListOf<Parcel>()
  private val foods = mutableSetOf<Food>()

  private var bestFood: Food? = null


  init {
    repeat(numForager) {
      val bee = Bee(Bee.Type.SCOUT, forageLimit = forageLimit)
      bees.add(bee)
      // initialize foods in altar
      foods.add(bee.scout(parcels))
      bee.becomeEmployed()
    }
    repeat(numOnlooker) {
      bees.add(Bee(Bee.Type.ONLOOKER))
    }
  }

  suspend fun compute() : List<Parcel> {
    var bestFood: Food? = null
    for (cycle in 1..cycleLimit) {
      // Employed Phase
      val processedAltar = mutableListOf<Food>()
      for (bee in bees) {
        if (bee.type == Bee.Type.EMPLOYED) {
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
          var food = processedAltar.first().also { processedAltar.remove(it) }
          food = bee.takeAndOptimizeFood(food)
          if (food.nectar < altarBestFood.nectar)
            altarBestFood = food
        }
      }

      // Finding best food for all time
      if (bestFood == null) bestFood = altarBestFood
      else if (altarBestFood.nectar < bestFood!!.nectar) bestFood = altarBestFood

      // Scout Phase
      foods.clear()
      for (bee in bees) {
        if (bee.type == Bee.Type.SCOUT) {
          foods.add(bee.scout(parcels))
          bee.becomeEmployed()
        }
      }

      progress(cycle.toFloat() / cycleLimit.toFloat())
      delay(10)
    }

    return bestFood?.getParcels() ?: emptyList()
  }

}