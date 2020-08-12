package com.example.newsapp;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
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

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Objects;

public class NewsList extends AppCompatActivity {

    String country;
    String category;
    RequestQueue queue;
    ListView listView;
    ArrayList<String> titlelist;
    ArrayList<String> urllist;
    ArrayList<String> arrayList;
    ArrayList<String> urlarraylist;
    ArrayAdapter arrayAdapter;
    ActionBar actionBar;
    SwipeRefreshLayout swiperefresh;
    String url;
    Boolean sourcevalue=false;
    String source;
    int intvalue;
    int x=0;
    Boolean internet;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_list);
        listView = findViewById(R.id.listView);
        swiperefresh = findViewById(R.id.swiperefresh);
        actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);
        arrayList = new ArrayList<>();
        urlarraylist=new ArrayList<>();
        arrayAdapter = new ArrayAdapter(arrayList,this);
        final Intent intent = getIntent();
        sourcevalue=intent.getBooleanExtra("sourcevalue",false);
        intvalue=intent.getIntExtra("intvalue",0);
        if(intvalue!=1) {
            Toast.makeText(this, "Long press on the article to save", Toast.LENGTH_SHORT).show();
        }
        final Handler handler=new Handler();
        internet=checkConnection();
        SharedPreferences getsharedpref = getSharedPreferences("savedarticle", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = getsharedpref.getString("items", " ");
        String json1 = getsharedpref.getString("urls", " ");
        Type type = new TypeToken<ArrayList<String>>() {}.getType();
        titlelist = gson.fromJson(json, type);
        urllist=gson.fromJson(json1,type);
        if(titlelist==null)
        {
            titlelist=new ArrayList<>();
        }
        if(urllist==null)
        {
            urllist=new ArrayList<>();
        }
        if(!sourcevalue) {
            if(intvalue==0)
            {
                if(internet) {
                    final ProgressDialog dialog = ProgressDialog.show(NewsList.this, "",
                            "Loading. Please wait...", true);
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            dialog.dismiss();
                        }
                    }, 1500);
                    country = intent.getStringExtra("countrycode");
                    category = intent.getStringExtra("category");
                    SharedPreferences sharedPreferences = getSharedPreferences("demo", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("country", country);
                    editor.putString("category", category);
                    editor.apply();
                    switch (country) {
                        case "in":
                            actionBar.setTitle("India - " + category.replaceFirst(String.valueOf(category.charAt(0)), String.valueOf((char) (category.charAt(0) - 32))) + " News");
                            break;
                        case "us":
                            actionBar.setTitle("USA - " + category.replaceFirst(String.valueOf(category.charAt(0)), String.valueOf((char) (category.charAt(0) - 32))) + " News");
                            break;
                        case "au":
                            actionBar.setTitle("Australia - " + category.replaceFirst(String.valueOf(category.charAt(0)), String.valueOf((char) (category.charAt(0) - 32))) + " News");
                            break;
                        case "ru":
                            actionBar.setTitle("Russia - " + category.replaceFirst(String.valueOf(category.charAt(0)), String.valueOf((char) (category.charAt(0) - 32))) + " News");
                            break;
                        case "fr":
                            actionBar.setTitle("France - " + category.replaceFirst(String.valueOf(category.charAt(0)), String.valueOf((char) (category.charAt(0) - 32))) + " News");
                            break;
                        default:
                            actionBar.setTitle("UK - " + category.replaceFirst(String.valueOf(category.charAt(0)), String.valueOf((char) (category.charAt(0) - 32))) + " News");
                            break;
                    }
                }
                else
                {
                    new AlertDialog.Builder(this)
                            .setIcon(R.drawable.nointernet)
                            .setTitle("Oops! There seems to be an error")
                            .setMessage("Please connect to the internet and retry.")
                            .setPositiveButton("RETRY", new DialogInterface.OnClickListener() {
                                @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    finish();
                                    startActivity(getIntent());
                                }

                            })
                            .setNegativeButton("GO BACK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent back = new Intent(NewsList.this, NewstypeActivity.class);
                                    if(sourcevalue) {
                                        back.putExtra("sources", true);
                                    }
                                    else
                                    {
                                        back.putExtra("intvalue",1);
                                        back.putExtra("sources",false );
                                    }
                                    startActivity(back);
                                }
                            })
                            .setCancelable(false)
                            .show();
                }
            }
            else
            {
                SharedPreferences getshared=getSharedPreferences("demo",MODE_PRIVATE);
                country=getshared.getString("country"," ");
                category=getshared.getString("category"," ");
                assert category != null;
                switch (country) {
                    case "in":
                        actionBar.setTitle("India - " + category.replaceFirst(String.valueOf(category.charAt(0)), String.valueOf((char) (category.charAt(0) - 32))) + " News");
                        break;
                    case "us":
                        actionBar.setTitle("USA - " + category.replaceFirst(String.valueOf(category.charAt(0)), String.valueOf((char) (category.charAt(0) - 32))) + " News");
                        break;
                    case "au":
                        actionBar.setTitle("Australia - " + category.replaceFirst(String.valueOf(category.charAt(0)), String.valueOf((char) (category.charAt(0) - 32))) + " News");
                        break;
                    case "ru":
                        actionBar.setTitle("Russia - " + category.replaceFirst(String.valueOf(category.charAt(0)), String.valueOf((char) (category.charAt(0) - 32))) + " News");
                        break;
                    case "fr":
                        actionBar.setTitle("France - " + category.replaceFirst(String.valueOf(category.charAt(0)), String.valueOf((char) (category.charAt(0) - 32))) + " News");
                        break;
                    default:
                        actionBar.setTitle("UK - " + category.replaceFirst(String.valueOf(category.charAt(0)), String.valueOf((char) (category.charAt(0) - 32))) + " News");
                        break;
                }
            }
            url = "https://saurav.tech/NewsAPI/top-headlines/category/" + category + "/" + country + ".json";
        }
        else
        {
            if(intvalue==0)
            {
                if(internet) {
                    source = intent.getStringExtra("source");
                    SharedPreferences sharedPreferences = getSharedPreferences("demo", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("source", source);
                    editor.apply();
                    switch (source) {
                        case "cnn":
                            actionBar.setTitle(source.toUpperCase() + "-NEWS");
                            break;
                        case "fox-news":
                            actionBar.setTitle(source.toUpperCase());
                            break;
                        case "bbc-news":
                            actionBar.setTitle(source.toUpperCase());
                            break;
                        default:
                            actionBar.setTitle(source.toUpperCase());
                            break;
                    }
                    final ProgressDialog dialog = ProgressDialog.show(NewsList.this, "",
                            "Loading. Please wait...", true);
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            dialog.dismiss();
                        }
                    }, 1500);
                }
                else
                {
                    new AlertDialog.Builder(this)
                            .setIcon(R.drawable.nointernet)
                            .setTitle("Oops! There seems to be an error.")
                            .setMessage("Please connect to the internet and retry.")
                            .setPositiveButton("RETRY", new DialogInterface.OnClickListener() {
                                @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    finish();
                                    startActivity(getIntent());
                                }

                            })
                            .setNegativeButton("GO BACK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent back = new Intent(NewsList.this, NewstypeActivity.class);
                                    if(sourcevalue) {
                                        back.putExtra("sources", true);
                                    }
                                    else
                                    {
                                        back.putExtra("intvalue",1);
                                        back.putExtra("sources",false );
                                    }
                                    startActivity(back);
                                }
                            })
                            .setCancelable(false)
                            .show();
                }
            }
            else
            {
                SharedPreferences getshared=getSharedPreferences("demo",MODE_PRIVATE);
                source=getshared.getString("source"," ");
                assert source != null;
                switch (source) {
                    case "cnn":
                        actionBar.setTitle(source.toUpperCase() + "-NEWS");
                        break;
                    case "fox-news":
                        actionBar.setTitle(source.toUpperCase());
                        break;
                    case "bbc-news":
                        actionBar.setTitle(source.toUpperCase());
                        break;
                    default:
                        actionBar.setTitle(source.toUpperCase());
                        break;
                }
            }
            url =  "https://saurav.tech/NewsAPI/everything/"+source+".json";
        }
            queue = Volley.newRequestQueue(this);
            final JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        JSONArray jsonArray = response.getJSONArray("articles");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            String title = jsonObject.getString("title");
                            String url = jsonObject.getString("url");
                            Boolean urlvalue = url.contains("macupdate");
                            if (urlvalue) {
                                continue;
                            } else {
                                if (!sourcevalue) {
                                    urlarraylist.add(url);
                                    arrayList.add(title);
                                } else {
                                    urlarraylist.add(url);
                                    arrayList.add(title);
                                }
                                x++;
                            }
                        }
                        listView.setAdapter(arrayAdapter);
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
            queue.add(request);
            swiperefresh.setColorSchemeColors(getResources().getColor(android.R.color.holo_red_dark));
            swiperefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    finish();
                    startActivity(getIntent());
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            swiperefresh.setRefreshing(false);
                        }
                    }, 1500);
                }
            });


    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater=getMenuInflater();
        menuInflater.inflate(R.menu.menuitem,menu);
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
            View v= LayoutInflater.from(getApplicationContext()).inflate(R.layout.newscardview,null);
            TextView textView=v.findViewById(R.id.textView);
            textView.setText(arrayListfiltered.get(position));
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent1=new Intent(NewsList.this,Webcontent.class);
                    intent1.putExtra("url",url);
                    intent1.putExtra("contenturl",urlarraylist.get(arrayList.indexOf(arrayListfiltered.get(position))));
                    startActivity(intent1);
                }
            });
            v.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    new AlertDialog.Builder(NewsList.this)
                            .setIcon(R.drawable.save)
                            .setTitle("Do you want to save this article?")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    int f=0;
                                    SharedPreferences sharedPreferences=getSharedPreferences("savedarticle",MODE_PRIVATE);
                                    SharedPreferences.Editor editor=sharedPreferences.edit();
                                    for(int i=0;i<titlelist.size();i++)
                                    {
                                        if(arrayListfiltered.get(position).equals(titlelist.get(i)))
                                        {
                                            f=1;
                                        }

                                    }
                                    if(f==1)
                                    {
                                        Toast.makeText(getApplicationContext(),"Article already saved", Toast.LENGTH_SHORT).show();
                                    }
                                    else
                                    {
                                        titlelist.add(arrayListfiltered.get(position));
                                        urllist.add(urlarraylist.get(arrayList.indexOf(arrayListfiltered.get(position))));
                                        Toast.makeText(getApplicationContext(),"Article saved", Toast.LENGTH_SHORT).show();
                                    }
                                    Gson gson=new Gson();
                                    String json=gson.toJson(titlelist);
                                    String json1=gson.toJson(urllist);
                                    editor.putString("items",json);
                                    editor.putString("urls",json1);
                                    editor.apply();
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
            Filter filter= new Filter() {
                @Override
                protected FilterResults performFiltering(CharSequence constraint) {
                    FilterResults filterResults=new FilterResults();
                    if(constraint==null||constraint.length()==0)
                    {
                        filterResults.count=arrlist.size();
                        filterResults.values=arrlist;
                    }
                    else
                    {
                        String searchstr=constraint.toString().toLowerCase();
                        ArrayList<String> resultdata=new ArrayList<>();
                        for(int i=0;i<arrlist.size();i++)
                        {
                            if(arrlist.get(i).toLowerCase().contains(searchstr))
                            {
                                resultdata.add(arrlist.get(i));
                            }
                            filterResults.count=resultdata.size();
                            filterResults.values=resultdata;
                        }
                    }
                    return filterResults;
                }
                @Override
                protected void publishResults(CharSequence constraint, FilterResults results) {
                        arrayListfiltered=(ArrayList<String>)results.values;
                        notifyDataSetChanged();
                }
            };
            return filter;
        }
    }
    private boolean checkConnection()
    {
        ConnectivityManager connectivityManager=(ConnectivityManager)getApplicationContext().getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo=connectivityManager.getActiveNetworkInfo();
        if(networkInfo==null) {
            return false;
        }
        else
        {
            return true;
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
            case android.R.id.home:
                // app icon in action bar clicked; go home
                Intent back = new Intent(this, NewstypeActivity.class);
                back.putExtra("sources", sourcevalue);
                back.putExtra("intvalue",1);
                startActivity(back);
                startActivity(back);
                return true;
            case R.id.search:
                return true;
            case R.id.saved:
                boolean source=false;
                Intent saved=new Intent(this,SavedArticles.class);
               if(url.contains("everything"))
                {
                    source=true;
                }
                saved.putExtra("sourcevalue",source);
                saved.putExtra("newsurl",url);
                startActivity(saved);
                return true;
            default:
                return false;
        }
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK)
        {
            Intent back = new Intent(this, NewstypeActivity.class);
                back.putExtra("sources", sourcevalue);
                back.putExtra("intvalue",1);
            startActivity(back);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
