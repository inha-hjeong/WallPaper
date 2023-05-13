package uz.adkhamjon.mobileprogrammingproject.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import uz.adkhamjon.mobileprogrammingproject.data.paging.ImagePagination
import uz.adkhamjon.mobileprogrammingproject.data.remote.ImageApiService
import uz.adkhamjon.mobileprogrammingproject.data.remote.dto.Hit
import uz.adkhamjon.mobileprogrammingproject.data.remote.dto.ImageResponse
import uz.adkhamjon.mobileprogrammingproject.domain.repository.ImageRepository
import javax.inject.Inject

class ImageRepositoryImpl @Inject constructor(
    private val imageApiService: ImageApiService
) : ImageRepository {
    override suspend fun getImages(type: String, page: Int): ImageResponse {
        return imageApiService.getImages(page = page, category = type)
    }


}