package com.example.mebap;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    TextView txt, txt1, time;
    Button btn;
    private RequestQueue mQueue;
    String url = "https://open.neis.go.kr/hub/mealServiceDietInfo?&Type=json";
    //    String urls = "https://open.neis.go.kr/hub/mealServiceDietInfo?&Type=json";
    String key = "a7cc721e31ef4e5199636b84dd243813";
    String areaCode = "B10"; //서울
    String schoolCode = "7010569"; //미림여자정보과학고등학교 학교 코드
    int urlDays = getUrlDate();
    String newUrl = url + "&KEY=" + key + "&ATPT_OFCDC_SC_CODE=" + areaCode + "&SD_SCHUL_CODE=" + schoolCode + "&MLSV_YMD=" + urlDays + "&pIndex=1&pSize=10"; // 파싱할 json파일이 있는 url



//    ArrayAdapter adapter;

    // 영화 제목을 담을 ArrayList 변수(items) 선언

    ArrayList<String> items = new ArrayList<String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("미림 급식앱");

        txt = findViewById(R.id.txt);
        txt1 = findViewById(R.id.txt1);
        btn = findViewById(R.id.btn);
        time = findViewById(R.id.time);
        mQueue = Volley.newRequestQueue(this);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                jsonParse();
                getDate();
                Log.e("새로운 링크",newUrl);
            }
        });
    }


    private void jsonParse(){


        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, newUrl , null,
                new Response.Listener<JSONObject>() {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray  = response.getJSONArray("mealServiceDietInfo");
                            JSONObject jsonRows = jsonArray.getJSONObject(1); // row가져오기 {"row":[{...
                            JSONArray jsonRow = jsonRows.getJSONArray("row"); // [{"...
                            int minute = getMinute();
                            JSONObject json1;
                            if(18 * 60 + 10 <= minute)
                                json1 = jsonRow.getJSONObject(0); // {"... 0: 아침 / 1: 중식 / 2:석식             // 시간대별로 보여주는 급식
                            else if(13*60+10 <= minute)
                                json1 = jsonRow.getJSONObject(2);
                            else if(7*60+30 <= minute)
                                json1 = jsonRow.getJSONObject(1);
                            else
                                json1 = jsonRow.getJSONObject(0);

                            String school = json1.getString("DDISH_NM");
                            school = school.replace("<br/>", "\n");
//                                Log.e("test", school.toString());
                            txt.setText(school.toString());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        mQueue.add(request);
    }

    private void getDate(){
        Date currentTime = Calendar.getInstance().getTime();

        String format_hh_mm = "aa hh:mm"; // am, pm 표시
//        String format_hh_mm = "kk:mm"; //24시간제
        String format_mm_dd = "MM월 dd일";

        SimpleDateFormat format = new SimpleDateFormat(format_hh_mm, Locale.getDefault());
        SimpleDateFormat format1 = new SimpleDateFormat(format_mm_dd, Locale.getDefault());
        String formatTime = format.format(currentTime); // 현재 시간 가져오기
        String formatDay  = format1.format(currentTime); // 현재 일 가져오기

//        txt1.setText(formatTime.toString());
        time.setText(formatTime.toString());
    }

    private int getUrlDate(){ //url에 들어가는 날짜를 구하는 메서드
        Date currentTime = Calendar.getInstance().getTime();
        String format_yy_mm_dd = "yyyyMMdd";
        SimpleDateFormat format2 = new SimpleDateFormat(format_yy_mm_dd, Locale.getDefault());
        String formatDays = format2.format(currentTime); // 파싱할 데이터 넣기
        int intDays = Integer.parseInt(String.valueOf(formatDays));

        return intDays;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private int getMinute(){
        LocalTime now = LocalTime.now();

        int hour = now.getHour();
        int minute = now.getMinute();
        int minutes = (hour*60) + minute;

        return minutes;
    }

}