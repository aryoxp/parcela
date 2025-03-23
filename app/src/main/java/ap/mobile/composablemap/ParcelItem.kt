package ap.mobile.composablemap

import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.clustering.ClusterItem

class ParcelItem (
  private val lat: Double,
  private val lng: Double,
  private val title: String,
  private val snippet: String,
  private var parcel: Parcel,
) : ClusterItem {

  private val position: LatLng = LatLng(lat, lng)

  val isSelected: Boolean
    get() = parcel.selected

  init {
    this.parcel = parcel
  }

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

  fun getParcel() : Parcel {
    return parcel
  }

  // fun select(selected: Boolean = true) {
  //   parcel.selected = selected
  // }

}
