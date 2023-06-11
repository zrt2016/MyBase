package com.zrt.kotlinapp.activity_view

import android.os.Bundle
import android.text.Html
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.zrt.kotlinapp.BasicActivity
import com.zrt.kotlinapp.R
import com.zrt.kotlinapp.learnkotlin.log
import kotlinx.android.synthetic.main.activity_u_i.*

class UIActivity : BasicActivity(), View.OnClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun getLayoutResID(): Int = R.layout.activity_u_i

    override fun initData() {
        ui_button.setOnClickListener(this)
        val spanned = Html.fromHtml("该医嘱执行时间未超过10分钟")
        ui_text.setText(spanned)
        log("##UI Text：${spanned.toString()}")
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.ui_button -> {
                Toast.makeText(this, ui_edittext.editableText.toString(), Toast.LENGTH_SHORT).show()
                ui_imageview.setImageResource(R.mipmap.all_depart_yes)
                // 圆形进度条
                if (ui_progressbar.visibility == View.VISIBLE) {
                    ui_progressbar.visibility = View.GONE
                } else {
                    ui_progressbar.visibility = View.VISIBLE
                }
                // 水平线进度条
                ui_progressbar_line.progress = ui_progressbar_line.progress + 10
            }
            R.id.ui_alertdialog_btn -> {
                showAlertDialog()
            }
        }
    }

    fun showAlertDialog(){
        AlertDialog.Builder(this).apply {
            setTitle("This is Dialog")
            setMessage("Something important.")
            setCancelable(false)
            setPositiveButton("OK"){ dialog, which ->
                dialog.dismiss()
            }
            setNegativeButton("No"){ dialog, which ->
                dialog.dismiss()
            }
            show()
        }
    }
}