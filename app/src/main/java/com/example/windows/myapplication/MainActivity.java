package com.example.windows.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btn = (Button)findViewById(R.id.button);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView view = (TextView)findViewById(R.id.init_message);
                Toast.makeText(MainActivity.this,"整整整", Toast.LENGTH_SHORT).show();
                view.setText(R.string.interact_message);
            }
        });

        TextView api_ip = (TextView)findViewById(R.id.api_ip);
        api_ip.setText("http:/"+ServerManager.getLocalIPAddress()+":9527/test?k=1");

        TextView tv = (TextView)findViewById(R.id.api_run);

        TextView api_msg = (TextView)findViewById(R.id.api_msg);
        api_msg.setText("初始化.");
        ServerManager serverManager = new ServerManager(tv);
        api_msg.setText("初始化..");
        serverManager.initServer(this);
        api_msg.setText("初始化...");

    }
}
