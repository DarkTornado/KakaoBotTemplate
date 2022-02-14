package com.darktornado.kakaobottemplate;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(1);

        final EditText txt = new EditText(this);
        txt.setHint("소스 입력");
        layout.addView(txt);

        /* 알림 접근 권한 안주고 리로드 누르면 오류뜨니 알아서 하셈셈 */
        Button js = new Button(this);
        js.setText("자바스크립트 리로드");
        js.setOnClickListener(v -> {
            String src = txt.getText().toString();
            if (src.equals("")) toast("입력한 내용이 없어요");
            else if (KakaotalkListener.js.reload(src)) toast("스크립트 리로드 완료");
        });
        layout.addView(js);
        Button lua = new Button(this);
        lua.setText("루아 리로드");
        lua.setOnClickListener(v -> {
            //미래의 내가 만들어줄거임
        });
        layout.addView(lua);
        setContentView(layout);
    }

    private void toast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

}