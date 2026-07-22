package ap.mobile.composablemap.usecase

import ap.mobile.composablemap.model.ParcelMapItem
import ap.mobile.composablemap.optimizer.IOptimizer
import ap.mobile.composablemap.repository.ParcelRepository
import ap.mobile.composablemap.repository.ProgressStatus
import kotlinx.coroutines.flow.Flow

class DeliveryUseCase {
  companion object { // static members dari DeliveryUseCase
    fun getPackagesToDeliver(repo: ParcelRepository): List<ParcelMapItem> {
      return repo.getAllParcels()
    }

    fun getDeliverySequence(repo: ParcelRepository, optimizer: IOptimizer) : Flow<ProgressStatus> {
      return repo.getDeliverySequence(optimizer)
    }
  }
}