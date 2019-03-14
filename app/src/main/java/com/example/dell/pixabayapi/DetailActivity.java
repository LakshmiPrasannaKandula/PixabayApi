package com.example.dell.pixabayapi;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class DetailActivity extends AppCompatActivity  implements LoaderManager.LoaderCallbacks<String> {

    RecyclerView recyclerView;
    ProgressBar progressBar;
    String str="https://pixabay.com/api/?key=10860748-83b5347a866cfeb8e4c85c3e2&q=";
    String imnamme;
    public  static final String imkey="imagename";

    public  static final  int Loader_ID=12;
    List<Model> modelList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        recyclerView=findViewById(R.id.recyclerid);
        progressBar=findViewById(R.id.progress);
        modelList=new ArrayList<>();

        Intent intent=getIntent();
        imnamme=intent.getStringExtra(imkey);

        if(isOnline()){
            recyclerView.setLayoutManager(new GridLayoutManager(this,2));
            recyclerView.setAdapter(new DetailsAdapter(this,modelList));
            getSupportLoaderManager().initLoader(Loader_ID,null,this);
        }
        else{
            AlertDialog alertDialog=new AlertDialog.Builder(this).create();
            alertDialog.setTitle("Info");
            alertDialog.setMessage("Something went wrong, Please check your Internet Connections");
            alertDialog.setIcon(android.R.drawable.ic_dialog_alert);
            alertDialog.show();
            alertDialog.setCancelable(false);
        }
    }

    private  boolean isOnline() {

        ConnectivityManager conMgr = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = conMgr.getActiveNetworkInfo();

        if (netInfo == null || !netInfo.isConnected() || !netInfo.isAvailable()) {
            return false;
        }
        return true;

    }

    @NonNull
    @Override
    public Loader<String> onCreateLoader(int id, @Nullable Bundle args) {
        return new AsyncTaskLoader<String>(this) {

            @Override
            protected void onStartLoading() {
                super.onStartLoading();
                progressBar.setVisibility(View.VISIBLE);
                forceLoad();
            }

            @Nullable
            @Override
            public String loadInBackground() {
                try {
                    URL url=new URL(str+imnamme);
                    HttpURLConnection httpURLConnection= (HttpURLConnection) url.openConnection();
                    InputStream inputStream=httpURLConnection.getInputStream();
                    Scanner scanner=new Scanner(inputStream);
                    scanner.useDelimiter("\\A");
                    if(scanner.hasNext()){
                        return  scanner.next();
                    }
                    else {
                        return  null;
                    }
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }
        };
    }

    @Override
    public void onLoadFinished(@NonNull Loader<String> loader, String data) {

        progressBar.setVisibility(View.GONE);
        try {
            JSONObject jsonObject=new JSONObject(data);
            JSONArray  jsonArray=jsonObject.getJSONArray("hits");
            for(int i=0;i<jsonArray.length();i++){
                JSONObject imageinfo=jsonArray.getJSONObject(i);
                String image=imageinfo.getString("largeImageURL");
                modelList.add(new Model(image));
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onLoaderReset(@NonNull Loader<String> loader) {

    }

}
