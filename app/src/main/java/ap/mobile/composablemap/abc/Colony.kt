package ap.mobile.composablemap.abc

import androidx.compose.runtime.MutableState
import ap.mobile.composablemap.Parcel
import kotlinx.coroutines.delay

class Colony(
  val parcels: List<Parcel>,
  val numBees: Int = 5,
  val numCycles: Int = 30,
  val progress: (progress: Float) -> Unit,
) {
  private val bees = mutableListOf<Bee>()
  private val delivery = mutableListOf<Parcel>()
  private val foods = mutableListOf<Food>()

  private var bestFood: Food? = null


  init {
    repeat(numBees) {
      val food = Food(parcels.shuffled())
      bees.add(Bee(food, Bee.Type.Employed))
    }
  }

  fun dance() {
    var nectar = Double.MAX_VALUE
    for(bee in bees) {
      val food = bee.dance()
      val foodNectar = food.nectar
      if (foodNectar < nectar) {
        nectar = foodNectar
        bestFood = food
      }
    }
  }

  suspend fun compute() : Food? {
    for (cycle in 1..numCycles) {
      // Employed Phase
      for (bee in bees) {
        if (bee.type == Bee.Type.Employed)
          bee.optimizeFood()
      }
      // Onlooker Phase
      dance()
      progress(cycle.toFloat() / numCycles.toFloat())
      delay(50)
    }
    return this.bestFood
  }

}