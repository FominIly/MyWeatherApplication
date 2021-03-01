package com.example.myweatherapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    String result = null;
    String city = null;
    TextView textMain;
    TextView textDescription;
    EditText cityEntrance;

    // класс для запроса данных по погоде и обработке JSON
    public class Weather extends AsyncTask<String, Void, String> {
        // создаем соединение с сайтом и читаем его
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

        // создаем JSON файл и обрабатывем его
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                JSONObject jsonObject = new JSONObject(s);
                String weatherInfo = jsonObject.getString("weather");
                JSONArray arr = new JSONArray(weatherInfo);
                for(int i=0; i<arr.length(); i++){
                    JSONObject jsonObject1 = arr.getJSONObject(i);
                    // выводим нужные данные на экран
                    textMain.setText(jsonObject1.getString("main"));
                    textDescription.setText(jsonObject1.getString("description"));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    // обращаемся к сайту согласно указаному городу в поле ввода
public void findWeather(View view){
    Weather task = new Weather();
    try {
        result = task.execute("http://api.openweathermap.org/data/2.5/weather?q="+ cityEntrance.getText().toString() +"&appid=8c1d561d0e5214acd00561ab8ff73f77").get();
    } catch (Exception e) {
        e.printStackTrace();
    }

    // Проверка корректности введения имени города. Если имя не правильно, всплывет сообщение
    if(result.equals("Failed")){
        Toast toast = Toast.makeText(getApplicationContext(),
                "Город не существует!", Toast.LENGTH_SHORT);
        toast.show();
    }
}

// Очищаем поле ввода города
public void clearEditText(View view){
    cityEntrance.getText().clear();
}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
        textMain = (TextView) findViewById(R.id.textView);
        textDescription = (TextView) findViewById(R.id.textView2);
        cityEntrance = (EditText) findViewById(R.id.editText);
    }
}
