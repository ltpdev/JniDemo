package com.example.administrator.jnidemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private static TextView tvName;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvName = (TextView) findViewById(R.id.tv_name);
        tvName.setOnClickListener(this);
        SerialPortUtil.openSerialPort();

    }


    public static void refreshTextView(String serialData) {
        tvName.setText(serialData);
    }


    @Override
    public void onClick(View view) {
        SerialPortUtil.sendSerialPort("ddd");
    }
}
