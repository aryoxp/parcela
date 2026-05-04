package ap.mobile.composablemap.repository

import ap.mobile.composablemap.entity.ParcelEntity
import retrofit2.Call
import retrofit2.http.GET

interface ApiService {

  @GET("parcels.json")
  fun getParcels(): Call<List<ParcelEntity>>

}