package com.example.jsondemo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.hardware.input.InputManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    EditText cityName;
    TextView resultTextView;

public class DownloadTask extends AsyncTask<String,Void,String>{


    @Override
    protected String doInBackground(String... urls) {
        String result="";
        URL url;
        HttpURLConnection httpURLConnection=null;
        try {
            url=new URL(urls[0]);
            httpURLConnection=(HttpURLConnection) url.openConnection();
            InputStream inputStream=httpURLConnection.getInputStream();
            InputStreamReader inputStreamReader= new InputStreamReader(inputStream);
            int data=inputStreamReader.read();
            while(data!=-1){
                char current= (char) data;
                result+=current;
                data=inputStreamReader.read();
            }
            return  result;

        } catch (Exception e) {
            Toast.makeText(getApplicationContext(),"Could not find weather",Toast.LENGTH_SHORT);
        }
        return null;
    }

    @Override
    protected void onPostExecute(String result) {
        String message="";
        super.onPostExecute(result);
        JSONObject jsonObject=new JSONObject();
        try {
            String weatherInfo= jsonObject.getString("weather");
            Log.i("Weather info",weatherInfo);
            JSONArray jsonArray=new JSONArray(weatherInfo);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonPart=jsonArray.getJSONObject(i);
                String main="";
                String description="";
                main=jsonPart.getString("main");
                description=jsonPart.getString("description");
                if(main!=""&& description!=""){
                    message+=main+":"+ description+"\r\n";


                }
                if (message != "") {
                    resultTextView.setText(message);
                }
                else{
                    Toast.makeText(getApplicationContext(),"Could not find weather",Toast.LENGTH_SHORT);
                }


            }
        } catch (JSONException e) {
            Toast.makeText(getApplicationContext(),"Could not find weather",Toast.LENGTH_SHORT);
        }
    }
}
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        cityName=(EditText)findViewById(R.id.cityName);
        resultTextView=(TextView)findViewById(R.id.resultTextView);



    }
    public void findWeather(View view){
        InputMethodManager inputMethodManager= (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(cityName.getWindowToken(),0);
        DownloadTask downloadTask=new DownloadTask();
        downloadTask.execute("https://samples.openweathermap.org/data/2.5/weather?q="+cityName.getText().toString()+"&appid=439d4b804bc8187953eb36d2a8c26a02");



    }
}
