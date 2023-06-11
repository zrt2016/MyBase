package com.zrt.kotlinapp.activity_view.material_design

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.core.view.GravityCompat
import com.google.android.material.snackbar.Snackbar
import com.zrt.kotlinapp.BasicActivity
import com.zrt.kotlinapp.R
import com.zrt.kotlinapp.utils.ToastUtils
import kotlinx.android.synthetic.main.activity_float_action_button.*
import kotlinx.android.synthetic.main.toolbar_title.*

/**
 * 悬浮按钮
 * <com.google.android.material.floatingactionbutton.FloatingActionButton
 * android:id="@+id/a_fab"
 * android:layout_width="wrap_content"
 * android:layout_height="wrap_content"
 * android:layout_margin="10dp"
 * android:src="@mipmap/ic_done"
 * android:elevation="8dp"
 * app:layout_constraintRight_toRightOf="parent"
 * app:layout_constraintBottom_toBottomOf="parent"
 * tools:ignore="MissingConstraints" />
 *
 * elevation:设置悬浮框阴影
 * app:layout_anchor="@id/a_ct_appbar" 设置锚点，即指定父布局范围内
 * app:layout_anchorGravity="bottom|end" 将悬浮按钮指定在右下角
 * android:elevation="6dp"      设置正常状态下的阴影大小
 * android:backgroundTint="@color/red" 背景色
 * app:pressedTranslationZ="10dp" 设置点击时阴影的大小
 *
 * Snackbar 提示选择框
 * 点击会Snackbar会遮挡祝悬浮框，因此可使用CoordinatorLayout替换悬浮框的外出布局
 */
class FloatActionButtonActivity : BasicActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSupportActionBar(toolbar)
        supportActionBar?.let {
            // 显示导航按钮
            it.setDisplayHomeAsUpEnabled(true)
            // 设置导航按钮图标
            it.setHomeAsUpIndicator(R.mipmap.ic_menu)
        }

        // 设置默认选中项
        a_fab_navView.setCheckedItem(R.id.navCall)
        // 设置菜单子项监听器
        a_fab_navView.setNavigationItemSelectedListener {
            // 关闭菜单滑动
            drawer_layout.closeDrawers()
            true
        }
    }

    override fun getLayoutResID(): Int = R.layout.activity_float_action_button

    override fun initData() {
        a_fab.setOnClickListener{ view ->
            ToastUtils.show("FAB Clicked")
            Snackbar.make(view, "Data deleted", Snackbar.LENGTH_SHORT)
                    .setAction("Undo"){
                        ToastUtils.show("Data Restored")
                    }
                    .show()
        }
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_menu, menu)
        return true
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            // GravityCompat.START 保证与设置的第二个布局行为一致
            android.R.id.home -> drawer_layout.openDrawer(GravityCompat.START)
            R.id.back_up -> ToastUtils.show(this, "You clicked BackUp")
            R.id.delete -> ToastUtils.show(this, "You clicked Delete")
            R.id.settings -> ToastUtils.show("You clicl Settings")
        }
        return true
    }
}