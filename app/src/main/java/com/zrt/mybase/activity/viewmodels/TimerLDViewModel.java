package com.zrt.mybase.activity.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.Timer;
import java.util.TimerTask;

/**
 * @author：Zrt
 * @date: 2022/2/18
 */
public class TimerLDViewModel extends ViewModel {


    @Override
    protected void onCleared() {
        super.onCleared();
    }

    MutableLiveData<Integer> secondLD;
    public LiveData<Integer> getCurrentSecond(){
        if (secondLD == null){
            secondLD = new MutableLiveData<>();
        }
        return secondLD;
    }
    /**
     * 写一个定时计时器，每隔1秒调用接口，通知UI更新
     */
    private Timer timer;
    private int currentSecond;
    public void startMing() {
        if (timer == null) {
            timer = new Timer();
            currentSecond = 0;
            secondLD.setValue(currentSecond);
            TimerTask timerTask = new TimerTask() {
                @Override
                public void run() {
                    // 主线程使用setValue
//                    secondLD.setValue(++currentSecond);
                    // 子线程使用postValue
                    secondLD.postValue(++currentSecond);
                }
            };
            timer.schedule(timerTask, 1000, 1000);
        }
    }


    MutableLiveData<Integer> secondLD2;
    public LiveData<Integer> getCurrentSecond2(){
        if (secondLD2 == null){
            secondLD2 = new MutableLiveData<>();
        }
        return secondLD2;
    }
}
