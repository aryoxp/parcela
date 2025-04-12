package ap.mobile.composablemap.aco

import ap.mobile.composablemap.Parcel

class Ant(val parcels: List<Parcel>,
          val distances: Map<Int, Map<Int, Double>>) {

  fun moves (
    pheromones: MutableMap<Int, MutableMap<Int, Double>>,
    startAtParcel: Parcel?
  ) : Path {

    var parcelDelivered = mutableListOf<Int>()
    val parcelsToDeliver = parcels.map { it.id }.toMutableList()

    startAtParcel?.let {
      parcelDelivered.add(it.id)
      parcelsToDeliver.remove(it.id)
    } ?: {
      val id = parcelsToDeliver[(Math.random() * parcels.size).toInt()]
      parcelDelivered.add(id)
      parcelsToDeliver.remove(id)
    }()

    // Start delivering the parcels
    while(parcelsToDeliver.isNotEmpty()) {

      // See all next available parcels/points/nodes and pheromones
      var nextParcelPheromones = pheromones[parcelDelivered.last()]?.toMap()
      var nextParcelDistances = distances[parcelDelivered.last()]?.toMap()

      // Filter out already delivered parcels
      // from the global pheromones and distances matrices
      nextParcelPheromones = nextParcelPheromones?.filter {
        !parcelDelivered.contains(it.key)
      }
      nextParcelDistances = nextParcelDistances?.filter {
        !parcelDelivered.contains(it.key)
      }

      if (nextParcelPheromones != null) {
        val nextParcelId = getNextParcel(nextParcelPheromones, nextParcelDistances) ?: parcelsToDeliver.first()
        parcelDelivered.add(nextParcelId)
        parcelsToDeliver.remove(nextParcelId)
      }
    }

    // Leaves pheromones to the trail path
    return Path(parcelDelivered, parcels).leaveTrail(pheromones)
  }

  private fun getNextParcel(
    nextParcelPheromones: Map<Int, Double>,
    nextParcelDistances: Map<Int, Double>?
  ): Int? {

    // Compute probabilities of next parcel selection process
    // In this implementation, distance matter
    // So, multiply path's pheromone and with its inverse distance
    val nextParcelProbabilities = mutableMapOf<Int, Double>()

    // Calculate sum of pheromones
    var totalNextPheromones = 0.0
    nextParcelPheromones.forEach { parcelId, pheromone ->
      val distance = nextParcelDistances?.get(parcelId) ?: 0.0
      totalNextPheromones += (pheromone * (1/distance))
    }

    // Otherwise, just calculate the total sum of pheromones
    // val totalNextPheromones = nextParcelPheromones.values.sum()

    // Update pheromones
    nextParcelPheromones.forEach { parcelId, pheromone ->
      val distance = nextParcelDistances?.get(parcelId) ?: 1.0
      nextParcelProbabilities.put( parcelId, (pheromone * (1 / distance)) / totalNextPheromones)
    }

    // Build cumulative sum roulette wheel
    val nextParcelCumulativeSum = mutableListOf<Double>()
    for(i in 0..nextParcelProbabilities.values.size - 1) {
      var sum = 0.0
      for (j in i..nextParcelProbabilities.values.size - 1) {
        sum += nextParcelProbabilities.values.toList()[j]
      }
      nextParcelCumulativeSum.add(sum)
    }

    // Pick random number 0..1
    val next = Math.random()
    // Select roulette wheel according to the selected random number
    val nextParcelIds = nextParcelProbabilities.keys.toList()
    for(i in 0..nextParcelCumulativeSum.size - 2) {
      if (next > nextParcelCumulativeSum[i + 1] && next < nextParcelCumulativeSum[i]) {
        return nextParcelIds[i]
      }
    }
    // Not in the cumulative sum range?
    // Returns the last item
    return nextParcelIds.last()
  }
}

