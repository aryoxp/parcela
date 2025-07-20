package ap.mobile.composablemap.optimizer

interface IOptimizer {
  val bestCycle: Int
  val fitness: Double
  suspend fun compute() : Delivery
}