package ap.mobile.composablemap

import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.clustering.ClusterItem

class ParcelItem (
  private var parcel: Parcel,
) : ClusterItem {

  private val position: LatLng = LatLng(parcel.lat, parcel.lng)
  private val title = parcel.recipientName
  private val snippet = parcel.address

  var isSelected: Boolean
    get() = parcel.selected
    set(value) { parcel.selected = value }

  init {
    this.parcel = parcel
  }

  fun select(selected: Boolean = true): ParcelItem {
    isSelected = selected
    return this
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
    return 0f
  }

  fun getParcel() : Parcel {
    return parcel
  }

  // fun select(selected: Boolean = true) {
  //   parcel.selected = selected
  // }

}
