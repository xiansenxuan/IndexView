package com.xuan.indexview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private IndexView index_view;
    private TextView tv_middle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        index_view=findViewById(R.id.index_view);
        tv_middle=findViewById(R.id.tv_middle);

        index_view.addIndexViewInter(new IndexView.IndexViewCallBackInter() {
            @Override
            public void selectIndexText(String text) {
                if(!TextUtils.isEmpty(text))
                    tv_middle.setText(text);
                    tv_middle.setVisibility(View.VISIBLE);
            }

            @Override
            public void cancelSelect() {
                tv_middle.setText("");
                tv_middle.setVisibility(View.GONE);
            }
        });
    }
}
