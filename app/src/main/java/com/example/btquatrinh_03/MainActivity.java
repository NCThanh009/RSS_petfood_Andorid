package com.example.btquatrinh_03;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ListView listFood;
    ArrayList<String> arrayFood;
    ArrayAdapter<String> arrayAdap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listFood = findViewById(R.id.listFood);
        arrayFood = new ArrayList<>();
        arrayFood.add("Dog and Cat Food Nutrition");
        arrayFood.add("Pet Food Recalls");
        arrayFood.add("Pet Food Safety");
        arrayFood.add("Podcast");
        arrayFood.add("Pet Food Processing");
        arrayAdap = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, arrayFood);
        listFood.setAdapter(arrayAdap);

        listFood.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(position==0){
                    Intent intent= new Intent(MainActivity.this, PetFood.class);
                    intent.putExtra("link","https://www.petfoodindustry.com/rss/topic/450-dog-and-cat-food-nutrition");
                    intent.putExtra("nameList",arrayFood.get(position));
                    startActivity(intent);
                }else if (position==1){
                    Intent intent= new Intent(MainActivity.this, PetFood.class);
                    intent.putExtra("link","https://www.petfoodindustry.com/rss/topic/253-pet-food-recalls");
                    intent.putExtra("nameList",arrayFood.get(position));
                    startActivity(intent);
                }
                else if (position==2){
                    Intent intent= new Intent(MainActivity.this, PetFood.class);
                    intent.putExtra("link","https://www.petfoodindustry.com/rss/topic/229-pet-food-safety");
                    intent.putExtra("nameList",arrayFood.get(position));
                    startActivity(intent);
                }
                else if (position==3){
                    Intent intent= new Intent(MainActivity.this, PetFood.class);
                    intent.putExtra("link","https://www.petfoodindustry.com/rss/topic/581-podcast");
                    intent.putExtra("nameList",arrayFood.get(position));
                    startActivity(intent);
                }
                else if (position==4){
                    Intent intent= new Intent(MainActivity.this, PetFood.class);
                    intent.putExtra("link","https://www.petfoodindustry.com/rss/topic/233-pet-food-processing");
                    intent.putExtra("nameList",arrayFood.get(position));
                    startActivity(intent);
                }
            }
        });
    }
}