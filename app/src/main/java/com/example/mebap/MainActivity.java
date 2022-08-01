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
    // https://open.neis.go.kr/hub/mealServiceDietInfo?&Type=json&KEY=a7cc721e31ef4e5199636b84dd243813&ATPT_OFCDC_SC_CODE=B10&SD_SCHUL_CODE=7010569&MLSV_YMD=20220728&pIndex=1&pSize=10
    String url = "https://open.neis.go.kr/hub/mealServiceDietInfo?&Type=json";
    //    String urls = "https://open.neis.go.kr/hub/mealServiceDietInfo?&Type=json";
    String key = "a7cc721e31ef4e5199636b84dd243813";
    String areaCode = "B10"; //서울
    String schoolCode = "7010569"; //미림여자정보과학고등학교
    int urlDays = getUrlDate();
    String newUrl = url + "&KEY=" + key + "&ATPT_OFCDC_SC_CODE=" + areaCode + "&SD_SCHUL_CODE=" + schoolCode + "&MLSV_YMD=" + urlDays + "&pIndex=1&pSize=10";



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

        String format_hh_mm = "hh:mm";
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


//        try {
//            // 인증키
////            String serviceKey = "인증키값";
//
//            String urlStr = "https://open.neis.go.kr/hub/mealServiceDietInfo?KEY=a7cc721e31ef4e5199636b84dd243813&Type=json&pIndex=1&pSize=10&ATPT_OFCDC_SC_CODE=B10&SD_SCHUL_CODE=7010569";
////            urlStr += "?"+ URLEncoder.encode("ServiceKey","UTF-8") +"=" + serviceKey;
////            urlStr += "&"+ URLEncoder.encode("numOfRows","UTF-8") +"=200";
////            urlStr += "&"+ URLEncoder.encode("pageNo","UTF-8") +"=1";
////            urlStr += "&"+ URLEncoder.encode("year","UTF-8") +"=2019";
////            urlStr += "&"+ URLEncoder.encode("_returnType","UTF-8") +"=json";
//
//            URL url = new URL(urlStr);
//
//            String line = "";
//            String result = "";
//
//            BufferedReader br;
//            br = new BufferedReader(new InputStreamReader(url.openStream()));
//            while ((line = br.readLine()) != null) {
//                result = result.concat(line);
//                //System.out.println(line);
//            }
//
//            // JSON parser 만들어 문자열 데이터를 객체화한다.
//            JSONParser parser = new JSONParser();
//            JSONObject obj = (JSONObject)parser.parse(result);
//
//            // list 아래가 배열형태로
//            // {"list" : [ {"returnType":"json","clearDate":"--",.......} ]
//            JSONArray parse_listArr = (JSONArray)obj.get("list");
//
//            String miseType = "";
//                // 객체형태로
//            // {"returnType":"json","clearDate":"--",.......},...
//            for (int i=0;i< parse_listArr.size();i++) {
//                JSONObject weather = (JSONObject) parse_listArr.get(i);
//                String dataDate = (String) weather.get("dataDate");            // 발령날짜
//                String districtName = (String) weather.get("districtName");    // 발령지역
//                String moveName = (String) weather.get("moveName");            // 발령권역
//                String issueDate = (String) weather.get("issueDate");        // 발령일자
//                String issueTime = (String) weather.get("issueTime");        // 발령시간
//                String issueVal  = (String) weather.get("issueVal");        // 발령농도
//                String itemCode  = (String) weather.get("itemCode");        // 미세먼지 구분 PM10, PM25
//                String issueGbn  = (String) weather.get("issueGbn");        // 경보단계 : 주의보/경보
//                String clearDate = (String) weather.get("clearDate");        // 해제일자
//                String clearTime = (String) weather.get("clearTime");        // 해제시간
//                String clearVal = (String) weather.get("clearVal");            // 해제시 미세먼지농도
//
//                if (itemCode.equals("PM10")) {
//                    miseType = "";
//                } else if (itemCode.equals("PM25")) {
//                    miseType = "초미세먼지";
//                }
//                StringBuffer sb = new StringBuffer();
//                sb.append("발령날짜 : " + dataDate + ", 지역 : " + districtName + " ("+ moveName +"), " + "발령시간 : "+ issueDate + " " + issueTime + ", 농도 : " + issueVal + " ("+ issueGbn +") " + miseType);
//                System.out.println(sb.toString());
//            }
//
//            br.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        InputStream is = null;

//        try {
//            is = new URL(url).openStream();
//            BufferedReader rd = new BufferedReader(new InputStreamReader(is, "UTF-8"));
//            String str;
//            StringBuffer buffer = new StringBuffer();
//            while ((str = rd.readLine()) != null) {
//                buffer.append(str);
//            }
//            String receiveMsg = buffer.toString();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
}