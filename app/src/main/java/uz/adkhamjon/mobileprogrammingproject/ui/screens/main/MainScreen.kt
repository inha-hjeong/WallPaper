package uz.adkhamjon.mobileprogrammingproject.ui.screens.main

import android.content.Intent
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import coil.compose.AsyncImage
import coil.compose.rememberImagePainter
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.pagerTabIndicatorOffset
import com.google.accompanist.pager.rememberPagerState
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import uz.adkhamjon.mobileprogrammingproject.R
import uz.adkhamjon.mobileprogrammingproject.data.remote.dto.Hit
import uz.adkhamjon.mobileprogrammingproject.ui.screens.destinations.ImageScreenDestination
import uz.adkhamjon.mobileprogrammingproject.ui.screens.image.ImageScreen

@RootNavGraph(start = true)
@Destination
@Composable
fun MainScreen(
    navigator: DestinationsNavigator,
    mainViewModel: MainViewModel = hiltViewModel()
) {

    var openIntent by remember { mutableStateOf("") }
    if (openIntent.isNotBlank()) {
        OpenWebPage(openIntent)
        openIntent = ""
    }
    Column {
        Toolbar(info = {
            openIntent = "https://pixabay.com/"
        })
        TabViewPager(
            mainViewModel,
            item = {
                navigator.navigate(ImageScreenDestination(it))
            }
        )
    }
}

@Composable
fun Toolbar(
    info: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .background(Color(0xFF0C0C0C)),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Home",
            color = Color.White,
            fontSize = 16.sp,
            fontStyle = FontStyle.Normal,
            modifier = Modifier.padding(start = 16.dp)
        )

        Image(
            painter = painterResource(id = R.drawable.photo_src),
            contentDescription = "",
            modifier = Modifier
                .padding(end = 16.dp)
                .size(24.dp)
                .clickable {
                    info.invoke()
                }
        )

    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun TabViewPager(
    mainViewModel: MainViewModel,
    item: (String) -> Unit
) {
    val tabs = listOf("All", "Animals", "Buildings", "Food", "Nature", "People", "Technology")
    val pagerState = rememberPagerState(pageCount = tabs.size)
    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        ScrollableTabRow(
            selectedTabIndex = pagerState.currentPage,
            divider = {
                Spacer(modifier = Modifier.height(5.dp))
            },
            edgePadding = 0.dp,
            indicator = { tabPositions ->
                TabRowDefaults.Indicator(
                    modifier = Modifier.pagerTabIndicatorOffset(pagerState, tabPositions),
                    height = 2.dp,
                    color = Color.White
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
        ) {
            tabs.forEachIndexed { index, s ->
                Tab(
                    selected = pagerState.currentPage == index,
                    onClick = {
                        coroutineScope.launch {
                            pagerState.animateScrollToPage(index)
                        }
                    },
                    text = {
                        Text(text = s)
                    },
                    modifier = Modifier
                        .background(Color(0xFF0C0C0C))
                )
            }
        }
        HorizontalPager(state = pagerState) { page ->
            val images = mainViewModel.images(tabs[page]).collectAsLazyPagingItems()
            ImagesScreen(images) {
                item.invoke(it!!)
            }
        }
    }
}

@Composable
fun ImagesScreen(
    images: LazyPagingItems<Hit>,
    onclick: (String?) -> Unit
) {
    LazyVerticalGrid(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        columns = GridCells.Fixed(3)
    ) {
        itemsGrid(images) { hit ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(170.dp)
                    .background(Color.White)
                    .clickable {
                        onclick.invoke(hit?.webformatURL)
                    },
            ) {
                AsyncImage(
                    model = hit?.webformatURL,
                    contentDescription = "Image item",
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(0.8.dp),
                    contentScale = ContentScale.Crop,
                )
            }
        }
    }
}

@Composable
fun OpenWebPage(url: String) {
    val browser = Intent().apply {
        action = Intent.ACTION_VIEW
        data = Uri.parse(url)
    }
    LocalContext.current.startActivity(browser)
}

fun <T : Any> LazyGridScope.itemsGrid(
    items: LazyPagingItems<T>,
    key: ((item: T) -> Any)? = null,
    span: ((item: T) -> GridItemSpan)? = null,
    contentType: ((item: T) -> Any)? = null,
    itemContent: @Composable LazyGridItemScope.(value: T?) -> Unit
) {
    items(
        count = items.itemCount,
        key = if (key == null) null else { index ->
            val item = items.peek(index)
            if (item != null) {
                key(item)
            }
        },
        span = if (span == null) null else { index ->
            val item = items.peek(index)
            if (item == null) {
                GridItemSpan(1)
            } else {
                span(item)
            }
        },
        contentType = if (contentType == null) {
            { null }
        } else { index ->
            val item = items.peek(index)
            if (item == null) {
                null
            } else {
                contentType(item)
            }
        }
    ) { index ->
        itemContent(items[index])
    }
}