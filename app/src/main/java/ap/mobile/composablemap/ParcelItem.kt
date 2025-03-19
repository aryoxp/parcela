package ap.mobile.composablemap

import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.clustering.ClusterItem

class ParcelItem (
  lat: Double,
  lng: Double,
  title: String,
  snippet: String
) : ClusterItem {

  private val position: LatLng
  private val title: String
  private val snippet: String

  override fun getPosition(): LatLng {
    return position
  }

  override fun getTitle(): String {
    return title
  }

  override fun getSnippet(): String {
    return snippet
  }

  override fun getZIndex(): Float? {
    return 1f
  }

  init {
    position = LatLng(lat, lng)
    this.title = title
    this.snippet = snippet
  }
}
