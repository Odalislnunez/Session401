package es.usj.mastertsa.onunez.session401

import android.app.job.JobInfo
import android.app.job.JobParameters
import android.app.job.JobScheduler
import android.app.job.JobService
import android.content.ComponentName
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.widget.Toast
import es.usj.mastertsa.onunez.session401.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private val bindings : ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(bindings.root)
        bindings.btnSendMessage.setOnClickListener {
            sendMessage()
        }
    }

    private fun sendMessage() {
        val text = bindings.etMessage.text.toString()
        val serviceComponent = ComponentName(this, DelayedMessageService::class.java)
        val builder = JobInfo.Builder(0, serviceComponent)
        builder.setMinimumLatency((3 * 1000).toLong()) // Wait at least 30s
        builder.setOverrideDeadline((6 * 1000).toLong()) // Maximum delay 60s
        builder.setExtras(
            PersistableBundle().apply {
                putString(DelayedMessageService.EXTRA_MESSAGE, text)
            }
        )
        val service = getSystemService(JOB_SCHEDULER_SERVICE) as JobScheduler
        service.schedule(builder.build())
    }
}

class DelayedMessageService : JobService() {
    private fun showText(text: String?) {
        Log.v(getSystemServiceName(this::class.java), "The message is: ${text}")
        Toast.makeText(this, "The message is: ${text}",
            Toast.LENGTH_LONG).show()
    }
    companion object {
        const val EXTRA_MESSAGE = "Important notice"
    }
    override fun onStartJob(params: JobParameters?): Boolean {
        synchronized(this) {
            Thread.sleep(1000)
            var text = params!!.extras[EXTRA_MESSAGE].toString()
            showText(text)
        }
        return true
    }
    override fun onStopJob(params: JobParameters?): Boolean {
        return true
    }
}