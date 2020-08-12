package com.example.newsapp;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Objects;

public class SavedArticles extends AppCompatActivity {
    ListView listView;
    ArrayAdapter arrayAdapter;
    ActionBar actionBar;
    Boolean source;
    Intent intent1;
    int intvalue;
    ArrayList<String> titlelist;
    ArrayList<String> urllist;
    String newsurl;
    TextView noarticlestextview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_articles);
        listView = findViewById(R.id.listView);
        noarticlestextview = findViewById(R.id.noarticlestextView);
        actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Saved Articles");
        intent1 = getIntent();
        intvalue = intent1.getIntExtra("intvalue", 0);
        newsurl = intent1.getStringExtra("newsurl");
        source = intent1.getBooleanExtra("sourcevalue", false);
        SharedPreferences getsharedpref = getSharedPreferences("savedarticle", MODE_PRIVATE);
        Gson gson = new Gson();
        noarticlestextview.setVisibility(View.INVISIBLE);
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
        if(titlelist.isEmpty()&&urllist.isEmpty())
        {
            noarticlestextview.setVisibility(View.VISIBLE);
            listView.setVisibility(View.INVISIBLE);
        }
        else
        {
            if(intvalue!=1) {
                Toast.makeText(this, "Long press on the article to delete", Toast.LENGTH_SHORT).show();
            }
        }
        arrayAdapter = new ArrayAdapter(titlelist, this);
        listView.setAdapter(arrayAdapter);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater=getMenuInflater();
        menuInflater.inflate(R.menu.savedmenusearch,menu);
        MenuItem item=menu.findItem(R.id.search);
        SearchView searchView=(SearchView) item.getActionView();
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    arrayAdapter.getFilter().filter(newText);
                    return true;
                }
            });
        searchView.setOnKeyListener(new View.OnKeyListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {
                    InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(Objects.requireNonNull(getCurrentFocus()).getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                }
                return true;
            }
        });
        return true;
    }
    class ArrayAdapter extends BaseAdapter implements Filterable {
        private ArrayList<String> arrlist;
        private ArrayList<String> arrayListfiltered;
        private Context context;

        public ArrayAdapter(ArrayList<String> arrlist, Context context) {
            this.arrlist = arrlist;
            this.arrayListfiltered = arrlist;
            this.context = context;
        }

        @Override
        public int getCount() {
            return arrayListfiltered.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            View v = LayoutInflater.from(getApplicationContext()).inflate(R.layout.newscardview, null);
            TextView textView = v.findViewById(R.id.textView);
            textView.setText(arrayListfiltered.get(position));
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        Intent intent = new Intent(SavedArticles.this, Webcontent.class);
                        intent.putExtra("contenturl", urllist.get(titlelist.indexOf(arrayListfiltered.get(position))));
                        intent.putExtra("zvalue", 1);
                        if(newsurl!=null) {
                            intent.putExtra("url", newsurl);
                        }
                        startActivity(intent);
                    }catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
            });
            v.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    final int pos=titlelist.indexOf(arrayListfiltered.get(position));
                    new AlertDialog.Builder(SavedArticles.this)
                            .setIcon(R.drawable.delete)
                            .setTitle("Are you sure you want to delete this article?")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                        titlelist.remove(pos);
                                        arrayAdapter.notifyDataSetChanged();
                                    urllist.remove(pos);
                                    if(titlelist.isEmpty()&&urllist.isEmpty())
                                    {
                                        noarticlestextview.setVisibility(View.VISIBLE);
                                        listView.setVisibility(View.INVISIBLE);
                                    }
                                        SharedPreferences sharedPreferences = getSharedPreferences("savedarticle", MODE_PRIVATE);
                                        SharedPreferences.Editor editor = sharedPreferences.edit();
                                        Gson gson = new Gson();
                                        String json = gson.toJson(titlelist);
                                        String json1 = gson.toJson(urllist);
                                        editor.putString("items", json);
                                        editor.putString("urls", json1);
                                        editor.apply();
                                        Toast.makeText(getApplicationContext(), "Article deleted", Toast.LENGTH_LONG).show();
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
            });
            return v;
        }

        @Override
        public Filter getFilter() {
            Filter filter = new Filter() {
                @Override
                protected FilterResults performFiltering(CharSequence constraint) {
                    FilterResults filterResults = new FilterResults();
                    if(!titlelist.isEmpty()&&!urllist.isEmpty()) {
                    if (constraint == null || constraint.length() == 0) {
                        filterResults.count = arrlist.size();
                        filterResults.values = arrlist;
                    } else {
                        String searchstr = constraint.toString().toLowerCase();
                            ArrayList<String> resultdata = new ArrayList<>();
                            for (int i = 0; i < arrlist.size(); i++) {
                                if (arrlist.get(i).toLowerCase().contains(searchstr)) {
                                    resultdata.add(arrlist.get(i));
                                }
                                filterResults.count = resultdata.size();
                                filterResults.values = resultdata;
                            }
                        }
                    }
                    return filterResults;
                }

                @Override
                protected void publishResults(CharSequence constraint, FilterResults results) {
                    if (!titlelist.isEmpty() && !urllist.isEmpty()) {
                        arrayListfiltered = (ArrayList<String>) results.values;
                        notifyDataSetChanged();
                    }
                }
            };
            return filter;
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // app icon in action bar clicked; go home
                Intent back = new Intent();
                if(newsurl==null)
                {
                    back = new Intent(this, FirstActivity.class);
                    back.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                }
                else {
                    back = new Intent(this, NewsList.class);
                    back.putExtra("sourcevalue", source);
                    back.putExtra("intvalue", 1);
                    back.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                }
                startActivity(back);
                return true;
            case R.id.search:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK)
        {
            Intent back = new Intent();
            if(newsurl==null)
            {
                back = new Intent(this, FirstActivity.class);
                back.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            }
            else {
                back = new Intent(this, NewsList.class);
                back.putExtra("sourcevalue", source);
                back.putExtra("intvalue", 1);
                back.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            }
            startActivity(back);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}

