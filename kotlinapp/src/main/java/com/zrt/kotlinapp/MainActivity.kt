package com.zrt.kotlinapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import com.zrt.kotlinapp.activitys.intents.IntentActivity
import com.zrt.kotlinapp.activity_view.MenuActivity
import com.zrt.kotlinapp.activity_view.UIActivity
import com.zrt.kotlinapp.activity_view.actionbar.TitleActivity
import com.zrt.kotlinapp.activity_view.custom_basic.CustomViewNavigationActivity
import com.zrt.kotlinapp.activity_view.custom_basic.view_shijianfenfa.ViewEventActivity
import com.zrt.kotlinapp.activity_view.custom_basic.ViewScrollActivity
import com.zrt.kotlinapp.activity_view.listview.LVActivity
import com.zrt.kotlinapp.activity_view.material_design.*
import com.zrt.kotlinapp.activity_view.recyclerview.RecyclerViewActivity
import com.zrt.kotlinapp.activity_view.recyclerview.RecyclerViewHActivity
import com.zrt.kotlinapp.activity_view.recyclerview.RecyclerViewSGLActivity
import com.zrt.kotlinapp.activity_view.recyclerview.chat.ChatInterfaceActivity
import com.zrt.kotlinapp.activitys.*
import com.zrt.kotlinapp.activitys.camera.CameraActivity
import com.zrt.kotlinapp.activitys.threads.ThreadActivity
import com.zrt.kotlinapp.animator.AnimationBasicActivity
import com.zrt.kotlinapp.broadcasts.BCActivity
import com.zrt.kotlinapp.contentproviders.ContactsActivity
import com.zrt.kotlinapp.contentproviders.VisitSQLiteDBActivity
import com.zrt.kotlinapp.fragment.*
import com.zrt.kotlinapp.jetpack.RoomActivity
import com.zrt.kotlinapp.jetpack.ViewModeActivity
import com.zrt.kotlinapp.notification.NotificationActivity
import com.zrt.kotlinapp.services.ServiceActivity
import com.zrt.kotlinapp.storage.SQLiteActivity
import com.zrt.kotlinapp.storage.StorageActivity
import com.zrt.kotlinapp.webviews.WebViewActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BasicActivity() {
//    val classList = ArrayList<KClass<out BasicActivity>>()
//    val dataList = ArrayList<DataState>()
    val dataMap = HashMap<Int, Class<out BasicActivity>>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("MainActivity", "onCreate execute")

    }

    override fun getLayoutResID(): Int = R.layout.activity_main

    override fun initData() {
//        classList.add(FirstActivity::class)
//        dataList.add(DataState("", FirstActivity::class.java))
        dataMap.put(main_btn1.id, FirstActivity::class.java)
        dataMap.put(main_menu_btn.id, MenuActivity::class.java)
        dataMap.put(main_intent_btn.id, IntentActivity::class.java)
        dataMap.put(main_ui_btn.id, UIActivity::class.java)
        dataMap.put(main_title_btn.id, TitleActivity::class.java)
        dataMap.put(main_lv_btn.id, LVActivity::class.java)
        dataMap.put(main_rv_btn.id, RecyclerViewActivity::class.java)
        dataMap.put(main_rv_h_btn.id, RecyclerViewHActivity::class.java)
        dataMap.put(main_sgl_h_btn.id, RecyclerViewSGLActivity::class.java)
        dataMap.put(main_chat_btn.id, ChatInterfaceActivity::class.java)
        dataMap.put(main_f_one_btn.id, FOneActivity::class.java)
        dataMap.put(main_f_two_btn.id, FTwoActivity::class.java)
        dataMap.put(main_f_large_btn.id, LargeActivity::class.java)
        dataMap.put(main_f_new_title.id, NewsTitleActivity::class.java)
        dataMap.put(main_broadcasts.id, BCActivity::class.java)
        dataMap.put(main_storage_file.id, StorageActivity::class.java)
        dataMap.put(main_sql.id, SQLiteActivity::class.java)
        dataMap.put(main_contact.id, ContactsActivity::class.java)
        // contentProvite 调用第三方APP的数据
        dataMap.put(main_visitSQLite.id, VisitSQLiteDBActivity::class.java)
        dataMap.put(main_notification.id, NotificationActivity::class.java)
        dataMap.put(main_camera.id, CameraActivity::class.java)
        dataMap.put(main_media_player.id, MediaPlayerActivity::class.java)
        dataMap.put(main_video_view.id, VideoViewActivity::class.java)
        dataMap.put(main_thread.id, ThreadActivity::class.java)
        dataMap.put(main_service.id, ServiceActivity::class.java)
        dataMap.put(main_webview.id, WebViewActivity::class.java)
        dataMap.put(main_http.id, HttpActivity::class.java)
        dataMap.put(main_json_xml.id, JsonXmlActivity::class.java)
        dataMap.put(main_Material.id, MaterialActivity::class.java)
        dataMap.put(main_toolbar.id, ToolbarActivity::class.java)
        dataMap.put(main_drawer.id, DrawerActivity::class.java)
        dataMap.put(main_NavigationView.id, NavigationViewActivity::class.java)
        dataMap.put(main_FloatActionButton.id, FloatActionButtonActivity::class.java)
        dataMap.put(main_MaterialCard.id, MaterialCardActivity::class.java)
        dataMap.put(main_Swiperefresh.id, SwiperefreshActivity::class.java)
        dataMap.put(main_CollapsingToolbar.id, CollapsingToolbarActivity::class.java)
        dataMap.put(main_ViewMode.id, ViewModeActivity::class.java)
        dataMap.put(main_Room.id, RoomActivity::class.java)
        dataMap.put(main_call_phone.id, CallPhoneActivity::class.java)
        dataMap.put(main_TabLayout.id, TabLayoutActivity::class.java)
        dataMap.put(main_ViewPager2.id, ViewPager2Activity::class.java)
        dataMap.put(main_Coordinator.id, CoordinatorActivity::class.java)
        dataMap.put(main_ViewScroll.id, ViewScrollActivity::class.java)
        dataMap.put(main_Animator.id, AnimationBasicActivity::class.java)
        dataMap.put(main_ViewEvent.id, ViewEventActivity::class.java)
        dataMap.put(main_CustomView.id, CustomViewNavigationActivity::class.java)

//        main_btn1.setOnClickListener {
//            var intent = Intent(this, FirstActivity::class.java)
//            startActivity(intent)
//        }
//        main_menu_btn.setOnClickListener {
//            startActivity(Intent(this, MenuActivity::class.java))
//        }
    }
//    data class DataState(val title:String, val startActivity: Class<out BasicActivity>)
    fun onClick(view:View){
//        if (view.id == main_f_new_title.id){
//            NewsContentActivity.actionStart(this, "新闻", "高温37摄氏度")
//            return
//        }
        if (dataMap.containsKey(view.id)) {
            val intent = Intent(this, dataMap[view.id])
            startActivity(intent)
        }
    }

}
