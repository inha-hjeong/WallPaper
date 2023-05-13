package uz.adkhamjon.mobileprogrammingproject.data.paging

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import uz.adkhamjon.mobileprogrammingproject.data.remote.ImageApiService
import uz.adkhamjon.mobileprogrammingproject.data.remote.dto.Hit
import uz.adkhamjon.mobileprogrammingproject.domain.repository.ImageRepository
import javax.inject.Inject

class ImagePagination @Inject constructor(
    private val imageRepository: ImageRepository,
    private val category: String
) : PagingSource<Int, Hit>() {
    override fun getRefreshKey(state: PagingState<Int, Hit>): Int? {
        return state.anchorPosition
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Hit> {
        return try {
            val nextPageNumber = params.key ?: 1
            val image = imageRepository.getImages(category, nextPageNumber)
            Log.d("TAG", "load: ${image.hits}")
            if (nextPageNumber > 1) {
                LoadResult.Page(image.hits, nextPageNumber - 1, nextPageNumber + 1)
            } else {
                LoadResult.Page(image.hits, null, nextPageNumber + 1)
            }
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }


}