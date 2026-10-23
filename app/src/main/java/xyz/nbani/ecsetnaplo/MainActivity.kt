/* While this template provides a good starting point for using Wear Compose, you can always
 * take a look at https://github.com/android/wear-os-samples/tree/main/ComposeStarter to find the
 * most up to date changes to the libraries and their usages.
 */

package xyz.nbani.ecsetnaplo

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.wear.compose.material3.AppScaffold
import xyz.nbani.ecsetnaplo.ui.nav.AppNavHost
import xyz.nbani.ecsetnaplo.ui.theme.EcsetTheme
import xyz.nbani.ecsetnaplo.ui.viewmodels.TimetableViewModel

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val timetableViewModel: TimetableViewModel = viewModel(factory = TimetableViewModel.Factory)

            EcsetTheme() {
                AppScaffold {
                    AppNavHost(timetableViewModel = timetableViewModel)
                }
            }
        }
    }
}