package com.wzkj.vzone.org;

import android.app.Application;
import android.content.Context;

import com.loopj.android.http.AsyncHttpClient;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

/**
 * Created by 詹子聪 on 2016/8/29.
 */
public class WeiApplication extends Application {



    // 从官方网站申请到的合法APP_ID
    public static final String APP_ID = "wx1570607999cee32b";
    public static IWXAPI api;


    @Override
    public void onCreate() {
        super.onCreate();
        regToWx(this);
    }

    private void regToWx(Context context) {
        // 通过WXAPIFactory工厂，获取IWXAPI的实例
        api = WXAPIFactory.createWXAPI(context, APP_ID, true);
        // 将应用的appId注册到微信
        api.registerApp(APP_ID);
    }
}
