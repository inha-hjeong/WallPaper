package uz.adkhamjon.mobileprogrammingproject.domain.repository

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import uz.adkhamjon.mobileprogrammingproject.data.remote.dto.Hit
import uz.adkhamjon.mobileprogrammingproject.data.remote.dto.ImageResponse

interface ImageRepository {
    suspend fun getImages(type: String, page: Int): ImageResponse
}