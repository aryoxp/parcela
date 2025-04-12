package ap.mobile.composablemap.aco

import ap.mobile.composablemap.Parcel
import kotlin.math.pow
import kotlin.math.sqrt

class Path (
  val parcelIds: List<Int>,
  parcels: List<Parcel> = listOf<Parcel>()
) {

  val route : MutableList<Parcel> = mutableListOf<Parcel>()
  var sugar = 0.0

  init {
    for (parcelId in parcelIds) {
      val parcel = parcels.first { it.id == parcelId }
      route.add(parcel)
    }
    computeSugar(route)
  }

  private fun computeSugar(parcels: List<Parcel>): Double {
    var distance = 0.0
    for (i in 1..(parcels.size-1)) {
      val d = distance(parcels[i], parcels[i - 1])
      distance += d
    }
    sugar = distance
    return sugar
  }

  fun leaveTrail(pheromones: MutableMap<Int, MutableMap<Int, Double>>) : Path {
    val trailPheromone = 1 / sugar
    for (i in 0..parcelIds.size - 2) {
      var level = pheromones[parcelIds[i]]?.get(parcelIds[i+1]) ?: 1.0
      pheromones[parcelIds[i]]?.set(parcelIds[i+1], level + trailPheromone)
    }
    return this
  }


  fun getParcels() : List<Parcel> {
    return route
  }

  // fun getParcelIds() : List<Int> {
  //   return route.map { it.id }
  // }

  fun getDuration(): Float { // in hrs
    // 10 kph
    // 10 minutes handover
    return ((sugar.times(110.574f) / 10f) + (route.size * 10f / 60f)).toFloat()
  }

  companion object {
    fun distance(parcel1: Parcel, parcel2: Parcel): Double {
      val distance = sqrt((parcel1.lat - parcel2.lat).pow(2) + (parcel1.lng - parcel2.lng).pow(2))
      return distance
    }
  }
}