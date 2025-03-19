package ap.mobile.composablemap.abc

import ap.mobile.composablemap.Parcel

class Bee(type: Type, forageLimit: Int = 5) {

  enum class Type {
    EMPLOYED,
    ONLOOKER,
    SCOUT,
  }

  var type: Type = type
    get() = field

  val forageLimit: Int = forageLimit

  fun takeAndOptimizeFood(food: Food) : Food {
    for(i in 1..forageLimit)
      food.optimize()
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