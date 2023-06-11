package com.zrt.mybase.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.zrt.mybase.R;
import com.zrt.mybase.base.BaseActivity;

/**
 * 动态更换APP图标
 * 1.图标更换：在AndroidManifest设置应用入口Activity的别名，然后通过setComponentEnabledSetting动态启用或禁用别名进行图标切换。
 * 2.控制图标显示：冷启动App时，调用接口判断是否需要切换icon。(ForegroundCallbacks)
 * 3.触发时机：监听App前后台切换，当App处于后台时切换图标，使得用户无感知。
 * https://mp.weixin.qq.com/s/SmYAtRQqunYwL5faWtFWnw
 *
 * 实现：
 *   1、在AndroidManifest.xml中给入口Activity设置activity-alias。
 *      activity-alias属性：
 *      android:name	别名，命名规则同Actively
 *      android:enabled	是否启用别名，这里的主要作用的控制显示应用图标
 *      android:icon	应用图标
 *      android:label	应用名
 *      android:targetActivity	必须指向原入口Activity
 *    例：
 *   <!-- 固定设置一个默认的别名，用来替代原MainActivity -->
 *       <activity-alias
 *       android:name=".DefaultAliasActivity"
 *       android:enabled="true"
 *       android:icon="@mipmap/ic_launcher"
 *       android:label="@string/app_name"
 *       android:targetActivity=".MainActivity">
 *           <intent-filter>
 *               <action android:name="android.intent.action.MAIN" />
 *               <category android:name="android.intent.category.LAUNCHER" />
 *           </intent-filter>
 *       </activity-alias>
 * 具体缺陷如下：
 * 1. 切换icon会关闭应用进程，不是崩溃所以不会上报bugly。
 * 2. 切换icon需要时间，部分华为机型要10s左右，之后能正常打开。
 * 3. 切换icon过程中，部分机型点击图标无法打开应用，提示应用未安装。
 * 2021.11.15更新 魅族机型 16S 不能动态切换icon
 */
public class AppIconReplaceActivity extends BaseActivity {
    Button set_app_icon_default;
    Button set_app_icon_alias2;
    // 0：默认图标。 1：切换alias1图标
    int replaceAppType = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ForegroundCallbacks.get(this).addListener(foregroundListener);
    }

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_app_icon_replace;
    }

    @Override
    protected void initView() {
        set_app_icon_default = findViewById(R.id.set_app_icon_default);
        set_app_icon_alias2 = findViewById(R.id.set_app_icon_alias2);
    }

    @Override
    protected void initData() {
        set_app_icon_default.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                setDefaultAlias(); // 切换默认图标
                replaceAppType = 0; // 修改图标切换类型，在后台进行切换
            }
        });
        set_app_icon_alias2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                setAlias1(); // 切换alias1图标
                replaceAppType = 1; // 修改图标切换alias1，在后台进行切换
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ForegroundCallbacks.get(this).removeListener(foregroundListener);
    }

    /**
     * 设置默认的别名为启动入口
     */
    public void setDefaultAlias() {
        PackageManager packageManager = getPackageManager();

        ComponentName name1 = new ComponentName(this, "com.zrt.mybase.DefaultAliasActivity");
        packageManager.setComponentEnabledSetting(name1, PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);

        ComponentName name2 = new ComponentName(this, "com.zrt.mybase.Alias2Activity");
        packageManager.setComponentEnabledSetting(name2, PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);
    }

    /**
     * 设置别名1为启动入口
     */
    public void setAlias1() {
        PackageManager packageManager = getPackageManager();
        ComponentName name1 = new ComponentName(this, "com.zrt.mybase.DefaultAliasActivity");
        packageManager.setComponentEnabledSetting(name1, PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);

        ComponentName name2 = new ComponentName(this, "com.zrt.mybase.Alias2Activity");
        packageManager.setComponentEnabledSetting(name2, PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);
    }

    ForegroundCallbacks.Listener foregroundListener = new ForegroundCallbacks.Listener() {
        @Override
        public void onForeground() {

        }

        @Override
        public void onBackground() {
            // 当前页面进入后台后会触发
            //根据具体业务需求设置切换条件,我公司采用接口控制icon切换
            switch (replaceAppType){
                case 1:
                    setAlias1();
                    break;
                default:
                    setDefaultAlias();
                    break;
            }
        }
    };

//    1、在AndroidManifest.xml中给入口Activity设置activity-alias
//    <application
//    android:name=".MyApplication"
//    android:allowBackup="true"
//    android:icon="@mipmap/ic_launcher"
//    android:label="@string/app_name"
//    android:supportsRtl="true"
//    android:theme="@style/Theme.SwitchIcon">
//
//    <!-- 原MainActivity -->
//    <activity android:name=".MainActivity" />
//
//    <!-- 固定设置一个默认的别名，用来替代原MainActivity -->
//    <activity-alias
//    android:name=".DefaultAliasActivity"
//    android:enabled="true"
//    android:icon="@mipmap/ic_launcher"
//    android:label="@string/app_name"
//    android:targetActivity=".MainActivity">
//        <intent-filter>
//            <action android:name="android.intent.action.MAIN" />
//            <category android:name="android.intent.category.LAUNCHER" />
//        </intent-filter>
//    </activity-alias>
//
//
//    <!-- 别名1，特定活动需要的图标如：双11，国庆节等 -->
//    <activity-alias
//    android:name=".Alias1Activity"
//    android:enabled="false"
//    android:icon="@mipmap/ic_launcher_show"
//    android:label="@string/app_name"
//    android:targetActivity=".MainActivity">
//        <intent-filter>
//            <action android:name="android.intent.action.MAIN" />
//
//            <category android:name="android.intent.category.LAUNCHER" />
//        </intent-filter>
//    </activity-alias>
//
//</application>
}