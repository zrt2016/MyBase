package com.zrt.kotlinapp.notification

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.widget.RemoteViews
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.NotificationCompat
import androidx.core.app.RemoteInput
import com.zrt.kotlinapp.BasicActivity
import com.zrt.kotlinapp.R
import com.zrt.kotlinapp.utils.ToastUtils
import kotlinx.android.synthetic.main.activity_notification.*

/**
 * 关键类：
 *  NotificationManager 通知管理器，用来发起、更新、删除通知
    NotificationChannel 通知渠道，8.0及以上配置渠道以及优先级
    NotificationCompat.Builder 通知构造器，用来配置通知的布局显示以及操作相关
 * 通知的使用
 *    1、获取通知管理器：NotificationManager
 *    2、创建通知渠道：NotificationChannel
 *    3、把通知渠道加入管理器中：createNotificationChannel
 *    4、发送通知:NotificationCompat.Builder
 * NotificationCompat.Builder的使用:
 *  setContentTitle 标题
    setContentText 内容
    setSmallIcon 小图标
    setLargeIcon 大图标
    setPriority 优先级or重要性（7.0和8.0的方式不同）
    setContentIntent 点击意图
    setAutoCancel 是否自动取消
    notify 发起通知
 * setVisibility 屏幕可见性，锁屏时，显示icon和标题，内容隐藏，解锁查看全部
 *      NotificationCompat.VISIBILITY_PUBLIC：任何情况都会显示通知
 *      NotificationCompat.VISIBILITY_PRIVATE:只有在没有锁屏时会显示通知
 *      NotificationCompat.VISIBILITY_SECRTE：在pin、password等安全锁和没有锁屏的情况下显示通知
 *
 * 通知的不同权限等级：
 *      setPriority(NotificationCompat.PRIORITY_DEFAULT) // 优先级，7.0
 *      IMPORTANCE_HIGH、PRIORITY_HIGH:发出声音，并显示为提醒通知
 *      IMPORTANCE_DEFAULT、PRIORITY_DEFAULT：发出声音
 *      IMPORTANCE_LOW、PRIORITY_LOW：没有声音
 *      IMPORTANCE_MIN、PRIORITY_MIN：没有声音且不显示在状态栏
 * 给通知添加点击效果：
 *    1、pendingIntent可用于启动Activity、Service以及发送广播等
 *    2、pendingIntent可以根据需求选择使用getActivity()、getBroadcast()、getService()方法
 *    这个几个方法的参数都相同：
 *      参数一：context
 *      参数二：requestCode，一般给0即可
 *      参数三：intent对象，构建pendingIntent的意图
 *      参数四：用于确定pendingIntent的行为，通常传0
 *          一共有四种行为： FLAG_ONE_SHOT,FLAG_NO_CREATE, FLAG_CANCEL_CURRENT, FLAG_UPDATE_CURRENT
 *  关闭通知的几种方法：
 *      1、setAutoCancel(true)是否自动消失（点击）
 *      2、mManager.cancel(mNormalNotificationId)、cancelAll、setTimeoutAfter()
 *
 *  Android 12.0，API 31
 *  自定义通知，提供通知模板。
 *  更改了完全自定义通知的外观和行为。
 */
class NotificationActivity : BasicActivity() {
    companion object{
        val channelID = "normal"
        val noticeID = 1
        val noticeIDStyle = 2
        val noticeIDStylePicture = 3

        val channelID_HIGH = "normal_high"
        val noticeID_HIGH = 11

        val channelID_Progress = "normal_progress"
        val noticeID_Progress = 21

        val channelID_Custom = "normal_custom"
        val noticeID_Custom = 31

        var mIsStop = false
        // 横幅通知
        val channelID_Banner = "banner"
        val noticeID_Binner = 51

        // 常驻通知
        val channelID_Permanent = "permanent"
        val noticeID_Permanent = 61

        val channelID_Custom2 = "normal_custom2"
        val noticeID_Custom2 = 71
        val channelID_Custom3 = "normal_custom3"
        val noticeID_Custom3 = 72

        val channelID_suspension = "normal_suspension"
        val noticeID_suspension = 82
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ActivityResultContracts.StartActivityForResult()
    }

    override fun getLayoutResID(): Int = R.layout.activity_notification

    override fun initData() {
        // 获取NotificationManager，对通知进行管理
        val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        // 使用NotificationChannel构建一个通知渠道
        // 并调用NotificationManager的createNotificationChannel完成创建
        // 由于NotificationChannel和createNotificationChannel是在8.0后添加的，使用时需要进行版本判断
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            /**
             * 参数一：channelID需要与NotificationCompat中的channelID保持一致
             * 参数二：建立的渠道名称
             * 参数三：通知的等级分别有四种，由高到低：IMPORTANCE_HIGH、IMPORTANCE_DEFAULT、IMPORTANCE_LOW、IMPORTANCE_MIN
             * 通知等级创建后，不予修改
             */
            createNoticeChannel(manager, channelID, "Normal", NotificationManager.IMPORTANCE_DEFAULT){}
            // 创建一个高登记的通知渠道
            createNoticeChannel(manager, channelID_HIGH, "Normal_HIGH", NotificationManager.IMPORTANCE_HIGH){}
            // 进度条通知
            createNoticeChannel(manager, channelID_Progress, "Normal_Progress", NotificationManager.IMPORTANCE_DEFAULT){}
            // 自定义通知
            createNoticeChannel(manager, channelID_Custom, "Normal_Custom", NotificationManager.IMPORTANCE_DEFAULT){}
        }
        // 关闭所有通知
        a_n_cancelALL.setOnClickListener {
            manager.cancelAll()
        }
        // IMPORTANCE_DEFAULT等级的通知
        createDefaultNotice(manager)
        // IMPORTANCE_HIGH等级的通知
        createHighNotice(manager)
        // 进度条通知
        createNotificeForProgress(manager)
        // 自定义通知
        createNotificeCustom(manager)
        // 创建一个回复消息的通知
        createNotificeReply(manager)
        // 创建横幅通知
        createNoticeBanner(manager)
        // 常驻通知
        createNoticeChangzhu(manager)
        // 自定义通知2
        createNotificeCustom2(manager)
        //自定义通知大小 可展开
        createNotificeCustom3(manager)
        // 悬挂式通知
        createSuspension(manager)
    }

    fun createDefaultNotice(manager: NotificationManager){
        a_n_sendNotice.setOnClickListener {
            val intent = Intent(this, NotificationActivity::class.java)
            val pendingIntent = PendingIntent.getActivity(this, 0, intent, 0)

            /**
             * NotificationChannel中的channelID 必须与 NotificationCompat中的channelID保持一致
             */
            val build = NotificationCompat.Builder(this, channelID)
                    .setContentTitle("This is Title") // 指定通知的标题内容
                    .setContentText("This is Content") // 指定通知的正文内容
                    .setSmallIcon(R.drawable.small_icon) // 设置通知的小图标
                    .setLargeIcon(BitmapFactory.decodeResource(resources, R.drawable.large_icon)) // 设置通知的大图标
                    .setContentIntent(pendingIntent) // 构建一个延迟的行为意图，即增加点击效果
                    .setAutoCancel(true) // true,通知会在你点击后，自动取消
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT) // 优先级，7.0
                    .build()
            manager.notify(noticeID, build)
        }
        a_n_cancelNotice.setOnClickListener {
            // 取消通知，参数noticeID为你创建的notice时添加的id，即：notify(noticeID, build)中的noticeID
            manager.cancel(noticeID)
        }
        /**
         * 使用setStyle
         */
        a_n_sendNotice_style.setOnClickListener {
            // 发送长文字内容的通知，并完整显示内容
            val build = NotificationCompat.Builder(this, channelID)
                    .setContentTitle("This is Title Style") // 指定通知的标题内容
                    // 使用setStyle代替serContentText
                    // 创建一个NotificationCompat.BigTextStyle()对象封装长文字信息
                    .setStyle(NotificationCompat.BigTextStyle()
                            .bigText("This is Content hdadaskdd djaskdalsdl dsjkadalsdkasdhajs dalsda dasdakd ladaj skdasdjaks jalsdaj dkaj dla")) // 指定通知的正文内容
                    // 使用NotificationCompat.BigPictureStyle()设置大图片展示
                    .setStyle(NotificationCompat.BigPictureStyle()
                            .bigPicture(BitmapFactory.decodeResource(resources, R.drawable.big_image)))
                    .setSmallIcon(R.drawable.small_icon) // 设置通知的小图标
                    .setLargeIcon(BitmapFactory.decodeResource(resources, R.drawable.large_icon)) // 设置通知的大图标
                    .setAutoCancel(true) // true,通知会在你点击后，自动取消
                    .build()
            manager.notify(noticeIDStyle, build)
        }
        a_n_cancelNotice_style.setOnClickListener {
            manager.cancel(noticeIDStyle)
        }
        a_n_sendNotice_style_p.setOnClickListener {
            // 展示大图片的通知
            val build = NotificationCompat.Builder(this, channelID)
                    .setContentTitle("This is Title Picture") // 指定通知的标题内容
                    // 使用NotificationCompat.BigPictureStyle()设置大图片展示
                    .setStyle(NotificationCompat.BigPictureStyle()
                            .bigPicture(BitmapFactory.decodeResource(resources, R.drawable.big_image)))
                    .setSmallIcon(R.drawable.small_icon) // 设置通知的小图标
                    .setLargeIcon(BitmapFactory.decodeResource(resources, R.drawable.large_icon)) // 设置通知的大图标
                    .setAutoCancel(true) // true,通知会在你点击后，自动取消
                    .build()
            manager.notify(noticeIDStylePicture, build)
        }
        a_n_cancelNotice_style_p.setOnClickListener {
            manager.cancel(noticeIDStylePicture)
        }
    }

    /**
     * setNumber 桌面通知数量
     * addAction 通知上的操作
     * setCategory 通知类别，"勿扰模式"时系统会决定要不要显示你的通知
     * setVisibility 屏幕可见性，锁屏时，显示icon和标题，内容隐藏，解锁查看全部
     *
     * Category各种参数：
     *  Notification.CATEGORY_CALL //呼入（语音或视频）或类似的同步通信请求。
     *  Notification.CATEGORY_MESSAGE //直接消息（短信、即时消息等）。
     *  Notification.CATEGORY_EMAIL //异步批量消息（电子邮件）。
     *  Notification.CATEGORY_EVENT //日历事件。
     *  Notification.CATEGORY_PROMO //促销或广告。
     *  Notification.CATEGORY_ALARM //闹钟或定时器。
     *  Notification.CATEGORY_PROGRESS //长时间后台操作的进展。
     *  Notification.CATEGORY_SOCIAL //社交网络或共享更新。
     *  Notification.CATEGORY_ERROR //后台操作或者身份验证状态出错。
     *  Notification.CATEGORY_TRANSPORT //回放媒体传输控制。
     *  Notification.CATEGORY_SYSTEM //系统或者设备状态更新，预留给系统使用。
     *  Notification.CATEGORY_SERVICE //运行后台服务的指示。
     *  Notification.CATEGORY_RECOMMENDATION //针对某一事物的具体及时的建议。
     *  Notification.CATEGORY_STATUS //关于设备或者上下文状态的正在进行的信息。
     *  Notification.CATEGORY_REMINDER //用户预定提醒。
     *
     */
    fun createHighNotice(manager: NotificationManager){
        a_n_sendNotice_high.setOnClickListener {
            val intent = Intent(this, NotificationActivity::class.java)
            val pendingIntent = PendingIntent.getActivity(this, 0, intent, 0)
            val build = NotificationCompat.Builder(this, channelID_HIGH)
                    .setContentTitle("This is Title HIGH") // 指定通知的标题内容
                    .setContentText("This is Content HIGH") // 指定通知的正文内容
                    .setSmallIcon(R.drawable.small_icon) // 设置通知的小图标
                    .setLargeIcon(BitmapFactory.decodeResource(resources, R.drawable.large_icon)) // 设置通知的大图标
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true) // true,通知会在你点击后，自动取消
                    .setPriority(NotificationCompat.PRIORITY_HIGH) // 优先级，7.0
                    .addAction(R.mipmap.apple_pic, "苹果", pendingIntent)
                    .setCategory(NotificationCompat.CATEGORY_MESSAGE)// 通知类别，"勿扰模式"时系统会决定要不要显示你的通知
                    .setVisibility(NotificationCompat.VISIBILITY_PRIVATE) // 屏幕可见性，锁屏时，显示icon和标题，内容隐藏
                    .build()
            manager.notify(noticeID_HIGH, build)
        }
        a_n_cancelNotice_high.setOnClickListener {
            manager.cancel(noticeID_HIGH)
        }
    }

    /**
     * 进度条通知
     */
    fun createNotificeForProgress(manager: NotificationManager) {
        var build: NotificationCompat.Builder? = null
        val progressMax = 100
        var progressCurrent = 30
        a_n_sendNotice_progress.setOnClickListener {

            build = NotificationCompat.Builder(this, channelID_Progress)
                    .setContentTitle("Progress 通知") // 指定通知的标题内容
                    .setContentText("Download $progressCurrent%") // 指定通知的正文内容
                    .setSmallIcon(R.drawable.small_icon) // 设置通知的小图标
                    .setLargeIcon(BitmapFactory.decodeResource(resources, R.drawable.large_icon)) // 设置通知的大图标
                    .setAutoCancel(true) // true,通知会在你点击后，自动取消
                    // 第3个参数indeterminate，false表示确定的进度，
                    // 比如100，true表示不确定的进度，会一直显示进度动画，直到更新状态下载完成，或删除通知
                    .setProgress(progressMax, progressCurrent, false);
//                    .setPriority(NotificationCompat.PRIORITY_HIGH) // 优先级，7.0
//                    .setCategory(NotificationCompat.CATEGORY_MESSAGE)// 通知类别，"勿扰模式"时系统会决定要不要显示你的通知
//                    .setVisibility(NotificationCompat.VISIBILITY_PRIVATE) // 屏幕可见性，锁屏时，显示icon和标题，内容隐藏
            build?.let { builder ->
                manager.notify(noticeID_Progress, builder.build())
            }
        }
        // 更新进度条
        a_n_sendNotice_progress_update.setOnClickListener {
            if (progressCurrent >= 100) {
                manager.cancel(noticeID_Progress)
                ToastUtils.show(this, "下载完成");
            }
            progressCurrent = progressCurrent+10
//            if (::build) {
            build?.let { builder ->
                builder.setContentText("Download ${progressCurrent}%")
                    .setProgress(progressMax, progressCurrent, false)
                manager.notify(noticeID_Progress, builder.build())
            }
//            }
        }
        a_n_cancelNotice_progress.setOnClickListener {
            manager.cancel(noticeID_Progress)
        }
    }

    private fun createNotificeCustom(manager: NotificationManager) {
        // 适配12.0及以上
//        var mFlag = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
//            PendingIntent.FLAG_IMMUTABLE
//        } else {
//            PendingIntent.FLAG_UPDATE_CURRENT
//        }
        var mFlag = PendingIntent.FLAG_UPDATE_CURRENT
        a_n_sendNotice_custom.setOnClickListener {
            // 添加自定义通知view
            val views = RemoteViews(packageName, R.layout.layout_notification_custom)
            // 添加暂停继续事件
            val intentStop = Intent(MusicPlayerReceiver.actionStop)
            val piStop = PendingIntent.getBroadcast(this@NotificationActivity, 0, intentStop, mFlag)
            views.setOnClickPendingIntent(R.id.btn_stop, piStop)
            // 添加完成事件
            val intentDone = Intent(MusicPlayerReceiver.actionDone)
            val piDone = PendingIntent.getBroadcast(this@NotificationActivity, 0, intentDone, mFlag)
            views.setOnClickPendingIntent(R.id.btn_done, piDone)
            // 创建Builder
            mBuilder = NotificationCompat.Builder(this@NotificationActivity, channelID_Custom)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setLargeIcon(BitmapFactory.decodeResource(resources, R.drawable.large_icon))
                    .setAutoCancel(true)
                    .setCustomContentView(views)
                    .setCustomBigContentView(views)// 设置自定义通知view

            // 发起通知
            mBuilder?.let {
                manager.notify(noticeID_Custom, it.build())
            }
        }
        a_n_cancelNotice_custom.setOnClickListener {
            manager.cancel(noticeID_Custom)
        }
    }
    var mBuilder: NotificationCompat.Builder? = null
    fun updateCustomNotice(){
//        ToastUtils.show(this, "Stop Music")
        val mManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val views = RemoteViews(packageName, R.layout.layout_notification_custom)
        val intentUpdate = Intent(MusicPlayerReceiver.actionStop)
        val pendingIntentUpdate = PendingIntent.getBroadcast(this, 0, intentUpdate, PendingIntent.FLAG_UPDATE_CURRENT)
        views.setOnClickPendingIntent(R.id.btn_stop, pendingIntentUpdate)
        // 根据状态更新UI
        if (mIsStop) {
            views.setTextViewText(R.id.tv_status, "安河桥-停止播放")
            views.setTextViewText(R.id.btn_stop, "继续")
//                mBinding.mbUpdateCustom.text = "继续"
        } else {
            views.setTextViewText(R.id.tv_status, "安河桥-正在播放")
            views.setTextViewText(R.id.btn_stop, "暂停")
//                mBinding.mbUpdateCustom.text = "暂停"
        }
        mBuilder?.let {
            it.setCustomContentView(views).setCustomBigContentView(views)
//            // 重新发起通知更新UI，注意：必须得是同一个通知id，即mCustomNotificationId
            mManager.notify(noticeID_Custom, it.build())
        }

    }

    override fun onResume() {
        super.onResume()
        MusicPlayerReceiver.registerReceiver(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        MusicPlayerReceiver.unregisterReceiver(this)
    }
    class MusicPlayerReceiver: BroadcastReceiver(){
        companion object{
            val actionDone = "com.zrt.kotlinapp.notification.MUSIC_PLAYER_DONE"
            val actionStop = "com.zrt.kotlinapp.notification.MUSIC_PLAYER_STOP"
            var mInstances: MusicPlayerReceiver? = null
            fun newInstance() : MusicPlayerReceiver?{
                if (mInstances == null){
                    mInstances = MusicPlayerReceiver()
                }
                return mInstances
            }
            fun registerReceiver(context: Context)
                    : Intent? {
                newInstance()
                val intentFilter = IntentFilter()
                intentFilter.addAction(MusicPlayerReceiver.actionStop)
                intentFilter.addAction(MusicPlayerReceiver.actionDone)
                return context.registerReceiver(newInstance(), intentFilter)
            }
            fun unregisterReceiver(context: Context){
                context.unregisterReceiver(newInstance())
            }
        }
        override fun onReceive(context: Context, intent: Intent) {
            if (intent.action == actionStop) {
//            // 改变状态
                mIsStop = !mIsStop
                val activity = context as? NotificationActivity
                activity?.updateCustomNotice()
//                Toast.makeText(context, "完成", Toast.LENGTH_SHORT).show()
            } else if (intent.action == actionDone) {
                Toast.makeText(context, "完成", Toast.LENGTH_SHORT).show()
            }
        }
    }

    /**
     * 低版本会出现，点击回复报错问题。
     * 高版本正常
     */
    var managerReply: NotificationManager? = null
    // 定义一个回复消息的通知
    private fun createNotificeReply(manager: NotificationManager) {
        a_n_sendNotice_reply.setOnClickListener {

            val remoteInput = RemoteInput.Builder(resultKey_Reply).setLabel("快速回复").build()
            //构建回复pendingIntent
//            val replyIntent = Intent(ReplyMessageReceiver.action)
            val replyIntent = Intent(this@NotificationActivity, ReplyMessageReceiver::class.java)
            replyIntent.setAction(ReplyMessageReceiver.action)
//            replyIntent.setPackage(packageName)
//            // 注：如果报错Cannot send pending intent:  android.content.IntentSender$SendIntentException
//            // 则修改requestCode唯一   (暂未生效)
//            val requestCode = 0x20000001
            val pendingIntent = PendingIntent.getBroadcast(this, 0, replyIntent, PendingIntent.FLAG_ONE_SHOT)
            //点击通知的发送按钮
            val action = NotificationCompat.Action.Builder(R.drawable.large_icon, "回复", pendingIntent)
                    .addRemoteInput(remoteInput).build()
            //构建通知
            managerReply = getNoticeManage(this)
            createNoticeChannel(managerReply, channelID_Reply, "回复消息1", NotificationManager.IMPORTANCE_HIGH){}
            var replyNotification = NotificationCompat.Builder(this, channelID_Reply)
                    .setSmallIcon(R.drawable.small_icon) // 设置通知的小图标
                    .setLargeIcon(BitmapFactory.decodeResource(resources, R.drawable.large_icon))
                    .setContentTitle("1008666")//标题
                    .setContentText("你的账号已欠费2000元！")//内容
                    .addAction(action)
                    .build()
            managerReply?.notify(noticeID_Reply2, replyNotification)
        }
        a_n_cancelNotice_reply.setOnClickListener {
            managerReply?.cancel(noticeID_Reply2)
        }
    }
    private lateinit var bannerNotification: NotificationCompat
    fun createNoticeBanner(manager: NotificationManager){
        a_n_sendNotice_banner.setOnClickListener {
            if (openBannerNotification(manager)) {
                /**
                 * 初始化横幅通知
                 */
                    //构建通知
                createNoticeChannel(manager, channelID_Banner, "banner"){
                    description = "提醒式通知"//渠道描述
                    enableLights(true)//开启闪光灯
                    lightColor = Color.BLUE//设置闪光灯颜色
                    enableVibration(true)//开启震动
                    vibrationPattern = longArrayOf(0, 1000, 500, 1000)//震动模式
                    setSound(null, null)//没有提示音
                }
                val build = NotificationCompat.Builder(this, channelID_Banner)
                        .setSmallIcon(R.mipmap.ic_launcher)//小图标（显示在状态栏）
                        .setLargeIcon(BitmapFactory.decodeResource(resources, R.mipmap.ic_launcher))//大图标（显示在通知上）
                        .setContentTitle("落魄Android在线炒粉")//标题
                        .setContentText("不要9块9，不要6块9，只要3块9。")//内容
                        .setWhen(System.currentTimeMillis())//通知显示时间
                        .setAutoCancel(true)//设置自动取消
                        .build()
                manager.notify(noticeID_Binner, build)
            }
        }
        a_n_cancelNotice_banner.setOnClickListener {
            manager.cancel(noticeID_Binner)
        }

    }
    /**
     * registerForActivityResult需要在1.3.1及以上的版本才能使用
     * implementation 'androidx.appcompat:appcompat:1.3.1'
     */
    //开启横幅通知返回
    private val bannerLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
                if (it.resultCode == RESULT_OK) {
                    Log.d("TAG", "返回结果")
                }
            }
    /**
     * 是否开启横幅通知
     */
    public fun openBannerNotification(manager: NotificationManager)
            = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val bannerImportance = manager.getNotificationChannel(channelID_Banner)?.importance
        if (bannerImportance == NotificationManager.IMPORTANCE_DEFAULT) {
            val intent = Intent(Settings.ACTION_CHANNEL_NOTIFICATION_SETTINGS)
                    .putExtra(Settings.EXTRA_APP_PACKAGE, packageName)
                    .putExtra(Settings.EXTRA_CHANNEL_ID, channelID_Banner)
            bannerLauncher.launch(intent)
            false
        } else true
    } else true

    /**
     * 添加常驻通知
     */
    private fun createNoticeChangzhu(manager: NotificationManager) {
        a_n_sendNotice_changzhu.setOnClickListener {
            //构建回复pendingIntent
            val permanentIntent = Intent(this, NotificationActivity::class.java)
            val pendingIntent =
                    PendingIntent.getActivity(this, 0, permanentIntent, PendingIntent.FLAG_UPDATE_CURRENT)
            //构建通知
            var notice = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                createNoticeChannel(manager, channelID_Permanent, "常驻通知")
                NotificationCompat.Builder(this, channelID_Permanent)
            } else {
                NotificationCompat.Builder(this)
            }.apply {
                setSmallIcon(R.mipmap.ic_launcher)//小图标（显示在状态栏）
                setLargeIcon(BitmapFactory.decodeResource(resources, R.mipmap.ic_launcher))//大图标（显示在通知上）
                setContentTitle("你在努力些什么？常驻")//标题
                setContentText("搞钱！搞钱！还是搞钱！")//内容
                setWhen(System.currentTimeMillis())//通知显示时间
                setContentIntent(pendingIntent)
            }.build()
            // 修改为常驻
            notice.flags = Notification.FLAG_ONGOING_EVENT
            manager.notify(noticeID_Permanent, notice)
        }
        a_n_cancelNotice_changzhu.setOnClickListener {
            manager.cancel(noticeID_Permanent)
        }
    }

    private fun createNotificeCustom2(manager: NotificationManager) {
        var mFlag = PendingIntent.FLAG_UPDATE_CURRENT
        a_n_sendNotice_custom2.setOnClickListener {
            // 添加自定义通知view
            val views = RemoteViews(packageName, R.layout.layout_notification_custom2)
            // 创建Builder
            createNoticeChannel(manager, channelID_Custom2, "Custom2")
            mBuilder = NotificationCompat.Builder(this@NotificationActivity, channelID_Custom2)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setLargeIcon(BitmapFactory.decodeResource(resources, R.drawable.large_icon))
                    .setAutoCancel(true)
                    .setCustomContentView(views)
                    .setCustomBigContentView(views)// 设置自定义通知view
                    .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                    .setOnlyAlertOnce(true)
                    .setOngoing(true)
            val build = mBuilder?.build()
            // 修改为常驻
            build?.flags = Notification.FLAG_ONGOING_EVENT
            // 发起通知
            manager.notify(noticeID_Custom2, build)

        }
        a_n_cancelNotice_custom2.setOnClickListener {
            manager.cancel(noticeID_Custom2)
        }
    }

    /**
     * 通知布局视图布局的高度上限为 64 dp，展开后的视图布局的高度上限为 256 dp，
     * createNotificeCustom2只设置了小的通知，那么如果要展开一个大一点的通知，最好是能够滑动通知的时候有大小变化。
     */
    private fun createNotificeCustom3(manager: NotificationManager) {
        var mFlag = PendingIntent.FLAG_UPDATE_CURRENT
        a_n_sendNotice_custom2.setOnClickListener {
            // 添加自定义通知view
            val views = RemoteViews(packageName, R.layout.layout_notification_custom2)
            val views_big = RemoteViews(packageName, R.layout.layout_notification_custom2_big)
            // 创建Builder
            createNoticeChannel(manager, channelID_Custom3, "Custom3")
            mBuilder = NotificationCompat.Builder(this@NotificationActivity, channelID_Custom3)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setLargeIcon(BitmapFactory.decodeResource(resources, R.drawable.large_icon))
                    .setAutoCancel(true)
                    .setCustomContentView(views)
                    .setCustomBigContentView(views_big)// 设置展开后的view
                    .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                    .setOnlyAlertOnce(true)
                    .setOngoing(true)
            val build = mBuilder?.build()
            // 修改为常驻
            build?.flags = Notification.FLAG_ONGOING_EVENT
            // 发起通知
            manager.notify(noticeID_Custom3, build)

        }
        a_n_cancelNotice_custom2.setOnClickListener {
            manager.cancel(noticeID_Custom3)
        }
    }

    private fun createSuspension(manager: NotificationManager) {
        a_n_sendNotice_suspension.setOnClickListener {
            var handIntent = Intent()
            val activity = PendingIntent.getActivity(this, 0, handIntent, PendingIntent.FLAG_CANCEL_CURRENT)

            val intent = Intent(this, NotificationActivity::class.java)
            val pendingIntent = PendingIntent.getActivity(this, 0, intent, 0)
            val build = NotificationCompat.Builder(this, channelID_suspension)
                    .setContentTitle("悬挂式通知") // 指定通知的标题内容
                    .setContentText("This is Content") // 指定通知的正文内容
                    .setSmallIcon(R.drawable.small_icon) // 设置通知的小图标
                    .setLargeIcon(BitmapFactory.decodeResource(resources, R.drawable.large_icon)) // 设置通知的大图标
                    .setContentIntent(pendingIntent) // 构建一个延迟的行为意图，即增加点击效果
                    .setAutoCancel(true) // true,通知会在你点击后，自动取消
                    .setFullScreenIntent(activity,true)
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT) // 优先级，7.0
                    .build()
            manager.notify(noticeID_suspension, build)
        }
        a_n_cancelNotice_suspension.setOnClickListener {
            manager.cancel(noticeID_suspension)
        }
    }
}

