package xyz.nbani.ecsetnaplo

import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.wear.compose.material3.AppScaffold
import xyz.nbani.ecsetnaplo.ui.nav.AppNavHost
import xyz.nbani.ecsetnaplo.ui.theme.EcsetTheme
import xyz.nbani.ecsetnaplo.ui.viewmodels.TimetableViewModel

const val EXTRA_DESTINATION = "destination"

class MainActivity : AppCompatActivity() {
    private var pendingDestination by mutableStateOf<String?>(null)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        pendingDestination = intent.getStringExtra(EXTRA_DESTINATION)

        setContent {
            val timetableViewModel: TimetableViewModel = viewModel(factory = TimetableViewModel.Factory)

            val settings by timetableViewModel.settings.collectAsStateWithLifecycle()
            val isOngoingActivityEnabled = settings?.devOptionsExpFeatOngoingActivity == true

            LaunchedEffect(isOngoingActivityEnabled) {
                if (isOngoingActivityEnabled) {
                    TimerService.start(this@MainActivity)
                } else {
                    TimerService.stop(this@MainActivity)
                }
            }

            EcsetTheme() {
                AppScaffold {
                    AppNavHost(
                        timetableViewModel = timetableViewModel,
                        pendingDestination = pendingDestination,
                        onDestinationHandled = { pendingDestination = null }
                    )
                }
            }
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        pendingDestination = intent.getStringExtra(EXTRA_DESTINATION)
    }
}