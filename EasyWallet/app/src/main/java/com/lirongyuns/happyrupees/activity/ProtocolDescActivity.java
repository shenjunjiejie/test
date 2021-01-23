package com.lirongyuns.happyrupees.activity;

import android.annotation.TargetApi;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.DownloadListener;
import android.webkit.WebBackForwardList;
import android.webkit.WebChromeClient;
import android.webkit.WebHistoryItem;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.Nullable;

import com.lirongyuns.happyrupees.R;
import com.lirongyuns.happyrupees.annonation.SetActivity;
import com.lirongyuns.happyrupees.annonation.SetView;
import com.lirongyuns.happyrupees.dialog.LoadingDialog;

import java.net.URISyntaxException;


/**
 * 协议详情页面
 */
@SetActivity(R.layout.activity_loan_desc)
public class ProtocolDescActivity extends BaseActivity {
    
    @SetView(R.id.wv_loan_desc_content)
    private WebView mWvLoanContent;
    
    private String mFlag;
    private String jumpUrl;
    
    
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        jumpUrl = intent.getStringExtra("jumpUrl");
        Log.e("http", "jumpUrl==="+jumpUrl);
        mFlag = intent.getStringExtra("flag");
        
        initData();
    }
    
    
   
    
    private void initData() {
        if(TextUtils.isEmpty(jumpUrl)){
            return;
        }
        mWvLoanContent.setLayerType(View.LAYER_TYPE_HARDWARE, null);
//        jumpUrl = "https://play.google.com/store/apps/details?id=com.sttdzv.esoxyv.fastcredit&referrer=adjust_reftag%3Dcft208BEfyseI%26utm_source%3DFF4";
        LoadingDialog.open(this);
        mWvLoanContent.loadUrl("https://server.magicrupees.com/profile/upload/2020/11/03/59dd790830c3cdb45eb9a990a60f48fc2.docx");
        
//        Log.e("http", "jumpUrl======"+jumpUrl);
        
        
        WebSettings webSetting = mWvLoanContent.getSettings();
        webSetting.setDomStorageEnabled(true);
        webSetting.setJavaScriptEnabled(true);
        webSetting.setAllowFileAccess(true);
//        webSetting.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        webSetting.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        webSetting.setSupportZoom(false);
        webSetting.setBuiltInZoomControls(false);
        webSetting.setUseWideViewPort(true);
        webSetting.setSupportMultipleWindows(false);
        webSetting.setAllowFileAccess(true);
        webSetting.setAppCacheEnabled(true);
        webSetting.setGeolocationEnabled(true);
        webSetting.setAppCacheMaxSize(Long.MAX_VALUE);
        webSetting.setAppCachePath(this.getDir("appcache", 0).getPath());
        webSetting.setDatabasePath(this.getDir("databases", 0).getPath());
        webSetting.setGeolocationDatabasePath(this.getDir("geolocation", 0)
                .getPath());
        webSetting.setPluginState(WebSettings.PluginState.ON_DEMAND);
        
        webSetting.setDefaultTextEncodingName("utf-8");
        
        mWvLoanContent.setWebViewClient(new WebPagerClient());
        mWvLoanContent.setHorizontalScrollBarEnabled(false);
        mWvLoanContent.setVerticalScrollBarEnabled(false);
        mWvLoanContent.setWebChromeClient(new MyWebViewChromeClient());
        mWvLoanContent.setDownloadListener(new DownloadListener() {
            @Override
            public void onDownloadStart(String s, String s1, String s2, String s3, long l) {
                Log.e("http", "s==="+s);
                downloadByBrowser(s);
            }
        });
    }
    
    public class MyWebViewChromeClient extends WebChromeClient {
        @Override
        public void onReceivedTitle(WebView view, String title) {
            super.onReceivedTitle(view, title);
            getWebViewPageTitle();
        }
        
    }
    
    
    
    public class WebPagerClient extends WebViewClient {
        
        @Override
        public void onPageStarted(WebView webView, String s, Bitmap bitmap) {
            LoadingDialog.open(ProtocolDescActivity.this);
        }
        
        @Override
        public void onPageFinished(WebView webView, String s) {
            String title = webView.getTitle();
            LoadingDialog.close();
        }
        
        @Override
        public void onReceivedError(WebView webView, int i, String s, String s1) {
        }
      
        
        @Override
        public boolean shouldOverrideUrlLoading(WebView webView, String url) {
            Uri uri = Uri.parse(url);
            WebView.HitTestResult hitTestResult = webView.getHitTestResult();
            if (hitTestResult != null) {
                Intent intent = new Intent();
                intent.setAction( "android.intent.action.VIEW");
                Uri content_url = Uri. parse(url);
                intent.setData(content_url);
                startActivity(intent);
                return true;
            }
    
            try{
                if(url.contains("https://play.google.com/store")){
                    Intent intent = new Intent();
                    intent.setAction("android.intent.action.VIEW");
                    intent.setData(uri);
                    startActivity(intent);
                    return true;
                }else if(url.contains("http://www.samsungapps.com/")){
                    Intent intent = Intent.parseUri(url, Intent.URI_INTENT_SCHEME); // 注释1
                    if (getPackageManager().resolveActivity(intent, 0) == null) {
                        // 如果手机还没安装app，则跳转到应用市场
                        goToSamsungappsMarket(intent.getPackage());
                    }
                    return true;
                    
                }else if(url.contains("market://details")){
                    Intent intent = Intent.parseUri(url, Intent.URI_INTENT_SCHEME); // 注释1
                    if (getPackageManager().resolveActivity(intent, 0) == null) {
                        // 如果手机还没安装app，则跳转到应用市场
                        goToMarket(intent.getPackage());
                    }
                   
                    return true;
                    
                }else if(url.startsWith("intent")){
                    try {
                        Intent intent = Intent.parseUri(url, Intent.URI_INTENT_SCHEME);
                        String fallbackUrl = intent.getStringExtra("browser_fallback_url");
                        if (fallbackUrl != null) {
                            mWvLoanContent.loadUrl(fallbackUrl);
                            return true;
                        }
    
                        return super.shouldOverrideUrlLoading(webView, url);
                    } catch (URISyntaxException e) {
                            //not an intent uri
                            return false;
                    }
                }else{
                    webView.loadUrl(url);
                    return false;
                }
                
                
            }catch (Exception e){
                e.printStackTrace();
                return super.shouldOverrideUrlLoading(webView, url);
            }
        }
        
        
        
        
        public  void goToMarket( String packageName) {
            //market://details?id=com.huawei.appmarket
            Uri uri = Uri.parse("market://details?id=" + packageName);
            Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
            goToMarket.setPackage("com.android.vending");
            try {
                startActivity(goToMarket);
            } catch (ActivityNotFoundException e) {
                e.printStackTrace();
            }
        }
        
        
        public  void goToSamsungappsMarket(String packageName) {
            Uri uri = Uri.parse("http://www.samsungapps.com/appquery/appDetail.as?appId=" + packageName);
            Intent goToMarket = new Intent();
            goToMarket.setClassName("com.sec.android.app.samsungapps", "com.sec.android.app.samsungapps.Main");
            goToMarket.setData(uri);
            try {
                startActivity(goToMarket);
            } catch (ActivityNotFoundException e) {
                e.printStackTrace();
            }
        }
        
        
        @TargetApi(android.os.Build.VERSION_CODES.M)
        @Override
        public void onReceivedHttpError(WebView view, WebResourceRequest request, WebResourceResponse errorResponse) {
            super.onReceivedHttpError(view, request, errorResponse);
            // 这个方法在6.0才出现
            int statusCode = errorResponse.getStatusCode();
//            System.out.println("onReceivedHttpError code = " + statusCode);
            if (404 == statusCode || 500 == statusCode) {
//                view.loadUrl("about:blank");// 避免出现默认的错误界面
//                view.loadUrl(mErrorUrl);
            }
        }
        
    }
    
    private void downloadByBrowser(String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addCategory(Intent.CATEGORY_BROWSABLE);
        intent.setData(Uri.parse(url));
        startActivity(intent);
    }
    
    
    
    private void getWebViewPageTitle(){
        WebBackForwardList forwardList = mWvLoanContent.copyBackForwardList();
        WebHistoryItem item = forwardList.getCurrentItem();
        if (item != null) {
            setTitle(item.getTitle());
//            mTvLoanDescTitle.setText(item.getTitle());
        }
    }
    
    private void onWebViewGoBack(){
        mWvLoanContent.goBack();
        getWebViewPageTitle();
    }
    
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (mWvLoanContent.canGoBack()) {
            onWebViewGoBack();
            return false;
            
        }
        
        jumpMainActivity();
        return super.onKeyDown(keyCode, event);
    }
    
    /**
     * 跳转到首页
     */
    private void jumpMainActivity() {
        if ("operate".equals(mFlag)) {
            startActivity(new Intent(ProtocolDescActivity.this, MainActivity.class));
        }else{
            finish();
        }
    }
    
    
    @Override
    protected void onResume() {
        super.onResume();
    }
    
    @Override
    protected void onPause() {
        super.onPause();
    }
    
    
    @Override
    protected void onDestroy() {
        if (mWvLoanContent != null) {
            mWvLoanContent.loadDataWithBaseURL(null, "", "text/html", "utf-8", null);
            mWvLoanContent.clearHistory();
            
            ((ViewGroup) mWvLoanContent.getParent()).removeView(mWvLoanContent);
            mWvLoanContent.destroy();
           
            mWvLoanContent = null;
        }
        
        super.onDestroy();
    }
    
}
