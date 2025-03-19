package ap.mobile.composablemap.abc

import ap.mobile.composablemap.Parcel
import kotlin.math.pow
import kotlin.math.sqrt

class Food(food: List<Parcel>) {
  private val food: List<Parcel> = food
  var nectar = 0.0
    get() = field

  init {
    computeNectar()
  }

  fun computeNectar(): Double {
    var prevParcel: Parcel? = null
    for (parcel in food) {
      if (prevParcel == null) {
        prevParcel = parcel
        continue
      }
      nectar += distance(parcel, prevParcel)
    }
    return nectar
  }

  private fun distance(parcel1: Parcel, parcel2: Parcel): Double {
    return sqrt((parcel1.lat-parcel2.lat).pow(2)-(parcel1.lng-parcel2.lng).pow(2))
  }
}