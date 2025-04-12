package ap.mobile.composablemap.aco

import ap.mobile.composablemap.Parcel
import ap.mobile.composablemap.optimizer.Delivery
import ap.mobile.composablemap.optimizer.IOptimizer

class AntColony(
  val parcels: List<Parcel>,
  numAnts: Int = 10,
  val cycleLimit: Int = 30,
  val rho: Float = .5f,
  val progress: (progress: Float) -> Unit,
  val startAtParcel: Parcel? = null,
) : IOptimizer {

  companion object {
    private val ants = mutableListOf<Ant>()
    private val distances = mutableMapOf<Int, MutableMap<Int, Double>>()
    private val pheromones = mutableMapOf<Int, MutableMap<Int, Double>>()
  }

  init {

    // Initializes ants
    repeat(numAnts) {
      ants.add(Ant(parcels = parcels, distances = distances))
    }

    // Initializes distances and pheromones matrices
    for (pa in parcels) {
      val destMap = mutableMapOf<Int, Double>()
      val pheromoneMap = mutableMapOf<Int, Double>()
      for (pb in parcels) {
        val d = Path.distance(pa, pb)
        destMap[pb.id] = d
        // Initialize pheromones with its inverse distance
        // instead of 1.0
        // Nearest destination is more preferred
        pheromoneMap[pb.id] = 1/d // 1.0
      }
      distances[pa.id] = destMap
      pheromones[pa.id] = pheromoneMap
    }
  }

  override suspend fun compute(): Delivery {
    var bestPath: Path? = null
    var bestCycle = 0
    for (cycle in 1..cycleLimit) {
      println("Cycle $cycle")

      // Evaporate pheromones
      if (cycle > 1) {
        pheromones.values.map {
          it.entries.map {
            it.setValue(it.value * (1 - rho))
          }
        }
      }

      // Ants start scouting
      for (ant in ants) {
        val path = ant.moves(pheromones, startAtParcel)

        // check the path's sugar quality
        if (bestPath == null || path.sugar < bestPath.sugar) {
          bestPath = path
          bestCycle = cycle
        }
      }

      println("Best Path ${bestCycle}/${cycle}: ${bestPath?.sugar}")
      progress(cycle.toFloat() / cycleLimit.toFloat())
      // delay(10)
    }

    val delivery = Delivery(
      parcels = bestPath?.getParcels() ?: emptyList(),
      distance = bestPath?.sugar?.times(110.574)?.toFloat() ?: 0.0f,
      duration = bestPath?.getDuration() ?: 0.0f
    )
    return delivery
  }

}