package uz.adkhamjon.mobileprogrammingproject

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.ramcosta.composedestinations.DestinationsNavHost
import dagger.hilt.android.AndroidEntryPoint
import uz.adkhamjon.mobileprogrammingproject.ui.screens.NavGraphs
import uz.adkhamjon.mobileprogrammingproject.ui.theme.MobileProgrammingProjectTheme


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MobileProgrammingProjectTheme {
                DestinationsNavHost(navGraph = NavGraphs.root)
            }
        }
    }
}