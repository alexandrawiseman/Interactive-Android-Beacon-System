package com.tcarrbraint.dmorgan.awiseman.interactiveandroidbeaconsystem;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.HashMap;

public class EnterNameActivity extends Activity{

    private EditText editTextName;
    private EditText editTextLoc;
    private EditText editTextScore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_name);

        editTextName = (EditText) findViewById(R.id.editText);

        Button submitname = (Button) findViewById(R.id.submitname);
        submitname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Bluetooth operated menu selects game to play
                addStudent();
                //GetID
                Intent pickerIntent = new Intent(EnterNameActivity.this, GamePickerActivity.class);
                startActivity(pickerIntent);
            }
        });

        Button backbutton = (Button) findViewById(R.id.backbutton);
        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void addStudent(){

        final String name = editTextName.getText().toString().trim();

        class AddStudent extends AsyncTask<Void,Void,String>
        {

            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(EnterNameActivity.this,"Adding...","Wait...",false,false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
            }

            @Override
            protected String doInBackground(Void... v) {
                HashMap<String,String> params = new HashMap<>();
                params.put(Config.KEY_EMP_NAME,name);
                params.put(Config.KEY_EMP_DESG,"Z");
                String put = params.put(Config.KEY_EMP_SAL, "0");

                RequestHandler rh = new RequestHandler();
                String res = rh.sendPostRequest(Config.URL_ADD, params);
                return res;
            }
        }

        AddStudent ae = new AddStudent();
        ae.execute();
    }

}
