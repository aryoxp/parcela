package ap.mobile.composablemap.model

import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.clustering.ClusterItem

class Parcel (
  private var parcel: ParcelMapItem
) : ClusterItem {

  override val position: LatLng = LatLng(parcel.lat, parcel.lng)
  override val title = parcel.recipientName
  override val snippet = parcel.address
  override val zIndex: Float = 0f


  var isSelected: Boolean
    get() = parcel.selected
    set(value) { parcel.selected = value }

  init {
    this.parcel = parcel
  }

  fun select(selected: Boolean = true): Parcel {
    isSelected = selected
    return this
  }

  // fun getPosition(): LatLng {
  //   return position
  // }
  //
  // fun getTitle(): String {
  //   return title
  // }
  //
  // fun getSnippet(): String {
  //   return snippet
  // }

  fun getZIndex(): Float {
    return 0f
  }

  fun getParcel() : ParcelMapItem {
    return parcel
  }

  // fun select(selected: Boolean = true) {
  //   parcel.selected = selected
  // }

}
