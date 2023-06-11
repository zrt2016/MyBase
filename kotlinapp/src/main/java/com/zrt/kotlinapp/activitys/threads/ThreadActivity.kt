package com.zrt.kotlinapp.activitys.threads

import android.app.ProgressDialog
import android.os.*
import androidx.appcompat.app.AppCompatActivity
import androidx.loader.content.AsyncTaskLoader
import com.zrt.kotlinapp.BasicActivity
import com.zrt.kotlinapp.R
import com.zrt.kotlinapp.utils.ToastUtils
import kotlinx.android.synthetic.main.activity_thread.*
import kotlin.concurrent.thread

class ThreadActivity : BasicActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun getLayoutResID(): Int = R.layout.activity_thread

    override fun initData() {
        a_t_changeTextBtn.setOnClickListener {
            // 启动线程
            thread {
                val msg = Message()
                msg.what = updateText
                msg.obj = "Nice to meet you to"
                handler.sendMessage(msg)
            }
        }
        DownloadTask().execute()
    }
    val updateText = 1
    val handler = object : Handler(Looper.getMainLooper()){
        override fun handleMessage(msg: Message) {
            when(msg.what){
                updateText -> {
                    a_t_textView.setText(msg.obj?.toString()?: "Nice to meet you?")
                }
            }

        }
    }

    inner class DownloadTask: AsyncTask<Unit, Int, Boolean>(){
        val progressDialog = ProgressDialog(this@ThreadActivity)
        override fun onPreExecute() {
            super.onPreExecute()
            progressDialog.setMessage("Download 0%")
            progressDialog.show()
        }
        override fun doInBackground(vararg params: Unit?): Boolean {
            while(true){
                val downloadPercent = this@ThreadActivity.doDownload()
                publishProgress(downloadPercent)
                if (downloadPercent >= 100){
                    break
                }
            }
            return true
        }

        override fun onProgressUpdate(vararg values: Int?) {
//            super.onProgressUpdate(*values)
            progressDialog.setMessage("Download ${values[0]}%")
        }

        override fun onPostExecute(result: Boolean?) {
            progressDialog.dismiss()
            if (result?: false){
                ToastUtils.show(this@ThreadActivity, "Download sucess")
            }else{
                ToastUtils.show(this@ThreadActivity, "Download failed")
            }

        }
    }
    var currentDownload: Int = 0
    fun doDownload(): Int{
        currentDownload += 10
        Thread.sleep(1000)
        return currentDownload
    }
}