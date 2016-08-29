package com.wzkj.vzone.org;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.tencent.mm.sdk.modelmsg.SendAuth;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btn_wei_login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_wei_login = (Button) findViewById(R.id.btn_wei_login);
        btn_wei_login.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_wei_login:
                Toast.makeText(this, "go", Toast.LENGTH_LONG).show();
                if (!WeiApplication.api.isWXAppInstalled()) {
                    Toast.makeText(this, "您还未安装微信客户端", Toast.LENGTH_LONG).show();
                    return;
                }
                final SendAuth.Req req = new SendAuth.Req();
                req.scope = "snsapi_userinfo";
                req.state = "tmdgame_wx_login";
                WeiApplication.api.sendReq(req);

                break;
            default:
                break;
        }
    }
}
