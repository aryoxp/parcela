package ap.mobile.composablemap.abc

import ap.mobile.composablemap.Parcel

class Bee(food: Food, type: Type) {

  enum class Type {
    Employed,
    Onlooker
  }

  private val food: Food = food
  var type: Type = type
    get() = field

  fun optimizeFood() {

  }

  fun dance(): Food {
    food.computeNectar()
    return food
  }

  fun turnOnlooker() {
    type = Type.Onlooker
  }

  fun turnEmployed() {
    type = Type.Employed
  }

}