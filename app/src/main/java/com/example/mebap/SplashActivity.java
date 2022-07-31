package com.example.mebap;
import androidx.constraintlayout.widget.ConstraintLayout;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class SplashActivity extends Activity {
    Animation anim_du1; //애니메이션 파일을 할당할 변수
    Animation anim_du2; //애니메이션 파일을 할당할 변수
    Animation anim_du3;   //애니메이션 파일을 할당할 변수
    ConstraintLayout constraintLayout;
    ImageView o, o1, o2, o3, o4, o5, o6, o7, o8 ,o9, o10;

    // https://jhshjs.tistory.com/44 애니메이션 사용법 정리


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        constraintLayout=findViewById(R.id.constraintLayout);
        o=findViewById(R.id.o);
        o1=findViewById(R.id.o1);
        o2=findViewById(R.id.o2);
        o3=findViewById(R.id.o3);
        o4=findViewById(R.id.o4);
        o5=findViewById(R.id.o5);
        o6=findViewById(R.id.o6);
        o7=findViewById(R.id.o7);
        o8=findViewById(R.id.o8);
        o9=findViewById(R.id.o9);
        o10=findViewById(R.id.o10);


        Handler handler = new Handler();
        handler.postDelayed(new Runnable(){
            @Override
            public void run() {
                Intent intent = new Intent (getApplicationContext(), MainActivity.class);
                startActivity(intent); //인트로 실행 후 바로 MainActivity로 넘어감.
                finish();
            }
        },4500); //4.5초 후 메인 실행


        //변수 할당
        anim_du1 = AnimationUtils.loadAnimation(this,R.anim.splash_icon_version_01); //duration 1초
        anim_du2 = AnimationUtils.loadAnimation(this,R.anim.splash_icon_version_02); //duration 2초
        anim_du3 = AnimationUtils.loadAnimation(this,R.anim.splash_icon_version_03); //duration 3초

        //애니메이션 실행
        o.startAnimation(anim_du2);
        o1.startAnimation(anim_du2);
        o2.startAnimation(anim_du1);
        o3.startAnimation(anim_du1);
        o4.startAnimation(anim_du3);
        o5.startAnimation(anim_du3);
        o6.startAnimation(anim_du1);
        o7.startAnimation(anim_du1);
        o8.startAnimation(anim_du3);
        o9.startAnimation(anim_du1);
        o10.startAnimation(anim_du1);

    }


    private class Login {
    }
}