package com.ablecloud.rxjavademo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * author : dukai
 * date  : 2018/8/15
 * describe:
 */
public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.create, R.id.translate, R.id.compose, R.id.function, R.id.filter,R.id.condition})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.create:
                startActivity(new Intent(this, CreateOperationActivity.class));
                break;
            case R.id.translate:
                startActivity(new Intent(this, TranslateOperationActivity.class));
                break;
            case R.id.compose:
                startActivity(new Intent(this, ComposeOperationActivity.class));
                break;
            case R.id.function:
                startActivity(new Intent(this, FunctionOperationActivity.class));
                break;
            case R.id.filter:
                startActivity(new Intent(this, FilterOperationActivity.class));
                break;
            case R.id.condition:
                startActivity(new Intent(this, ConditionOperationActivity.class));
                break;
        }
    }
}
