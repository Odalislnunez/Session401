package es.usj.mastertsa.onunez.session401

import android.app.Service
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Binder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.IBinder
import android.os.SystemClock
import android.util.Log
import android.widget.Chronometer
import es.usj.mastertsa.onunez.session401.databinding.ActivitySecondBinding

class SecondActivity : AppCompatActivity() {
    lateinit var mBoundService: BoundService
    internal var mServiceBound = false

    private val bindings : ActivitySecondBinding by lazy {
        ActivitySecondBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(bindings.root)
        bindings.btnStop.setOnClickListener { stop() }
        bindings.btnPrintTimestamp.setOnClickListener { printTimestamp() }
    }

    private fun stop() {
        if (mServiceBound) { unbindService(mServiceConnection)
            mServiceBound = false
        }
        val intent = Intent(
            this@SecondActivity,
            BoundService::class.java
        )
        stopService(intent)
    }
    private fun printTimestamp() {
        if (mServiceBound) {
            bindings.tv1.text = mBoundService.getTimestamp()
        }
    }

    override fun onStart() {
        super.onStart()
        val intent = Intent(this, BoundService::class.java)
        startService(intent)
        bindService(intent, mServiceConnection,
            Context.BIND_AUTO_CREATE)
    }
    override fun onStop() {
        super.onStop()
        if (mServiceBound) {
            unbindService(mServiceConnection)
            mServiceBound = false
        }
    }

    private val mServiceConnection = object : ServiceConnection {
        override fun onServiceDisconnected(name: ComponentName) {
            mServiceBound = false
        }
        override fun onServiceConnected(name: ComponentName, service: IBinder) {
            val myBinder = service as BoundService.BoundServiceBinder
            mBoundService = myBinder.service
            mServiceBound = true
        }
    }

}

class BoundService : Service() {
    private val LOG_TAG = "BoundService"
    private val mBinder = BoundServiceBinder()
    private var mChronometer: Chronometer? = null

    override fun onCreate() {
        super.onCreate()
        Log.v(LOG_TAG, "in onCreate")
        mChronometer = Chronometer(this)
        mChronometer!!.base = SystemClock.elapsedRealtime()
        mChronometer!!.start()
    }
    override fun onBind(intent: Intent): IBinder {
        Log.v(LOG_TAG, "in onBind")
        return mBinder
    }
    override fun onRebind(intent: Intent) {
        Log.v(LOG_TAG, "in onRebind")
        super.onRebind(intent)
    }
    override fun onUnbind(intent: Intent): Boolean {
        Log.v(LOG_TAG, "in onUnbind")
        return true
    }
    override fun onDestroy() {
        super.onDestroy()
        Log.v(LOG_TAG, "in onDestroy")
        mChronometer!!.stop()
    }

    fun getTimestamp(): String {
        val elapsedMillis = SystemClock.elapsedRealtime() -
                mChronometer!!.base
        val hours = (elapsedMillis / 3600000).toInt()
        val minutes = (elapsedMillis - hours * 3600000).toInt() /
                60000
        val seconds =
            (elapsedMillis - (hours * 3600000).toLong() - (minutes *
                    60000).toLong()).toInt() / 1000
        val millis =
            (elapsedMillis - (hours * 3600000).toLong() - (minutes *
                    60000).toLong() - (seconds * 1000).toLong()).toInt()
        return "$hours:$minutes:$seconds:$millis"
    }

    inner class BoundServiceBinder : Binder() {
        internal val service: BoundService
            get() = this@BoundService
    }
}

