package com.zrt.kotlinapp.activity_view.material_design

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.zrt.kotlinapp.BasicActivity
import com.zrt.kotlinapp.R
import com.zrt.kotlinapp.activity_view.material_design.demo.DrawerToolbarActivity
import com.zrt.kotlinapp.utils.ToastUtils
import kotlinx.android.synthetic.main.activity_toolbar.*

/**
 * toolbar继承了ActionBar的所有功能，而且灵活性更高
 * ActionBar中的主题是在AndroidManif.xml的application中配置android:theme="@style/AppTheme"
 * 现在使用Toolbar代替ActionBar,需要指定一个不带ActionBar的主题
 * 可以在styles.xml 中配置Theme.AppCompat.NoActionBar和Theme.AppCompat.Light.NoActionBar
 *
 * <style name="NoAppTheme" parent="Theme.AppCompat.Light.DarkActionBar">
 *      <!-- Customize your theme here. -->
 *      <item name="colorPrimary">@color/colorPrimary</item>            title背景色
 *      <item name="colorPrimaryDark">@color/colorPrimaryDark</item>    顶部信号栏背景色
 *      <item name="colorAccent">@color/colorAccent</item>              悬浮按钮添加背景色
 *      <item name="android:textColorPrimary">@color/orange</item>      顶部title字体颜色
 *      <item name="android:windowBackground">@color/white</item>       背景色
 *      <item name="android:navigationBarColor">@color/black</item>     底部虚拟键盘背景色 在api21后
 * </style>
 * <style name="NoAppTheme" parent="Theme.AppCompat.NoActionBar"></style>
 * <style name="NoAppTheme_Light" parent="Theme.AppCompat.Light.NoActionBar"></style>
 *
 * Menu配置
 * <!--    showAsAction是定义此item在ActionBar中的位置，常用的有-->
 * <!--    never 永远不会在标题栏上直接显示，必须手动点击菜单才会显示-->
 * <!--    ifRoom 如果有空间就会在标题栏上显示，如果没有就需要手动点击菜单才会显示-->
 * <!--    always 永远都在标题栏显示，空间不够则不显示-->
 * <!--    这里需要说一下：标题栏的菜单按钮，如果你的手机没有自带菜单按钮，那么在标题栏的最右边就会有3个竖直排列的小点；如果你的手机自带了，那么菜单按钮就是你手机上自带的那个-->
 * <!--    ####orderInCategory：决定每个Item的次序-->
 */
class ToolbarActivity : BasicActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSupportActionBar(a_toolbar)
    }

    override fun getLayoutResID(): Int = R.layout.activity_toolbar

    override fun initData() {
        a_t_btn_dt.setOnClickListener {
            startActivity(Intent(this, DrawerToolbarActivity::class.java))
        }
    }

    /**
     * 在onResume生命周期后调用
     * @param menu
     * @return
     */
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_menu, menu)

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.back_up ->
                ToastUtils.show(this, "You clicked BackUp")
            R.id.delete -> ToastUtils.show(this, "You clicked Delete")
            R.id.settings -> ToastUtils.show("You clicl Settings")
        }
        return true
    }
}