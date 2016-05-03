package com.appintroexample;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.ecometr.app.R;
import com.example.skoml.bioindication.MenuActivity;
import com.github.paolorotolo.appintro.AppIntro2;

public class SecondLayoutIntro extends AppIntro2 {
    @Override
    public void init(Bundle savedInstanceState) {
        addSlide(SampleSlide.newInstance(R.layout.intro_1));
        addSlide(SampleSlide.newInstance(R.layout.intro_2));
        addSlide(SampleSlide.newInstance(R.layout.intro_3));
    }

    private void loadMainActivity(){
        Intent intent = new Intent(this, MenuActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onDonePressed() {
        loadMainActivity();
    }

    public void getStarted(View v){
        loadMainActivity();
    }

    @Override
    public void onSlideChanged() {
       // loadMainActivity();
    }

    @Override
    public void onNextPressed() {
        // Do something when users tap on Next button.
    }

}
