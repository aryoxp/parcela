package ap.mobile.composablemap.abc

import ap.mobile.composablemap.Parcel
import kotlin.math.pow
import kotlin.math.sqrt

class Food(private val parcels: MutableList<Parcel>, val startAtParcel: Parcel?) {
  var nectar = 0.0

  init {
    computeNectar(parcels)
    if (startAtParcel != null) {
      parcels.remove(startAtParcel)
      parcels.add(index = 0, startAtParcel)
    }
  }

  fun optimizeShuffle(): Food {
    // print("Before: ")
    val before = computeNectar(parcels)
    val index = (1..(parcels.size-1)).shuffled().first()
    val chainA = parcels.slice(0..(index-1))
    val chainB = parcels.slice(index..(parcels.size-1)).shuffled()
    val newParcels: MutableList<Parcel> = mutableListOf()
    newParcels.addAll(chainA + chainB)
    // print("After: ")
    val after = computeNectar(newParcels)
    if (after < before) {
      parcels.clear()
      parcels.addAll(newParcels)
      print("Improved!.")
    }
    return this
  }

  fun optimize(): Food {
    // print("Before: ")
    val before = computeNectar(parcels)
    val index = (2..(parcels.size-1)).shuffled().first()
    val chainA = parcels.slice(0..(index-2))
    val chainB = parcels.slice(((index+1).takeIf { parcels.size > index+1 } ?: (parcels.size-1))..(parcels.size-1))
    val newParcels: MutableList<Parcel> = mutableListOf()
    newParcels.addAll(chainA + parcels[index] + parcels[index-1] + chainB)
    // print("After: ")
    val after = computeNectar(newParcels)
    if (after < before) {
      parcels.clear()
      parcels.addAll(newParcels)
      print("Improved!.")
    }
    return this
  }

  fun lookup(): Food {
    // print("Before: ")
    val before = computeNectar(parcels)
    val index = ((1.takeIf { startAtParcel != null } ?: 2)..(parcels.size-1)).shuffled().first()
    val chainA = parcels.slice(0..(index-1)).toMutableList()
    val chainB = parcels.slice(index..(parcels.size-1)).toMutableList()
    val newParcels: MutableList<Parcel> = mutableListOf()

    while(chainB.isNotEmpty()) {
      val lastParcel = chainA.last()
      val destMap = Colony.distances.get(lastParcel.id)
      var min = Double.MAX_VALUE
      var minId = 0
      var nextParcel: Parcel? = null
      for (next in chainB) {
        destMap?.getValue(next.id)?.let {
          if ( it < min) {
            min = it
            minId = next.id
            nextParcel = next
          }
        }
      }
      chainA.add(nextParcel!!)
      chainB.remove(nextParcel)
    }

    newParcels.addAll(chainA)
    // print("After: ")
    val after = computeNectar(newParcels)
    if (after < before) {
      parcels.clear()
      parcels.addAll(newParcels)
      print("Improved!.")
    }
    return this
  }

  fun computeNectar(parcels: List<Parcel>): Double {
    var distance = 0.0
    for (i in 1..(parcels.size-1)) {
      val d = distance(parcels[i], parcels[i-1])
      distance += d
    }
    nectar = distance
    // println("Nectar: " + nectar)
    return nectar
  }

  fun getParcels() : List<Parcel> {
    return this.parcels
  }

  fun getDuration(): Float { // in hrs
    // 10 kph
    // 10 mins handover
    return ((nectar.times(110.574f) / 10f) + (parcels.size * 10f / 60f)).toFloat()
  }

  companion object {
    fun distance(parcel1: Parcel, parcel2: Parcel): Double {
      val distance = sqrt((parcel1.lat - parcel2.lat).pow(2) + (parcel1.lng - parcel2.lng).pow(2))
      return distance
    }
  }
}