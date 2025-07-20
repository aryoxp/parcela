package ap.mobile.composablemap.optimizer

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.net.toUri

object Logger {

  @RequiresApi(Build.VERSION_CODES.Q)
  fun saveFile(context: Context, path: String, fileName: String, mode: String = "wt", content : String = "") {
    val uri = "file:///storage/emulated/0/$path/$fileName".toUri()
    val os = context.contentResolver.openOutputStream(uri, mode)
    os?.write(content.toByteArray())
    os?.close()
  }

}