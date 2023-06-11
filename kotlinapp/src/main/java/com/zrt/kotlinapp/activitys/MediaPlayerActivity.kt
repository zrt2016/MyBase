package com.zrt.kotlinapp.activitys

import android.media.MediaPlayer
import android.os.Bundle
import com.zrt.kotlinapp.BasicActivity
import com.zrt.kotlinapp.R
import kotlinx.android.synthetic.main.activity_media_player.*

/**
 * MediaPlayer类中常用控制方法
 * setDataSource()	设置要播放的音频文件的位置
 * prepare()	在开始播放之前调用，以完成准备工作
 * prepareAsync() 异步加载媒体资源，与setOnPreparedListener陪护使用
 * start()	开始或继续播放音频
 * pause()	暂停播放音频
 * reset()	将MediaPlayer对象重置到刚刚创建的状态
 * seekTo()	从指定的位置开始播放音频
 * stop()	停止播放音频。调用后的MediaPlayer对象无法再播放音频
 * release()	释放与MediaPlayer对象相关的资源
 * isPlaying()	判断当前MediaPlayer是否正在播放音频
 * getDuration()	获取载入的音频文件的时长
 */
class MediaPlayerActivity : BasicActivity() {
    private val mediaPlayer = MediaPlayer()
    private lateinit var player:MediaPlayer
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initMediaPlayer()
    }

    override fun getLayoutResID(): Int = R.layout.activity_media_player

    override fun initData() {
        a_mp_play.setOnClickListener {
            if (!mediaPlayer.isPlaying){
                mediaPlayer.start() // 开始播放
            }
        }
        a_mp_pause.setOnClickListener {
            if (mediaPlayer.isPlaying){
                mediaPlayer.pause()
            }
        }
        a_mp_stop.setOnClickListener {
            if (mediaPlayer.isPlaying){
                mediaPlayer.reset()
                initMediaPlayer()
            }
        }
//        player = MediaPlayer.create(getApplicationContext(), R.raw.pishi_remind)
        player = MediaPlayer()
//        initRemindMedia()
        a_mp_play_error.setOnClickListener{
//            if (player == null){
//                player = MediaPlayer.create(getApplicationContext(), R.raw.pishi_remind)
//            }else {
//                player.release()
//                player = MediaPlayer.create(getApplicationContext(), R.raw.pishi_remind)
//            }

            if (!player.isPlaying()) {
                initRemindMedia()
                player.setOnPreparedListener(object : MediaPlayer.OnPreparedListener {
                    override fun onPrepared(mp: MediaPlayer?) {
                        // 待媒体资源加载完成后，开起播放
                        mp?.start()
                    }
                })
//                player.start()
                // 监听音频播放完的代码，实现音频的自动循环播放
                player.setOnCompletionListener(MediaPlayer.OnCompletionListener {
                    it.start()
                    it.isLooping = true
                })
            }

        }
        a_mp_stop_error.setOnClickListener{
            player.isLooping = false
            player.stop()
            player.reset()

        }
    }
    val remind by lazy {
        resources.openRawResourceFd(R.raw.pishi_remind)
    }
    fun initRemindMedia(){
        player.reset() // 解决setDataSource()时， 报IllegalStateException异常的问题
        player.setDataSource(remind.fileDescriptor, remind.startOffset, remind.length)
//        player.prepare()
        //            player.prepare();
        player.prepareAsync() // 异步加载媒体资源
    }

    fun initMediaPlayer(){
        // 使用assets读取assets目录下的资源文件
        val assetManager = assets
        // 获取存放在assets中的music.mp3文件位置
        val openFd = assetManager.openFd("music.mp3")
        mediaPlayer.setDataSource(openFd.fileDescriptor, openFd.startOffset, openFd.length)
        mediaPlayer.prepare()
    }
    fun closeMediaPlayer(){
        mediaPlayer.stop()
        mediaPlayer.release()
    }

    override fun onDestroy() {
        super.onDestroy()
        closeMediaPlayer()
        player.isLooping = false
        player.stop()
        player.reset()
        player.release()
    }
}
/**
 * MediaPlayer其实是一个封装的很好的音频、视频流媒体操作类，如果查看其源码，会发现其内部是调用的native方法，所以它其实是有C++实现的。

　　既然是一个流媒体操作类，那么必然涉及到，播放、暂停、停止等操作，实际上MediaPlayer也为我们提供了相应的方法来直接操作流媒体。

void statr()：开始或恢复播放。
void stop()：停止播放。
void pause()：暂停播放。　　
　　通过上面三个方法，只要设定好流媒体数据源，即可在应用中播放流媒体资源，为了更好的操作流媒体，MediaPlayer还为我们提供了一些其他的方法，这里列出一些常用的，详细内容参阅官方文档。

int getDuration()：获取流媒体的总播放时长，单位是毫秒。
int getCurrentPosition()：获取当前流媒体的播放的位置，单位是毫秒。
void seekTo(int msec)：设置当 前MediaPlayer的播放位置，单位是毫秒。
void setLooping(boolean looping)：设置是否循环播放。
boolean isLooping()：判断是否循环播放。
boolean  isPlaying()：判断是否正在播放。
void prepare()：同步的方式装载流媒体文件。
void prepareAsync()：异步的方式装载流媒体文件。
void release ()：回收流媒体资源。
void setAudioStreamType(int streamtype)：设置播放流媒体类型。
void setWakeMode(Context context, int mode)：设置CPU唤醒的状态。
setNextMediaPlayer(MediaPlayer next)：设置当前流媒体播放完毕，下一个播放的MediaPlayer。
　　大部分方法的看方法名就可以理解，但是有几个方法需要单独说明一下。

　　在使用MediaPlayer播放一段流媒体的时候，需要使用prepare()或prepareAsync()方法把流媒体装载进MediaPlayer，才可以调用start()方法播放流媒体。　　　　　　　　　　　　　　　　　

　　setAudioStreamType()方法用于指定播放流媒体的类型，它传递的是一个int类型的数据，均以常量定义在AudioManager类中， 一般我们播放音频文件，设置为AudioManager.STREAM_MUSIC即可。

　　除了上面介绍的一些方法外，MediaPlayer还提供了一些事件的回调函数，这里介绍几个常用的：

setOnCompletionListener(MediaPlayer.OnCompletionListener listener)：当流媒体播放完毕的时候回调。
setOnErrorListener(MediaPlayer.OnErrorListener listener)：当播放中发生错误的时候回调。
setOnPreparedListener(MediaPlayer.OnPreparedListener listener)：当装载流媒体完毕的时候回调。
setOnSeekCompleteListener(MediaPlayer.OnSeekCompleteListener listener)：当使用seekTo()设置播放位置的时候回调。
 *
 * */