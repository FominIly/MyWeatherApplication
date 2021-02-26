package com.example.myweatherapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    String result = null;
    String city = null;
    TextView textMain;
    TextView textDescription;

    public class Weather extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            String result = "";
            URL url;
            HttpURLConnection connection;
            StringBuilder stringBuilder = new StringBuilder();
            try {
                url = new URL(urls[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                InputStream in = connection.getInputStream();
                InputStreamReader reader = new InputStreamReader(in);
                int data = reader.read();

                while (data != -1){
                    char current = (char) data;
                    stringBuilder.append(current);
                    data = reader.read();
                }
                result = stringBuilder.toString();
                return result;
            } catch (Exception e) {
                e.printStackTrace();
                return "Failed";
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                JSONObject jsonObject = new JSONObject(s);
                String weatherInfo = jsonObject.getString("weather");
                JSONArray arr = new JSONArray(weatherInfo);
                for(int i=0; i<arr.length(); i++){
                    JSONObject jsonObject1 = arr.getJSONObject(i);
                    textMain.setText(jsonObject1.getString("main"));
                    textDescription.setText(jsonObject1.getString("description"));
                    Log.i("MAIN", jsonObject1.getString("main"));
                    Log.i("MAIN", jsonObject1.getString("description"));
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

public void onClick(View view){

    EditText enterCity = (EditText) findViewById(R.id.editTextTextPersonName);

    Weather task = new Weather();
    try {
        result = task.execute("http://api.openweathermap.org/data/2.5/weather?q="+ enterCity.getText().toString() +"&appid=8c1d561d0e5214acd00561ab8ff73f77").get();
    } catch (Exception e) {
        e.printStackTrace();
    }

}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textMain = (TextView) findViewById(R.id.textView);
        textDescription = (TextView) findViewById(R.id.textView2);

    }


}