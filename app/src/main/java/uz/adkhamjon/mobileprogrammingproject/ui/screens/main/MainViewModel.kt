package uz.adkhamjon.mobileprogrammingproject.ui.screens.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import uz.adkhamjon.mobileprogrammingproject.data.paging.ImagePagination
import uz.adkhamjon.mobileprogrammingproject.data.remote.dto.Hit
import uz.adkhamjon.mobileprogrammingproject.domain.repository.ImageRepository
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val imageRepository: ImageRepository
) : ViewModel() {

    fun images(category: String) = Pager(
        PagingConfig(pageSize = 10)
    )
    {
        ImagePagination(imageRepository, category = category)
    }.flow.cachedIn(viewModelScope)

}