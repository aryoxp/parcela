package ap.mobile.composablemap.optimizer

interface IOptimizer {
  suspend fun compute() : Delivery
}