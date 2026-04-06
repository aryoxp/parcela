package ap.mobile.composablemap.view

import android.content.Context
import ap.mobile.composablemap.model.Parcel
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.MarkerOptions
import com.google.maps.android.clustering.Cluster
import com.google.maps.android.clustering.ClusterManager
import com.google.maps.android.clustering.view.DefaultClusterRenderer

class MyClusterRenderer(context: Context,
                        map: GoogleMap, clusterManager: ClusterManager<Parcel>
) : DefaultClusterRenderer<Parcel>(context, map, clusterManager) {

  override fun onBeforeClusterRendered(cluster: Cluster<Parcel>, markerOptions: MarkerOptions) {
    // set anchor if present
    markerOptions.anchor(.5f, .5f)
    // markerAnchorConfig?.getAnchorOffsetFor(cluster.items.first(), true)?.let {
    //   markerOptions.anchor(it.x, it.y)
    // }

    super.onBeforeClusterRendered(cluster, markerOptions)
  }

  override fun onBeforeClusterItemRendered(item: Parcel, markerOptions: MarkerOptions) {
    // set anchor if present
    // markerAnchorConfig?.getAnchorOffsetFor(item, false)?.let { offset ->
    //   markerOptions.anchor(offset.x, offset.y)
    // }
    markerOptions.anchor(0f, 0f)
    super.onBeforeClusterItemRendered(item, markerOptions)
  }

}