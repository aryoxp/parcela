package ap.mobile.composablemap.abc

import ap.mobile.composablemap.Parcel
import kotlin.math.pow
import kotlin.math.sqrt

class Food(private val parcels: MutableList<Parcel>, val startAtParcel: Parcel?) {
  var nectar = 0.0

  init {
    computeNectar()
    if (startAtParcel != null) {
      val filtered: List<Parcel> = parcels.filterNot { it.id == startAtParcel.id }
      parcels.clear()
      parcels.add(startAtParcel)
      parcels.addAll(filtered)
    }
  }

  fun optimizeShuffle(): Food {
    // print("Before: ")
    val before = computeNectar()
    val index = (1..(parcels.size-1)).shuffled().first()
    val chainA = parcels.slice(0..(index-1))
    val chainB = parcels.slice(index..(parcels.size-1)).shuffled()
    val newParcels: MutableList<Parcel> = mutableListOf()
    newParcels.addAll(chainA + chainB)
    // print("After: ")
    val after = nectar(newParcels)
    if (after < before) {
      parcels.clear()
      parcels.addAll(newParcels)
      computeNectar()
      // print("Improved!.")
    }
    return this
  }

  fun optimize(): Food {
    // print("Optimizing. ")
    val before = computeNectar()
    // println(nectar)
    val index = (1..(parcels.size-2)).shuffled().first()
    val index2 = (2..(parcels.size-1)).shuffled().first()
    val newParcels: MutableList<Parcel> = mutableListOf()
    newParcels.addAll(parcels)
    val a = newParcels[index]
    val b = newParcels[index2]
    newParcels[index] = b
    newParcels[index2] = a
    // val chainA = parcels.slice(0..(index-2))
    // val chainB = parcels.slice(((index+1).takeIf { parcels.size > index+1 } ?: (parcels.size-1))..(parcels.size-1))
    // val chainA = parcels.slice(0..(index-1))
    // val chainB = parcels.slice((index+2)..(parcels.size-1))
    // val newParcels: MutableList<Parcel> = mutableListOf()
    // newParcels.addAll(chainA + parcels[index] + parcels[index-1] + chainB)
    // newParcels.addAll(chainA + parcels[index+1] + parcels[index] + chainB)
    // print("After: ")
    val after = nectar(newParcels)
    // println(nectar)
    if (after < before) {
      parcels.clear()
      parcels.addAll(newParcels)
      computeNectar()
      // print("Improved! $before to $after")
    }
    return this
  }

  fun lookup(): Food {
    // print("Looking up.")
    val before = computeNectar()
    // println(nectar)
    val index = (1..(parcels.size-2)).shuffled().first()
    val index2 = (2..(parcels.size-1)).shuffled().first()
    val newParcels: MutableList<Parcel> = mutableListOf()
    newParcels.addAll(parcels)
    val a = newParcels[index]
    val b = newParcels[index2]
    newParcels[index] = b
    newParcels[index2] = a
    // val chainA = parcels.slice(0..(index-2))
    // val chainB = parcels.slice(((index+1).takeIf { parcels.size > index+1 } ?: (parcels.size-1))..(parcels.size-1))
    // val chainA = parcels.slice(0..(index-1))
    // val chainB = parcels.slice((index+2)..(parcels.size-1))
    // val newParcels: MutableList<Parcel> = mutableListOf()
    // newParcels.addAll(chainA + parcels[index] + parcels[index-1] + chainB)
    // newParcels.addAll(chainA + parcels[index+1] + parcels[index] + chainB)
    // print("After: ")
    val after = nectar(newParcels)
    // println(nectar)
    if (after < before) {
      parcels.clear()
      parcels.addAll(newParcels)
      computeNectar()
      // print("Improved! $before to $after")
    }
    return this
    // // print("Before: ")
    // val before = computeNectar(parcels)
    // val index = ((1.takeIf { startAtParcel != null } ?: 2)..(parcels.size-1)).shuffled().first()
    // val chainA = parcels.slice(0..(index-1)).toMutableList()
    // val chainB = parcels.slice(index..(parcels.size-1)).toMutableList()
    // val newParcels: MutableList<Parcel> = mutableListOf()
    //
    // while(chainB.isNotEmpty()) {
    //   val lastParcel = chainA.last()
    //   val destMap = BeeColony.distances.get(lastParcel.id)
    //   var min = Double.MAX_VALUE
    //   var minId = 0
    //   var nextParcel: Parcel? = null
    //   for (next in chainB) {
    //     destMap?.getValue(next.id)?.let {
    //       if ( it < min) {
    //         min = it
    //         minId = next.id
    //         nextParcel = next
    //       }
    //     }
    //   }
    //   chainA.add(nextParcel!!)
    //   chainB.remove(nextParcel)
    // }
    //
    // newParcels.addAll(chainA)
    // // print("After: ")
    // val after = computeNectar(newParcels)
    // if (after < before) {
    //   parcels.clear()
    //   parcels.addAll(newParcels)
    //   // print("Improved!.")
    // }
    // return this
  }

  fun computeNectar(): Double {
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

    fun nectar(parcels: List<Parcel>): Double {
      var distance = 0.0
      for (i in 1..(parcels.size - 1)) {
        val d = distance(parcels[i], parcels[i - 1])
        distance += d
      }
      return distance
    }
  }
}