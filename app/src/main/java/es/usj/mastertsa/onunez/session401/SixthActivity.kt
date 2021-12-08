package es.usj.mastertsa.onunez.session401

import android.app.ProgressDialog
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView

class SixthActivity : AppCompatActivity() {
    private var button: Button? = null
    private var time: EditText? = null
    private var finalResult: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sixth)
        time = findViewById(R.id.in_time)
        button = findViewById(R.id.btn_run)
        finalResult = findViewById(R.id.tv_result)
        button!!.setOnClickListener {
            val runner = AsyncTaskRunner()
            val sleepTime = time!!.text.toString()
            runner.execute(sleepTime)
        }
    }

    private inner class AsyncTaskRunner : AsyncTask<String, String, String>() {
        private var resp: String? = null
        var progressDialog: ProgressDialog? = null
        override fun doInBackground(vararg params: String): String?
        {
            publishProgress("Sleeping...") // Call onProgressUpdate();
            try {
                val time = Integer.parseInt(params[0]) * 1000
                Thread.sleep(time.toLong())
                resp = "Slept for " + params[0] + " seconds"
            } catch (e: InterruptedException) {
                e.printStackTrace()
                resp = e.message
            } catch (e: Exception) {
                e.printStackTrace()
                resp = e.message
            }
            return resp
        }
        override fun onPostExecute(result: String) {
            progressDialog!!.dismiss()
            finalResult!!.text = result
        }
        override fun onPreExecute() {
            progressDialog = ProgressDialog.show(
                this@SixthActivity,
                "ProgressDialog",
                "Wait for " + time!!.text.toString() + " seconds"
            )
        }
        override fun onProgressUpdate(vararg text: String) {
            finalResult!!.text = text[0]
        }
    }
}