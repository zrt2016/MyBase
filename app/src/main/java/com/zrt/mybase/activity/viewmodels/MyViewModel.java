package com.zrt.mybase.activity.viewmodels;


import androidx.lifecycle.ViewModel;

import java.util.Timer;
import java.util.TimerTask;

/**
 * @author：Zrt
 * @date: 2022/2/16
 * 注意：既然ViewModel的销毁是由系统来判断和执行的，那么系统是如何判断的呢？
 *      是根据Context引用。因此，我们在使用ViewModel的时候，千万不能从外面传入Activity，
 *      Fragment或者View之类的含有Context引用的东西，否则系统会认为该ViewModel还在使用中，
 *      从而无法被系统销毁回收，导致内存泄漏的发生。
 * 如果希望ViewModel使用AndroidViewModel
 */
public class MyViewModel extends ViewModel {
    private String TAG = this.getClass().getName();

    /**
     *  由于屏幕旋转导致的Activity重建，该方法不会被调用
     *  只有ViewModel已经没有任何Activity与之有关联，系统则会调用该方法，你可以在此清理资源
     */
    @Override
    protected void onCleared() {
        super.onCleared();
        if (timer != null) timer.cancel();
    }

    /**
     * 写一个定时计时器，每隔1秒调用接口，通知UI更新
     */
    private Timer timer;
    private int currentSecond;
    public void startMing(){
        if (timer == null){
            currentSecond = 0;
            timer = new Timer();
            TimerTask timerTask = new TimerTask() {
                @Override
                public void run() {
                    currentSecond++;
                    if (onTimeChangeListener != null){
                        onTimeChangeListener.onTimeChanged(currentSecond);
                    }
                }
            };
            timer.schedule(timerTask, 1000, 5000);
        }
    }
    /**
     * 通过接口的方式，完成对调用者的通知，这种方式不是太好，更好的方式是通过LiveData组件来实现
     * */
    public interface OnTimeChangeListener {
        void onTimeChanged(int second);
    }
    private OnTimeChangeListener onTimeChangeListener;
    public void setOnTimeChangeListener(OnTimeChangeListener onTimeChangeListener) {
        this.onTimeChangeListener = onTimeChangeListener;
    }

}
