package ap.mobile.composablemap.repository

import android.content.Context
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

open class Repository {

  private val _progressFlow = MutableStateFlow(0)
  val progressFlow: StateFlow<Int> = _progressFlow

}