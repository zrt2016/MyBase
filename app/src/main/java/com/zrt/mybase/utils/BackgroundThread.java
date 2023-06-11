package com.zrt.mybase.utils;

import android.os.Handler;
import android.os.HandlerThread;

import java.util.HashMap;
import java.util.Map;

/**
 * 需要自己控制生命周期，在这个生命周期内都可以使用这个线程
 *
 */
public class BackgroundThread extends HandlerThread {
//    private BackgroundThread mInstance;
//    private Handler mHandler;
//    private Handler mHandler2;
    private Handler mainHandler;
    public String key;
    public Map<String, String> keyMap;
    public Map<String, Handler> handlerMap;


    private BackgroundThread() {
        this("ThreadName");
    }
    private BackgroundThread(String name) {
        super(name, android.os.Process.THREAD_PRIORITY_DEFAULT);
        keyMap = new HashMap<>();
        handlerMap = new HashMap<>();
//        prepareThread();
    }
    public void addHandler(String handlerKey, String keyValue){
        keyMap.put(handlerKey, keyValue);
        handlerMap.put(handlerKey, new Handler((this.getLooper())));
    }
    public void removeHandler(String handlerKey){
        keyMap.remove(handlerKey);
        handlerMap.remove(handlerKey);
    }
//    private void prepareThread() {
////        mInstance = new BackgroundThread();
////        // 创建HandlerThread后一定要记得start()
////        mInstance.start();
//        // 获取HandlerThread的Looper,创建Handler，通过Looper初始化
//        mHandler = new Handler(this.getLooper());
//        mHandler2 = new Handler(this.getLooper());
//    }

    private void setMainHandle(Handler mainHandler) {
    	this.mainHandler = mainHandler;
    }
    
    public Handler getMainHandle() {
    	return mainHandler;
    }

    /**
     * 如果需要在后台线程做一件事情，那么直接调用post方法，使用非常方便
     */
    public void post(String handlerKey, final Runnable runnable) {
        postDelayed(handlerKey, runnable, 0);
    }

    public void postDelayed(String handlerKey, final Runnable runnable, long nDelay) {
        if (handlerMap.containsKey(handlerKey) && handlerMap.get(handlerKey) == null) return;
        handlerMap.get(handlerKey).postDelayed(runnable, nDelay);
    }

    
    /**
     * 退出HandlerThread
     */
    public void destroyThread() {
            this.quit();
//            mInstance = null;
//            mHandler = null;
            mainHandler = null;
            keyMap.clear();
            handlerMap.clear();

    }
    public static class Builder{
        private String threadName = "ThreadName";
        private String key;
        private Handler mainHandler;
        public Builder setKey(String key){
            this.key = key;
            return this;
        }
        public Builder setThreadName(String threadName){
            this.threadName = threadName;
            return this;
        }
        public Builder setMainHandler(Handler mainHandler){
            this.mainHandler = mainHandler;
            return this;
        }
        public BackgroundThread create(){
            BackgroundThread thread = new BackgroundThread(threadName);
            thread.start();
            thread.key = key;
            thread.setMainHandle(mainHandler);
//            thread.prepareThread();
            return thread;
        }
    }

    /**
     * 测试例子
     */
    int num1 = 0;
    int num2 = 0;
    private void ceshiDemo() {
        final BackgroundThread backgroundThread = new BackgroundThread.Builder()
                .setKey("123456")
                .setMainHandler(new Handler())
                .create();
        final String key1 = "KEY_1";
        backgroundThread.addHandler(key1, "value1");
        Runnable runnable1 = new Runnable() {
            @Override
            public void run() {
                MyLogger.Log().i("##runnable-1="+num1);
                num1++;
                if (num1 < 5){
                    backgroundThread.postDelayed(key1, this, 1000);
                }else{
                    backgroundThread.getMainHandle().post(new Runnable() {
                        @Override
                        public void run() {
                            backgroundThread.removeHandler(key1);
                            MyLogger.Log().i("##runnable-1="+num1+"; end");
                        }
                    });
                }
            }
        };
        final String key2 = "KEY_2";
        backgroundThread.addHandler(key2, "value2");
        Runnable runnable2 = new Runnable() {
            @Override
            public void run() {
                MyLogger.Log().i("##runnable2="+num2);
                num2++;
                if (num2 < 5){
                    backgroundThread.postDelayed(key2, this, 2000);
                }else {
                    backgroundThread.getMainHandle().post(new Runnable() {
                        @Override
                        public void run() {
                            backgroundThread.removeHandler(key2);
                            MyLogger.Log().i("##runnable-2="+num2+"; end");
                        }
                    });
                }
            }
        };
        backgroundThread.postDelayed(key1, runnable1, 1000);
        backgroundThread.postDelayed(key2, runnable2, 2000);
    }
}