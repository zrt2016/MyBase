package com.zrt.kotlinapp.activitys

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.zrt.kotlinapp.BasicActivity
import com.zrt.kotlinapp.R
import kotlinx.android.synthetic.main.activity_video_view.*

/**
 * VideoView中常用方法
 * setVideoPath()	设置要播放的视频文件的位置
 * start()	开始或继续播放视频
 * pause()	暂停播放视频
 * resume()	将视频从头开始播放
 * seekTo()	从指定的位置开始播放视频
 * isPlaying()	判断当前是否正在播放视频
 * getDuration()	获取载入的视频文件的时长
 *
 * 读取res-raw目录下的视频文件播放
 */
class VideoViewActivity : BasicActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun getLayoutResID(): Int = R.layout.activity_video_view

    override fun initData() {
        // 将raw目录下的video.mp4文件解析成一个Uri对象
        val parse = Uri.parse("android.resource://$packageName/${R.raw.video}")
        a_v_videoView.setVideoURI(parse)
        a_v_play.setOnClickListener {
            if (!a_v_videoView.isPlaying){
                a_v_videoView.start()
            }
        }
        a_v_pause.setOnClickListener {
            if (a_v_videoView.isPlaying){
                a_v_videoView.pause()
            }
        }
        a_v_replay.setOnClickListener {
            if (a_v_videoView.isPlaying){
                a_v_videoView.resume()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        a_v_videoView.suspend()
    }
}