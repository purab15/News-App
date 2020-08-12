package com.example.newsapp;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ListView listView;
    ArrayList<String> list=new ArrayList<>();
    int[] images ={R.drawable.india,R.drawable.usa,R.drawable.aus,R.drawable.russia,R.drawable.france,R.drawable.uk};
    MyAdapter myadapter;
    ActionBar actionBar;
    int intvalue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView=findViewById(R.id.listView);
        actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);
        myadapter=new MyAdapter();
        actionBar.setTitle("Country");
        Intent intent1=getIntent();
        intvalue=intent1.getIntExtra("intvalue",0);
        if(intvalue!=1) {
            Toast.makeText(this, "Select a Country", Toast.LENGTH_SHORT).show();
        }
        list.add("INDIA");
        list.add("USA");
        list.add("AUSTRALIA");
        list.add("RUSSIA");
        list.add("FRANCE");
        list.add("UNITED KINGDOM");
        listView.setAdapter(myadapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=new Intent(MainActivity.this,NewstypeActivity.class);
                if(position==0)
                {
                    intent.putExtra("country","in");
                }
                else if(position==1)
                {
                    intent.putExtra("country","us");
                }
                else if(position==2)
                {
                    intent.putExtra("country","au");
                }
                else if(position==3)
                {
                    intent.putExtra("country","ru");
                }
                else if(position==4)
                {
                    intent.putExtra("country","fr");
                }
                else
                {
                    intent.putExtra("country","gb");
                }
                startActivity(intent);
            }
        });

    }
    class MyAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return list.size();
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
            View v=LayoutInflater.from(getApplicationContext()).inflate(R.layout.cardview,null);
            TextView textView=v.findViewById(R.id.nametextView);
            ImageView imageView=v.findViewById(R.id.imageView);
            imageView.setImageResource(images[position]);
            textView.setText(list.get(position));
            return v;
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // app icon in action bar clicked; go home
                Intent back = new Intent(this, FirstActivity.class);
                back.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
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
            Intent back = new Intent(this, FirstActivity.class);
            back.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(back);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}