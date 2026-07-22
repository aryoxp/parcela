package ap.mobile.composablemap.repository

import ap.mobile.composablemap.entity.ParcelEntity
import ap.mobile.composablemap.model.ParcelMapItem
import ap.mobile.composablemap.optimizer.Delivery
import ap.mobile.composablemap.optimizer.IOptimizer
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import timber.log.Timber
import kotlin.system.measureTimeMillis

sealed interface ProgressStatus {
  data class Loading(val percentage: Float) : ProgressStatus
  data class Success(val parcels: List<ParcelMapItem>, val distance: Float, val duration: Float) : ProgressStatus
  data class Error(val message: String) : ProgressStatus
}

class ParcelRepository : Repository() {

  fun getAllParcels(): List<ParcelMapItem> {
    val response = RetrofitClient.apiService.getParcels().execute()
    if (response.isSuccessful) {
      val parcelsDto: List<ParcelEntity> = response.body() ?: emptyList()
      val parcels = mutableListOf<ParcelMapItem>()
      parcelsDto.forEach { p -> // mapping dari parcel DTO ke parcel "App"
        parcels.add(ParcelMapItem(
          id = p.id,
          lat = p.lat,
          lng = p.lng,
          type = p.type,
          recipientName = p.recipientName,
          address = p.address
        ))
      }
      return parcels
    } else return emptyList()
  }

  // @RequiresApi(Build.VERSION_CODES.Q)
  // private fun report(cycle: Int, fitness: Double) {
  //   // saveFile(
  //   //   context = context,
  //   //   path = "Download/data",
  //   //   fileName = "aco.csv".takeIf { this.optimizer == "ACO" } ?: "abc.csv",
  //   //   mode = "wa",
  //   //   content = "$cycle,$fitness\n",
  //   // )
  // }

  // @RequiresApi(Build.VERSION_CODES.Q)
  // suspend fun computeDelivery(
  //   progress: (Float) -> Unit,
  //   parcel: ParcelMapItem?,
  //   optimizer: Optimizer,
  //   useHeuristicInit: Boolean? = false
  // ): ComputeResult<Delivery> {
  //   this.optimizer = optimizer
  //   return withContext(Dispatchers.Main) {
  //     var delivery = Delivery(listOf(), 0f, 0f)
  //     for (i in 1..1) {
  //       // thread(start = true) {
  //         print("Sample $i\n")
  //         val opt = when (optimizer) {
  //           Optimizer.ACO ->
  //             AntColony(parcels, progress = progress, report = ::report, startAtParcel = parcel, useHeuristicInit = useHeuristicInit)
  //           Optimizer.ABC ->
  //             BeeColony(parcels, progress = progress, report = ::report, startAtParcel = parcel)
  //         }
  //         val elapsed = measureTimeMillis {
  //           // runBlocking(Dispatchers.IO) {
  //             delivery = opt.compute()
  //           // }
  //         }
  //         // println("Elapsed time: $elapsed, Best cycle: ${opt.bestCycle}")
  //         // saveFile(
  //         //   context = context,
  //         //   path = "Download/data",
  //         //   fileName = "aco-perf.csv".takeIf { optimizer == "ACO" } ?: "abc-perf.csv",
  //         //   mode = "wa",
  //         //   content = "$elapsed,${opt.bestCycle},${opt.fitness}\n",
  //         // )
  //         println("$elapsed ms, at cycle: ${opt.bestCycle}, fitness:${opt.fitness}\n")
  //         System.gc()
  //         sleep(100)
  //       // }
  //     }
  //     if (delivery.distance == 0f) ComputeResult.Error(Exception("Invalid result."))
  //     ComputeResult.Success(delivery)
  //   }
  // }

  fun getDeliverySequence(optimizer: IOptimizer) : Flow<ProgressStatus> = flow {
    val elapsed = measureTimeMillis {
      emit(ProgressStatus.Loading(percentage = 0f))

      // Try-catch assigns the variable or exits early
      val delivery: Delivery = try {
        optimizer.compute(onProgress = { percentage ->
          emit(ProgressStatus.Loading(percentage = percentage))
        })
      } catch (e: Exception) {
        emit(ProgressStatus.Error(e.message ?: "Unknown error."))
        return@flow // Stop executing the flow execution on failure
      }

      // This only runs if the try block succeeded
      emit(ProgressStatus.Success(delivery.parcels, delivery.distance, delivery.duration))
    }
    Timber.tag("Elapsed").d(elapsed.toString())
  }
}
