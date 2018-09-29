package com.test.webapplication;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    Button button;
    TextView text;

     public class Category {
         int [] category1 = { 6818, 6847, 6881, 6924, 6938, 6955, 6998, 7009, 7023, 7044, 7045   };
         int [] category2 = { 6799, 6826, 6866, 6886, 6891, 6928, 6948, 6953, 6991, 7007, 7017, 7024, 7042   };
         int [] category3 = { 6833, 6834, 6871, 6880, 6896, 6944, 6966, 7029  };
         int [] category4 = { 6832, 6857, 6865, 6988 };
         int [] category5 = { 6853, 6897, 6913, 6917, 6939, 6962 };

     }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button = (Button) findViewById(R.id.button);
        text = (TextView) findViewById(R.id.text);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new InternetThread(new TextHandler()).start();
            }
        });
    }

    private class TextHandler extends Handler {
        public void handleMessage (Message msg) {
            Bundle bun = msg.getData();
            text.setText(bun.getString("HTML"));
        }
    }

    private class InternetThread extends Thread {

        CheckBox checkBox1 = (CheckBox) findViewById(R.id.check1);
        CheckBox checkBox2 = (CheckBox) findViewById(R.id.check2);
        CheckBox checkBox3 = (CheckBox) findViewById(R.id.check3);
        CheckBox checkBox4 = (CheckBox) findViewById(R.id.check4);
        CheckBox checkBox5 = (CheckBox) findViewById(R.id.check5);

        Handler handler;

        public InternetThread (Handler handler) {
            this.handler = handler;
        }

        public void run () {
            String html = connect();

            Bundle bundle = new Bundle();

            JSONObject json1 = null;
            String output = "";
            try {
                json1 = new JSONObject(html);
                JSONArray json2 = json1.getJSONObject("SbChcHealth").getJSONArray("row");
                for (int i = 0; i < json2.length(); i++) {

                    Category c = new Category();
                    JSONObject json3 = json2.getJSONObject(i);

                    if (checkBox1.isChecked()) {
                        for(i = 0; i<c.category1.length; i++) {
                            if (json3.getInt("IDX") == c.category1[i]) {
                                output += json3.getString("TITLE") + "\n" + json3.getString("CONTENT") + "\n\n";
                            }
                        }
                    }

                     if (checkBox2.isChecked()) {
                         for(i = 0; i<c.category2.length; i++) {
                             if (json3.getInt("IDX") == c.category2[i]) {
                                 output += json3.getString("TITLE") + "\n" + json3.getString("CONTENT") + "\n\n";
                             }
                         }
                     }

                      if (checkBox3.isChecked()) {
                          for(i = 0; i<c.category3.length; i++) {
                              if (json3.getInt("IDX") == c.category3[i]) {
                                  output += json3.getString("TITLE") + "\n" + json3.getString("CONTENT") + "\n\n";
                              }
                          }
                      }

                       if (checkBox4.isChecked()) {
                           for(i = 0; i<c.category4.length; i++) {
                               if (json3.getInt("IDX") == c.category4[i]) {
                                   output += json3.getString("TITLE") + "\n" + json3.getString("CONTENT") + "\n\n";
                               }
                           }
                       }

                        if (checkBox5.isChecked()) {
                            for(i = 0; i<c.category5.length; i++) {
                                if (json3.getInt("IDX") == c.category5[i]) {
                                    output += json3.getString("TITLE") + "\n" + json3.getString("CONTENT") + "\n\n";
                                }
                            }
                        }

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }


            bundle.putString("HTML", output);
            Message msg = handler.obtainMessage();
            msg.setData(bundle);
            handler.sendMessage(msg);
        }

        private String connect () {
            try {
                URL url = new URL("http://openapi.seoul.go.kr:8088/444543414e74746e37335048576a43/json/SbChcHealth/1/100/");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                int resCode = connection.getResponseCode();
                if (resCode != HttpURLConnection.HTTP_OK)
                    throw new Error("Code: " + resCode);

                InputStream in = connection.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                String data = "", buf;

                while ((buf = reader.readLine()) != null) {
                    data += buf + "\n";
                }

                return data;
            } catch (Exception e) {
                return e.toString();
            }
        }
    }
}
