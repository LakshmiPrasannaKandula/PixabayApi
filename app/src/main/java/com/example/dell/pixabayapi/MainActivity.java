package com.example.dell.pixabayapi;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    EditText editText;
    Button  button;
    String text;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editText=findViewById(R.id.textid);
        button=findViewById(R.id.btnid);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                text=editText.getText().toString();
                if((text.equals(""))) {
                    Toast.makeText(MainActivity.this, " Please Enter ImageName ", Toast.LENGTH_SHORT).show();
                }
                else
                    {
                    if (isOnline()) {
                        Intent intent = new Intent(MainActivity.this, DetailActivity.class);
                        intent.putExtra(DetailActivity.imkey, editText.getText().toString());
                        startActivity(intent);
                    } else {

                        AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
                        alertDialog.setTitle("Info");
                        alertDialog.setMessage("Something went wrong, Please check your Internet Connections");
                        alertDialog.setIcon(android.R.drawable.ic_dialog_alert);
                        alertDialog.setCancelable(false);
                        alertDialog.show();
                    }
                }
            }
        });
    }
    private  boolean isOnline() {

        ConnectivityManager conMgr = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = conMgr.getActiveNetworkInfo();

        if(netInfo == null || !netInfo.isConnected() || !netInfo.isAvailable()){
            return false;
        }
        return true;
    }
}
