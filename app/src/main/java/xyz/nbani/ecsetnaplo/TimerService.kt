package xyz.nbani.ecsetnaplo

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.ServiceInfo
import android.os.Build
import android.os.IBinder
import android.os.SystemClock
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.ServiceCompat
import androidx.wear.ongoing.OngoingActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import xyz.nbani.ecsetnaplo.data.model.TimetableEntry
import java.time.LocalDate
import java.time.ZoneId
import androidx.wear.ongoing.Status
import kotlinx.coroutines.cancel

class TimerService : Service() {

    companion object {
        private const val CHANNEL_ID = "ongoing_channel"
        private const val NOTIFICATION_ID = 1

        fun start(context: Context) {
            context.startForegroundService(Intent(context, TimerService::class.java))
        }

        fun stop(context: Context) {
            context.stopService(Intent(context, TimerService::class.java))
        }
    }

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main)
    private var job: Job? = null

    private val repository by lazy { (application as EcsetApp).timetableRepository }

    override fun onBind(intent: Intent?): IBinder? = null

    private fun createChannel() {
        getSystemService(NotificationManager::class.java).createNotificationChannel(
            NotificationChannel(
                CHANNEL_ID,
                getString(R.string.current_status),
                NotificationManager.IMPORTANCE_LOW
            )
        )
    }

    private fun placeholderNotification(): Notification =
        NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_monochrome)
            .setContentTitle(getString(R.string.current_status))
            .setOngoing(true)
            .build()

    private fun showOnGoing(entry: TimetableEntry) {
        val endMillis = entry.endTime
            .atDate(LocalDate.now())
            .atZone(ZoneId.systemDefault())
            .toInstant()
            .toEpochMilli()

        val remaining = (endMillis - System.currentTimeMillis()).coerceAtLeast(0L)
        val elapsedEnd = SystemClock.elapsedRealtime() + remaining

        val touchIntent = PendingIntent.getActivity(
            this, 0,
            Intent(this, MainActivity::class.java).apply {
                putExtra(EXTRA_DESTINATION, "current_lesson")
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_SINGLE_TOP
            },
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val builder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_monochrome)
            .setContentTitle(
                when (entry) {
                    is TimetableEntry.PeriodItem ->
                        getString(R.string.period_notification_title)
                    is TimetableEntry.Break ->
                        getString(R.string.break_text)
                }
            )
            .setCategory(NotificationCompat.CATEGORY_PROGRESS)
            .setOngoing(true)
            .setOnlyAlertOnce(true)
            .setContentIntent(touchIntent)
            .setUsesChronometer(true)
            .setChronometerCountDown(true)
            .setWhen(endMillis)
            .setRequestPromotedOngoing(true)

        val status = Status.Builder()
            .addTemplate(getString(R.string.remaining_time))
            .addPart("time", Status.TimerPart(elapsedEnd))
            .build()

        OngoingActivity.Builder(applicationContext, NOTIFICATION_ID, builder)
            .setStaticIcon(R.drawable.ic_launcher_monochrome)
            .setTouchIntent(touchIntent)
            .setStatus(status)
            .build()
            .apply(applicationContext)

        Log.d("TimerService", "posting real notification for $entry")

        if (Build.VERSION.SDK_INT >= 34) {
            ServiceCompat.startForeground(
                this,
                NOTIFICATION_ID,
                builder.build(),
                ServiceInfo.FOREGROUND_SERVICE_TYPE_SPECIAL_USE
            )
        } else {
            startForeground(NOTIFICATION_ID, builder.build())
        }
    }

    override fun onStartCommand(
        intent: Intent?,
        flags: Int,
        startId: Int,
    ): Int {
        createChannel()

        if (Build.VERSION.SDK_INT >= 34) {
            ServiceCompat.startForeground(
                this,
                NOTIFICATION_ID,
                placeholderNotification(),
                ServiceInfo.FOREGROUND_SERVICE_TYPE_SPECIAL_USE
            )
        } else {
            startForeground(NOTIFICATION_ID, placeholderNotification())
        }

        if (job == null) {
            job = scope.launch {
                repository.observeCurrentPeriodOrBreak().collect { entry ->
                    if (entry == null) stopSelf() else showOnGoing(entry)
                }
            }
        }
        return START_NOT_STICKY
    }

    override fun onDestroy() {
        scope.cancel()
        super.onDestroy()
    }
}