package com.example.newsapp;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class FirstActivity extends AppCompatActivity {
    GridView gridView;
    ArrayList<String> arrayList=new ArrayList<>();
    int[] images ={R.drawable.globe,R.drawable.source,R.drawable.bookmark};
    Adapter adapter;
    ActionBar actionBar;
    ArrayList<String> titlelist;
    ArrayList<String> urllist;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);
        gridView=findViewById(R.id.gridView);
        actionBar = getSupportActionBar();
        adapter= new Adapter();
        arrayList.add("GLOBAL NEWS");
        arrayList.add("NEWS SOURCES");
        arrayList.add("BOOKMARKED");
        gridView.setAdapter(adapter);
        actionBar.setTitle("Daily Press");
        actionBar.setElevation(1);
        SharedPreferences getsharedpref = getSharedPreferences("savedarticle", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = getsharedpref.getString("items", " ");
        String json1 = getsharedpref.getString("urls", " ");
        Type type = new TypeToken<ArrayList<String>>() {
        }.getType();
        titlelist = gson.fromJson(json, type);
        urllist = gson.fromJson(json1, type);
        if (titlelist == null) {
            titlelist = new ArrayList<>();
        }
        if (urllist == null) {
            urllist = new ArrayList<>();
        }
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(position==0)
                {
                    Intent intent1=new Intent(FirstActivity.this,MainActivity.class);
                    startActivity(intent1);
                }
                else if(position==1)
                {
                    Intent intent1=new Intent(FirstActivity.this,NewstypeActivity.class);
                    intent1.putExtra("sources",true);
                    startActivity(intent1);
                }
                else
                {
                    SharedPreferences sharedPreferences = getSharedPreferences("savedarticle", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    Gson gson = new Gson();
                    String json = gson.toJson(titlelist);
                    String json1 = gson.toJson(urllist);
                    editor.putString("items", json);
                    editor.putString("urls", json1);
                    editor.apply();
                    Intent intent1=new Intent(FirstActivity.this,SavedArticles.class);
                    startActivity(intent1);
                }
            }
        });
    }
    class Adapter extends BaseAdapter {

        @Override
        public int getCount() {
            return arrayList.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View v= LayoutInflater.from(getApplicationContext()).inflate(R.layout.sources,null);
            TextView textView=v.findViewById(R.id.nametextView);
            ImageView imageView=v.findViewById(R.id.imageView);
            imageView.setImageResource(images[position]);
            textView.setText(arrayList.get(position));
            return v;
        }
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK)
        {
            new AlertDialog.Builder(this)
                    .setIcon(R.drawable.exit)
                    .setTitle("Are you sure you want to exit")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finishAffinity();
                        }

                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .setCancelable(false)
                    .show();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
