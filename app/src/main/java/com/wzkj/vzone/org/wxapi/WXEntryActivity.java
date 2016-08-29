package com.wzkj.vzone.org.wxapi;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.wzkj.vzone.org.R;
import com.wzkj.vzone.org.WeiApplication;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class WXEntryActivity extends Activity implements IWXAPIEventHandler {
	//这两个常量代表了微信返回的消息类型，是对登录的处理还是对分享的处理，登录会在后面介绍到
	private static final int RETURN_MSG_TYPE_LOGIN = 1;
	private static final int RETURN_MSG_TYPE_SHARE = 2;
	private static final String TAG = "WXEntryActivity";

	private AsyncHttpClient client = new AsyncHttpClient();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_wxentry);
		WeiApplication.api.handleIntent(getIntent(), this);
		//如果没回调onResp，八成是这句没有写
	}

	// 微信发送消息给app，app接受并处理的回调函数
	@Override
	public void onReq(BaseReq baseReq) {

	}

	// app发送消息给微信，微信返回的消息回调函数,根据不同的返回码来判断操作是否成功
	@Override
	public void onResp(BaseResp resp) {
		switch (resp.errCode) {
			case BaseResp.ErrCode.ERR_AUTH_DENIED:
			case BaseResp.ErrCode.ERR_USER_CANCEL:
				//showToast("微信失败");
				break;
			case BaseResp.ErrCode.ERR_OK:
				switch (resp.getType()) {
					case RETURN_MSG_TYPE_SHARE:
						finish();
						break;

					case RETURN_MSG_TYPE_LOGIN:
						Toast.makeText(this, "微信登录返回", Toast.LENGTH_LONG).show();

						// 获取到code
						String code = ((SendAuth.Resp) resp).code;

						String url = "https://api.weixin.qq.com/sns/oauth2/access_token?" +
								"appid=" + WeiApplication.APP_ID +
								"&secret=a377798fad9efea5408d83706d400e0e" +
								"&code=" + code +
								"&grant_type=authorization_code";
						client.get(url, new JsonHttpResponseHandler() {
							@Override
							public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
								super.onSuccess(statusCode, headers, response);
								if (response != null) {
									try {
										String unionid = response.getString("unionid");
										String openid = response.getString("openid");
										String refresh_token = response.getString("refresh_token");
										String expires_in = response.getString("expires_in");
										String access_token = response.getString("access_token");

										System.out.println(unionid + "===" + openid + "===" + refresh_token + "===" + expires_in + "===" + access_token);



										client.get("https://api.weixin.qq.com/sns/userinfo?access_token=" + access_token +
												"&openid=" + openid, new JsonHttpResponseHandler(){
											@Override
											public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
												super.onSuccess(statusCode, headers, response);

												if (response != null) {
													try {
														String nickname = response.getString("nickname");
														String sex = response.getString("sex");
														String province = response.getString("province");
														String city = response.getString("city");
														String country = response.getString("country");
														String headimgurl = response.getString("headimgurl");

														String info = "nickname:" + nickname +
																" sex:" + sex +
																" procince:" + province +
																" city:" + city +
																" country:" + country +
																" headimagurl" + headimgurl;
														Toast.makeText(getApplicationContext(), info, Toast.LENGTH_LONG).show();

													} catch (Exception e) {

													}
												}
											}
										});

									} catch (Exception e) {
										e.printStackTrace();
									}
								}
							}
						});
						finish();
						break;
				}
				break;
		}
	}
}