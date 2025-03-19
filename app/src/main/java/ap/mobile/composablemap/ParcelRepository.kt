package ap.mobile.composablemap

import androidx.compose.runtime.MutableState
import ap.mobile.composablemap.abc.Colony
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext

sealed class Result<out R> {
  data class Success<out T>(val data: T) : Result<T>()
  data class Error(val exception: Exception) : Result<Nothing>()
}

class ParcelRepository {
  private val parcels = mutableListOf<Parcel>()

  init {
    // Pre-populate the repository with some sample data
    // parcels.add(Parcel(1, name = "user1", address = "user1@example.com"))
    // parcels.add(Parcel(2, name = "user2", address = "user2@example.com"))
    // parcels.add(Parcel(3, name = "user3", address = "user3@example.com"))
    // parcels.add(Parcel(4, name = "user4", address = "user4@example.com"))
    // parcels.add(Parcel(5, name = "user5", address = "user5@example.com"))
    // parcels.add(Parcel(6, name = "user6", address = "user6@example.com"))
    // parcels.add(Parcel(7, name = "user7", address = "user7@example.com"))
    // parcels.add(Parcel(8, name = "user8", address = "user8@example.com"))
    // parcels.add(Parcel(9, name = "user9", address = "user9@example.com"))
    // parcels.add(Parcel(10, name = "user10", address = "user10@example.com"))
    
    parcels.add(Parcel(1,lat=-8.01815,lng=112.62943,type="Regular",name="Nizar Zulfikar",address="Simo Jawar VII 54, Jawa Timur"))
    parcels.add(Parcel(2,lat=-8.01895,lng=112.62941,type="Regular",name="Ibrahim Eka",address="Blora R Gg III 40, Jakarta"))
    parcels.add(Parcel(3,lat=-8.01896,lng=112.63076,type="Regular",name="Halim Yunus",address="Kapuk Indah 11 B, Jakarta"))
    parcels.add(Parcel(4,lat=-8.01905,lng=112.63071,type="Regular",name="Yusuf Ilham",address="Kompl Harmoni Mas Jembatan Dua 11 Bl A, Jakarta"))
    parcels.add(Parcel(5,lat=-8.01966,lng=112.63113,type="Regular",name="Nissa Afifah",address="Balongsari Tama Bl 3 A/33, Jawa Timur"))
    parcels.add(Parcel(6,lat=-8.02048,lng=112.63174,type="Priority",name="Idris Muhamad",address="Dukuh Kupang Brt 31, Jawa Timur"))
    parcels.add(Parcel(7,lat=-8.0186,lng=112.63117,type="Regular",name="Wati Indra",address="Mojo Kidul 3, Jawa Timur"))
    parcels.add(Parcel(8,lat=-8.01845,lng=112.63118,type="Regular",name="Buana Sri Wahyuni",address="Cilandak KKO Cilandak Commercial Estate Bldg #202, Jakarta"))
    parcels.add(Parcel(9,lat=-8.01747,lng=112.62969,type="Regular",name="Amin Adam",address="Raya Cibeureum Blk 30, Jawa Barat"))
    parcels.add(Parcel(10,lat=-8.01732,lng=112.63092,type="Regular",name="Sri Wahyuni Harun",address="Puspowarno Tgh IX 2, Jawa Tengah"))
    parcels.add(Parcel(11,lat=-8.01756,lng=112.63124,type="Regular",name="Rizky Sultan",address="Kayoon 24, Jawa Timur"))
    parcels.add(Parcel(12,lat=-8.01705,lng=112.62949,type="Priority",name="Jamil Yusuf",address="Jend A Yani Km 7/29, Jawa Barat"))
    parcels.add(Parcel(13,lat=-8.01677,lng=112.62984,type="Regular",name="Jamilah Fikri",address="Baliwerti 119-121 Kav 6, Jawa Timur"))
    parcels.add(Parcel(14,lat=-8.01632,lng=112.62974,type="Regular",name="Alya Batari",address="Jaksa Agung Suprapto 17, Jawa Timur"))
    parcels.add(Parcel(15,lat=-8.01591,lng=112.63015,type="Regular",name="Yahya Fatimah",address="Topografi 98 H, Jawa Barat"))
    parcels.add(Parcel(16,lat=-8.0148,lng=112.63011,type="Regular",name="Putra Faisal",address="Danau Sunter Brt Bl A-1/8, Jakarta"))
    parcels.add(Parcel(17,lat=-8.015,lng=112.6305,type="Priority",name="Jamilah Akhmad",address="Riung Tingtrim III/1, Jawa Barat"))
    parcels.add(Parcel(18,lat=-8.01419,lng=112.62993,type="Regular",name="Imam Zulfikar",address="Ngagel Jaya Slt III/14, Jawa Timur"))
    parcels.add(Parcel(19,lat=-8.01384,lng=112.62976,type="Regular",name="Wati Joko",address="Waspada 26, Jawa Timur"))
    parcels.add(Parcel(20,lat=-8.01385,lng=112.62981,type="Regular",name="Rustam Faris",address="Durian Slt I 6, Jawa Tengah"))
    parcels.add(Parcel(21,lat=-8.01385,lng=112.63029,type="Regular",name="Jamilah Farida",address="Laks L RE Martadinata 112, Jawa Barat"))
    parcels.add(Parcel(22,lat=-8.01356,lng=112.63035,type="Regular",name="Salma Rizki",address="Singosari II/10, Jawa Tengah"))
    parcels.add(Parcel(23,lat=-8.01304,lng=112.63045,type="Priority",name="Indra Cinta",address="Tj Morawa Km 13/8, Sumatera Utara"))
    parcels.add(Parcel(24,lat=-8.01271,lng=112.63007,type="Regular",name="Sri Purnama",address="Agung Timur 4 Blok O No. 2 Kav. 18-19, Sunter Podomoro, North Jakarta"))
    parcels.add(Parcel(25,lat=-8.01505,lng=112.63004,type="Regular",name="Yuda Burhanuddin",address="Cisanggiri III 10, Jakarta"))
    parcels.add(Parcel(26,lat=-8.01701,lng=112.62846,type="Regular",name="Cahya Kusuma",address="Cukang Kawung 36, Jawa Barat"))
    parcels.add(Parcel(27,lat=-8.02218,lng=112.62878,type="Priority",name="Sitti Ruslan",address="Gedawang RT 002/2I, Jawa Tengah"))
    parcels.add(Parcel(28,lat=-8.02255,lng=112.63005,type="Regular",name="Fatimah Haris",address="Pasir Kaliki 6-8, Jawa Barat"))
    parcels.add(Parcel(29,lat=-8.02289,lng=112.63026,type="Regular",name="Kasih Said",address="Gatot Subroto No. 86, Rt. 30, Banjarmasin"))
    parcels.add(Parcel(30,lat=-8.02334,lng=112.63014,type="Regular",name="Sutrisno Malik",address="Manyar Sambongan 56, Jawa Timur"))

  }

  suspend fun getAllParcels(): List<Parcel> {
    return parcels.toList()
  }

  suspend fun computeDelivery(progress: (Float) -> Unit) : Result<List<Parcel>> {
    val delivery: List<Parcel> = parcels.shuffled()
    return withContext(Dispatchers.IO) {
      var abc = Colony(delivery, progress = progress)
      abc.compute()
      Result.Success(delivery)
    }
  }
}