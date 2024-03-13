package com.example.chuvanchung_weather;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    private EditText timkiem;
    private Button bnttimkiem, bntnext;
    private TextView txtname, txtcountty, txttemp, txthumi, txtstatus, txtcloud, txtwind, txtdate;
    private ImageView imgicon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Anhxa();

        bnttimkiem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String city = timkiem.getText().toString();
                getCurrentWeather(city);
            }
        });
    }

    private void getCurrentWeather(String city) {
        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
        String apiKey = "2dd947ea299e384149c92332273b59b9";  // Thay YOUR_API_KEY bằng khóa API thực tế của bạn
        String url = "https://api.openweathermap.org/data/2.5/weather?q=" + city + "&appid=" + apiKey;

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                response -> {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        updateUI(jsonObject);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        showToast("Lỗi xử lý dữ liệu");
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        showToast("Lỗi kết nối mạng");
                    }
                }
        );

        requestQueue.add(stringRequest);
    }

    private void updateUI(JSONObject jsonObject) throws JSONException {
        String name = jsonObject.getString("name");
        txtname.setText("Tên thành phố: " + name);

        long timestamp = jsonObject.getLong("dt") * 1000L;
        Date date = new Date(timestamp);
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE yyyy-MM-dd HH:mm:ss");
        String formattedDate = dateFormat.format(date);
        txtdate.setText(formattedDate);

        JSONArray weatherArray = jsonObject.getJSONArray("weather");
        JSONObject weatherObject = weatherArray.getJSONObject(0);
        String status = weatherObject.getString("main");
        String icon = weatherObject.getString("icon");
        Picasso.with(MainActivity.this).load("http://openweathermap.org/img/w/" + icon + ".png").into(imgicon);
        txtstatus.setText(status);

        JSONObject mainObject = jsonObject.getJSONObject("main");
        String temperature = mainObject.getString("temp");
        String humidity = mainObject.getString("humidity");
        txttemp.setText(temperature);
        txthumi.setText(humidity);

        JSONObject windObject = jsonObject.getJSONObject("wind");
        String windSpeed = windObject.getString("speed");
        txtwind.setText(windSpeed);

        JSONObject cloudsObject = jsonObject.getJSONObject("clouds");
        String cloudiness = cloudsObject.getString("all");
        txtcloud.setText(cloudiness);

        JSONObject sysObject = jsonObject.getJSONObject("sys");
        String country = sysObject.getString("country");
        txtcountty.setText("Tên quốc gia: " + country);
    }

    private void showToast(final String message) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void Anhxa() {
        timkiem = findViewById(R.id.txt1);
        bnttimkiem = findViewById(R.id.bnt1);
        bntnext = findViewById(R.id.bnt2);
        txtname = findViewById(R.id.txt2);
        txtcountty = findViewById(R.id.txt3);
        txtstatus = findViewById(R.id.txt5);
        txttemp = findViewById(R.id.txt4);
        txthumi = findViewById(R.id.txt6);
        txtcloud = findViewById(R.id.txt7);
        txtwind = findViewById(R.id.txt8);
        txtdate = findViewById(R.id.txt9);
        imgicon = findViewById(R.id.img_icon);
    }
}
