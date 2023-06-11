package com.zrt.mybase.activity.viewdemo;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.HttpAuthHandler;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.TextView;

import com.zrt.mybase.R;
import com.zrt.mybase.utils.MyLogger;

public class WebViewActivity extends AppCompatActivity {
//    public static final String URL = "https://dev-yxqv2.linksign.cn/h5/accreditLogin/accreditLogin.html?transactionId=6cg40mgebqgny7dq";
    public static final String URL = "https://dev-yxqv2.linksign.cn/h5/accreditLogin/accreditLogin.html?transactionId=6cg40mgebqgny7dq";
    public static final String password = "Zzszxyy@123";
    WebView webView;
    private WebSettings setting;
    TextView text_url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        initView();
        initWeb();
    }

    private void initView() {
        webView = findViewById(R.id.webView);
        text_url = findViewById(R.id.text_url);
        text_url.setText(URL);
    }

    private void initWeb() {
        setSetting();
        webView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        webView.setWebViewClient(webViewClient);
//        this.webView.getSettings().setJavaScriptEnabled(true);
//        this.home_javascript = new JavaScriptExtension(this);
//        /** 添加JavaScript访问接口,Android原生环境和JavaScript交互的另一个窗口 */
//        this.webView.addJavascriptInterface(this.home_javascript, "jsjiaohu");
//        webView.loadUrl(URL);
        webView.post(new Runnable() {
            @Override
            public void run() {
                webView.loadUrl(URL);
            }
        });
    }

    private void setSetting() {
        this.setting = this.webView.getSettings();

        /** 启用JavaScript,JavaScript自动打开窗口 */
        this.setting.setJavaScriptEnabled(true);
        this.setting.setJavaScriptCanOpenWindowsAutomatically(true);

        // 打开页面时， 自适应屏幕
        setting.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NORMAL);
        /** 设置此属性，可任意比例缩放 */
        this.setting.setUseWideViewPort(true);
        /** 将页面缩放到屏幕大小 */
        this.setting.setLoadWithOverviewMode(true);


//        /** 支持页面缩放 */
        this.setting.setBuiltInZoomControls(true);
        this.setting.setSupportZoom(true);

        /** 设置渲染优先级 */
        this.setting.setRenderPriority(WebSettings.RenderPriority.HIGH);

        /** 启用HTML5离线数据 */
        this.setting.setDatabaseEnabled(true);
        this.setting.setAppCacheEnabled(true);

         String dir = this.getApplicationContext().getDir("database", Context.MODE_PRIVATE).getPath();
        // 启用地理定位
        // this.webView.getSettings().setRenderPriority(RenderPriority.HIGH);
        // this.setting.setGeolocationEnabled(true); // 设置定位的数据库路径
        // this.setting.setGeolocationDatabasePath(dir);
        // 最重要的方法，一定要设置，这就是出不来的主要原因
        this.setting.setDomStorageEnabled(true);
        this.setting.setAllowFileAccess(true);
        this.setting.setAppCacheEnabled(true);
        this.setting.setAppCacheMaxSize(1024*1024*8);
        // 设置应用缓存的路径
        String appCachePath = getApplicationContext().getCacheDir().getAbsolutePath();
        this.setting.setAppCachePath(appCachePath);

        //设置数据库路径
//        String dirDatabase = this.getApplicationContext().getDir("database", Context.MODE_PRIVATE).getPath();
//        this.setting.setDatabasePath(dirDatabase);

        String dirCache = this.getApplicationContext().getDir("cache", Context.MODE_PRIVATE).getPath();
        this.setting.setAppCachePath(dirCache);



        /** 允许通过file url加载的JavaScript读取其他的本地文件 */
        this.setting.setAllowFileAccessFromFileURLs(true);
        this.setting.setAllowContentAccess(true);
        this.setting.setAllowUniversalAccessFromFileURLs(true);

        if(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
            this.setting.setAllowUniversalAccessFromFileURLs(true);
            this.setting.setAllowFileAccessFromFileURLs(true);
        }

        /** 支持插件 */
//		this.setting.setPluginsEnabled(true);

        /** 设置地理定位 */
//        this.setting.setGeolocationEnabled(true);
//        this.setting.setGeolocationDatabasePath(this.getFilesDir().getPath());

        /** 设置插件状态 */
        this.setting.setPluginState(WebSettings.PluginState.ON);

        /** 网络图片加载 */
        this.setting.setBlockNetworkLoads(false);
        this.setting.setBlockNetworkImage(false);
        this.setting.setLoadsImagesAutomatically(true);

        /** 保存密码即表单数据 */
//        this.setting.setSavePassword(true);
//        this.setting.setSaveFormData(true);

        /** 设置缓存模式 */
         this.setting.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        // //关闭webview中缓存
//		this.setting.setCacheMode(this.isNetworkAvailable(HomeActivity.this) ? WebSettings.LOAD_DEFAULT : WebSettings.LOAD_CACHE_ELSE_NETWORK);

        // 不使用缓存
        this.setting.setCacheMode(WebSettings.LOAD_NO_CACHE);
        this.setting.setDefaultTextEncodingName("utf-8");
    }

    WebViewClient webViewClient = new WebViewClient(){
        /**
         * 当加载的网页需要重定向的时候就会回调这个函数告知我们应用程序是否需要接管控制网页加载，如果应用程序接管，
         * 并且return true意味着主程序接管网页加载，如果返回false让webview自己处理。
         */
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

        /**
         * 开始加载网络
         * 当内核开始加载访问的url时，会通知应用程序，对每个main frame
         * 这个函数只会被调用一次，页面包含iframe或者framesets 不会另外调用一次onPageStarted，
         * 当网页内内嵌的frame 发生改变时也不会调用onPageStarted。
         * @param view
         * @param url
         * @param favicon:如果这个favicon已经存储在本地数据库中，则会返回这个网页的favicon，否则返回为null。
         */
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
//            loadingDialog.show();
            MyLogger.Log().d(String.format("##onPageStarted url:%s", url));
        }

        /**
         * 网页加载完成回调
         * 当内核加载完当前页面时会通知我们的应用程序，这个函数只有在main
         * frame情况下才会被调用，当调用这个函数之后，渲染的图片不会被更新，如果需要获得新图片的通知可以使用@link
         * WebView.PictureListener#onNewPicture。 参数说明：
         * @param view
         * @param url
         */
        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            MyLogger.Log().i("##onPageFinished url="+url );
        }

        /**
         * 加载Url资源回调
         * 通知应用程序WebView即将加载url 制定的资源
         * @param view
         * @param url
         */
        @Override
        public void onLoadResource(WebView view, String url) {
            super.onLoadResource(view, url);
            MyLogger.Log().i("##onLoadResource url="+url );
        }

        /**
         * 访问地址错误回调，当浏览器访问制定的网址发生错误时会通知我们应用程序 参数说明
         * @param view
         * @param errorCode：错误号可以在WebViewClient.ERROR_* 里面找到对应的错误名称。
         * @param description：描述错误的信息
         * @param failingUrl：当前访问失败的url，注意并不一定是我们主url
         */
        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            super.onReceivedError(view, errorCode, description, failingUrl);
            MyLogger.Log().e(String.format("##onReceivedError errorCode:%s, failingUrl:%s", description, failingUrl));
            if (errorCode == ERROR_HOST_LOOKUP || errorCode == ERROR_CONNECT || errorCode == ERROR_TIMEOUT) {
//                showErrorPage(view);
            }
        }

        @RequiresApi(api = Build.VERSION_CODES.M)
        @Override
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError
                error) {
            super.onReceivedError(view, request, error);
            MyLogger.Log().e(String.format("##onReceivedError request:%s, error:%s", request.toString(), error.getErrorCode()));
            if (request.isForMainFrame()) {
                this.onReceivedError(view, error.getErrorCode(), error.getDescription().toString(), request.getUrl().toString());
            }
//            MyLogger.Log().e(String.format("errorCode:%s, failingUrl:%s", description, failingUrl));
//            isReLoading = false;
//            loadingDialog.dismiss();
//            updateView(false);			//隐藏网页，显示加载失败的原生页面
        }

        @Override
        public void onReceivedHttpError(WebView view, WebResourceRequest request, WebResourceResponse errorResponse) {
            super.onReceivedHttpError(view, request, errorResponse);
            MyLogger.Log().i(String.format("##onReceivedHttpError request:%s, error:%s", request.toString(), errorResponse.toString()));
        }

        @Override
        public void onReceivedHttpAuthRequest(WebView view, HttpAuthHandler handler, String host, String realm) {
            super.onReceivedHttpAuthRequest(view, handler, host, realm);
            MyLogger.Log().i("##onReceivedHttpAuthRequest host="+host+";realm="+realm);
        }
    };
}