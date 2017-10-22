package com.example.android.customarraylist;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    CustomArraylist<String> customArraylist;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        customArraylist = new CustomArraylist<>();

        customArraylist.add("I am ");
        customArraylist.add("kevin");
        customArraylist.add("right?");


        String first = customArraylist.get(0);

        String second = customArraylist.get(1);

        String fourth = customArraylist.get(4);



    }
}
