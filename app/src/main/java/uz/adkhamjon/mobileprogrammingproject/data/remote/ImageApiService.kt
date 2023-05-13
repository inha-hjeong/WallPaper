package uz.adkhamjon.mobileprogrammingproject.data.remote

import retrofit2.http.GET
import retrofit2.http.Query
import uz.adkhamjon.mobileprogrammingproject.data.remote.dto.ImageResponse

interface ImageApiService {

    @GET("?key=21513068-62dda8c9c630391d0aef56784&")
    suspend fun getImages(
        @Query("page") page: Int,
        @Query("q") category: String = "All",
        @Query("size") size: Int = 30
    ): ImageResponse
}