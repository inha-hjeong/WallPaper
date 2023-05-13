package uz.adkhamjon.mobileprogrammingproject.ui.screens.image

import android.app.WallpaperManager
import android.content.Context
import android.content.Intent
import android.content.Intent.*
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.DrawableRes
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.VectorComposable
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat.startActivity
import androidx.core.graphics.drawable.toBitmap
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.decode.BitmapFactoryDecoder
import coil.request.ImageRequest
import coil.request.SuccessResult
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.skydoves.cloudy.Cloudy
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import net.engawapg.lib.zoomable.rememberZoomState
import net.engawapg.lib.zoomable.zoomable
import uz.adkhamjon.mobileprogrammingproject.R
import uz.adkhamjon.mobileprogrammingproject.utils.*
import java.io.IOException

const val DEFAULT = "default"
const val EDIT = "edit"
const val SHARE = "share"
const val DOWNLOAD = "download"
const val WALLPAPER = "wallpaper"
const val BACK = "back"
const val INITIAL = "initial"
const val EDITED = "edited"

@Destination
@Composable
fun ImageScreen(
    navigator: DestinationsNavigator,
    image: String = ""
) {
    val context = LocalContext.current
    var rotate by remember {
        mutableStateOf(0)
    }
    var optionTypes by rememberSaveable {
        mutableStateOf(DEFAULT)
    }
    var actions by rememberSaveable {
        mutableStateOf("")
    }
    var paddingToImage by rememberSaveable {
        mutableStateOf(INITIAL)
    }
    var drawable by remember {
        mutableStateOf(Any())
    }
    val rememberCoroutineScope = rememberCoroutineScope()
    val loading = ImageLoader(LocalContext.current)
    val request = ImageRequest.Builder(LocalContext.current)
        .data(image)
        .build()

    rememberCoroutineScope.launch {
        drawable = (loading.execute(request) as SuccessResult).drawable
        Log.d("TAG", "coroutine: $drawable")

    }

    when (actions) {
        EDIT -> {

        }
        BACK -> {
            navigator.popBackStack()
            actions = ""
        }
        SHARE -> {
            SharIntent(txt = image)
            actions = ""
        }
        DOWNLOAD -> {
            val downloader = AndroidDownloader(LocalContext.current)
            downloader.downloadFile(image)
            Log.d("TAG", "coroutine: $drawable")

            Toast.makeText(
                LocalContext.current,
                "Image Downloaded Successfully!!",
                Toast.LENGTH_SHORT
            ).show()
            actions = ""
        }
        WALLPAPER -> {
            setWallpaper(context, (drawable as BitmapDrawable).bitmap)
            actions = ""
        }
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black),
        contentAlignment = Alignment.Center
    ) {
        AsyncImage(
            model = image,
            contentDescription = "Image item",
            contentScale = ContentScale.Fit,
            modifier = if (paddingToImage == INITIAL) {
                Modifier
                    .fillMaxSize()
            } else {
                Modifier
                    .height(300.dp)
                    .padding(horizontal = 16.dp)
                    .rotate(rotate.toFloat())
                    .zoomable(rememberZoomState(3.0F))
            }
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.0F))
        )
        when (optionTypes) {
            DEFAULT -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        BlurButton(
                            icon = Icons.Default.ArrowBack,
                            onClick = {
                                actions = BACK
                                paddingToImage = INITIAL
                            }
                        )
                        BlurButton(
                            icon = Icons.Default.Edit,
                            onClick = {
                                optionTypes = EDIT
                                paddingToImage = EDITED
                            }
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        BlurButton(
                            icon = Icons.Default.KeyboardArrowDown,
                            onClick = {
                                actions = DOWNLOAD
                            }
                        )
                        BlurButton(
                            icon = Icons.Default.Lock,
                            onClick = {
                                actions = WALLPAPER
                            }
                        )
                        BlurButton(
                            icon = Icons.Default.Share
                        ) {
                            actions = SHARE
                        }
                    }
                }
            }
            EDIT -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        BlurButton(
                            icon = Icons.Default.ArrowBack,
                            onClick = {
                                optionTypes = DEFAULT
                                paddingToImage = INITIAL
                            }
                        )
                    }
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Bottom
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            BlurButtonEdit(
                                icon = R.drawable.baseline_rotate_left_24
                            ) {
                                rotate -= 90
                            }
                            Spacer(
                                modifier = Modifier.width(16.dp)
                            )
                            BlurButtonEdit(
                                icon = R.drawable.baseline_rotate_right_24
                            ) {
                                rotate += 90
                            }
                        }
                    }
                }
            }
        }
    }
}


@Composable
fun BlurButton(
    icon: @VectorComposable ImageVector,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier.size(48.dp),
        shape = RoundedCornerShape(24.dp),
        elevation = 0.dp
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0x99FFFFFF))
                .clickable { onClick() },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = "",
                tint = Color.Black,
                modifier = Modifier
                    .size(24.dp)
            )
        }
    }
}

@Composable
fun BlurButtonEdit(
    icon: Int,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier.size(48.dp),
        shape = RoundedCornerShape(24.dp),
        elevation = 0.dp
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0x99FFFFFF))
                .clickable { onClick() },
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = icon),
                contentDescription = "",
                modifier = Modifier
                    .size(24.dp)
            )
        }
    }
}

@Composable
fun SharIntent(txt: String) {
    val shareIntent = Intent().apply {
        action = ACTION_SEND
        putExtra(EXTRA_TEXT, txt)
        type = "text/plain"
    }

    val chooserIntent = createChooser(shareIntent, "Share Image")
    LocalContext.current.startActivity(chooserIntent)
}

@Composable
fun setWallpaper(context: Context, bitmap: Bitmap) {
    val wallpaperManager = WallpaperManager.getInstance(context)
    wallpaperManager.setBitmap(bitmap)
    Toast.makeText(
        context,
        "Wallpaper Set Successfully!!",
        Toast.LENGTH_SHORT
    ).show()
}