package com.example.newsapp;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class NewstypeActivity extends AppCompatActivity {
    GridView gridView;
    ArrayList<String> arrayList=new ArrayList<>();
    Adapter adapter;
    ActionBar actionBar;
    String x;
    Boolean sources;
    int p;
    int[] images ={R.drawable.entertainment, R.drawable.general, R.drawable.health, R.drawable.science, R.drawable.sports, R.drawable.technology};
    int[] images1 ={R.drawable.bbc,R.drawable.cnn,R.drawable.fox,R.drawable.google};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newstype);
        final Intent intent=getIntent();
        Intent back=getIntent();
        sources=intent.getBooleanExtra("sources",false);
        p=back.getIntExtra("intvalue",0);
        gridView=findViewById(R.id.gridView);
        actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);
        adapter=new Adapter();
        if(!sources)
        {
            actionBar.setTitle("Category");
            if(p!=1)
            {
                Toast.makeText(this, "Select a Category", Toast.LENGTH_SHORT).show();
                x=intent.getStringExtra("country");
                SharedPreferences sharedPreferences=getSharedPreferences("demo",MODE_PRIVATE);
                SharedPreferences.Editor editor=sharedPreferences.edit();
                editor.putString("code",x);
                editor.apply();

            }
            SharedPreferences getshared=getSharedPreferences("demo",MODE_PRIVATE);
            x=getshared.getString("code"," ");
            arrayList.add("ENTERTAINMENT");
            arrayList.add("GENERAL");
            arrayList.add("HEALTH");
            arrayList.add("SCIENCE");
            arrayList.add("SPORTS");
            arrayList.add("TECHNOLOGY");
            gridView.setAdapter(adapter);
        }
        else
        {
            actionBar.setTitle("Source");
            if(p!=1) {
                Toast.makeText(this, "Select a Source", Toast.LENGTH_SHORT).show();
            }
            arrayList.add("BBC-NEWS");
            arrayList.add("CNN-NEWS");
            arrayList.add("FOX-NEWS");
            arrayList.add("GOOGLE-NEWS");
            gridView.setAdapter(adapter);
        }
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent1=new Intent(NewstypeActivity.this,NewsList.class);
                if(!sources)
                {
                    intent1.putExtra("countrycode",x);
                    intent1.putExtra("category",arrayList.get(position).toLowerCase());
                }
                else
                {
                    if(arrayList.get(position).equals("CNN-NEWS"))
                    {
                        intent1.putExtra("source","cnn");
                    }
                    else
                    {
                        intent1.putExtra("source",arrayList.get(position).toLowerCase());
                    }
                }
                intent1.putExtra("sourcevalue",sources);
                startActivity(intent1);
            }
        });
    }
    class Adapter extends BaseAdapter{

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
            View v=LayoutInflater.from(getApplicationContext()).inflate(R.layout.gridcardview,null);
            TextView textView=v.findViewById(R.id.nametextView);
            ImageView imageView=v.findViewById(R.id.imageView);
            if(sources)
            {
                imageView.setImageResource(images1[position]);
            }
            else
            {
                imageView.setImageResource(images[position]);
            }
            textView.setText(arrayList.get(position));
            return v;
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent back;
                // app icon in action bar clicked; go home
                if(sources)
                {
                    back = new Intent(this, FirstActivity.class);
                }
                else
                {
                    back = new Intent(this, MainActivity.class);
                    back.putExtra("intvalue",1);
                }
                startActivity(back);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK)
        {
            Intent back;
            if(sources)
            {
                back = new Intent(this, FirstActivity.class);
            }
            else
            {
                back = new Intent(this, MainActivity.class);
                back.putExtra("intvalue",1);
            }
            startActivity(back);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}

