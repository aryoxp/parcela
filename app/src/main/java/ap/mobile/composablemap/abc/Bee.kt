package ap.mobile.composablemap.abc

import ap.mobile.composablemap.Parcel

class Bee(var type: Type, val forageLimit: Int = 5) {

  enum class Type {
    EMPLOYED,
    ONLOOKER,
    SCOUT,
  }

  fun takeAndOptimizeFood(food: Food) : Food {
    print("Take and Optimizing...")
    for(i in 1..forageLimit) {
      food.optimize()
      print("${i}.")
    }
    println("")
    return food
  }

  fun lookupFood(food: Food) : Food {
    print("Take and Optimizing...")
    for(i in 1..forageLimit) {
      food.lookup()
      print("${i}.")
    }
    println("")
    return food
  }

  fun becomeEmployed() {
    type = Type.EMPLOYED
  }

  fun becomeScout() {
    type = Type.SCOUT
  }

  fun scout(parcels: List<Parcel>): Food {
    return Food(parcels.shuffled().toMutableList())
  }

}