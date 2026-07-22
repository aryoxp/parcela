package ap.mobile.composablemap.aco

import ap.mobile.composablemap.model.ParcelMapItem
import ap.mobile.composablemap.optimizer.Delivery
import ap.mobile.composablemap.optimizer.IOptimizer
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.isActive

class AntColony(
  val parcels: List<ParcelMapItem>,
  numAnts: Int = 10,
  val cycleLimit: Int = 100,
  val cycleConvergence: Int = 30,
  val rho: Float = .5f,
  val startAtParcel: ParcelMapItem? = null,
  val useHeuristicInit: Boolean? = false
) : IOptimizer {

  override var bestCycle: Int = 0
    private set
  override var fitness: Double = 0.0
    private set

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
        if (useHeuristicInit == true)
          // Initialize pheromones with its inverse distance
          // instead of 1.0
          // Nearest destination is more preferred
          pheromoneMap[pb.id] = 1 / d
        else pheromoneMap[pb.id] = 1.0
      }
      distances[pa.id] = destMap
      pheromones[pa.id] = pheromoneMap
    }
  }

  override suspend fun compute(onProgress: suspend (Float) -> Unit): Delivery {
    var bestPath: Path? = null
    var bestCycle: Int
    var convergence = 0
    for (cycle in 1..cycleLimit) {
      if (!currentCoroutineContext().isActive) {
        return Delivery(
          parcels = bestPath?.getParcels() ?: emptyList(),
          distance = bestPath?.sugar?.times(110.574)?.toFloat() ?: 0.0f,
          duration = bestPath?.getDuration() ?: 0.0f
        )
      } // Fast fail manually
      currentCoroutineContext().ensureActive()
      println("Cycle $cycle")
      // Evaporate pheromones
      if (cycle > 1) {
        pheromones.values.forEach { a ->
          a.entries.forEach { b ->
            b.setValue(b.value * (1 - rho))
          }
        }
      }

      val lastBestCycle = this.bestCycle

      // Ants start scouting
      for (ant in ants) {
        val path = ant.moves(pheromones, startAtParcel)

        // check the path's sugar quality
        if (bestPath == null || path.sugar < bestPath.sugar) {
          bestPath = path
          bestCycle = cycle
          this.bestCycle = bestCycle
          this.fitness = bestPath.sugar
        }
      }

      if (lastBestCycle == this.bestCycle) convergence++
      else convergence = 0

      // println("Best Path ${bestCycle}/${cycle}: ${bestPath?.sugar}")
      // report(cycle, bestPath?.sugar ?: 0.0)
      onProgress(cycle.toFloat() / cycleLimit.toFloat())
      // delay(10)
      if (convergence >= cycleConvergence) {
        println("CONVERGE! at cycle: ${this.bestCycle}")
        break
      }
    }

    val delivery = Delivery(
      parcels = bestPath?.getParcels() ?: emptyList(),
      distance = bestPath?.sugar?.times(110.574)?.toFloat() ?: 0.0f,
      duration = bestPath?.getDuration() ?: 0.0f
    )
    return delivery
  }

}