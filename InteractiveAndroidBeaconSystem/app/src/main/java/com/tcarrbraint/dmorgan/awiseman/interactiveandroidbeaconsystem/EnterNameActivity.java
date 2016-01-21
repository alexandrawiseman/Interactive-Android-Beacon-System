package com.tcarrbraint.dmorgan.awiseman.interactiveandroidbeaconsystem;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;

public class EnterNameActivity extends Activity{

    private EditText mNameEditText;
    private Button mSubmitName;
    private int studentID = 0;
    private String JSON_STRING = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_name);

        mNameEditText = (EditText) findViewById(R.id.name_editText);
        mSubmitName = (Button) findViewById(R.id.submitname);
        mSubmitName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!mNameEditText.getText().toString().matches(""))
                {
                    addStudent();
                    getJSON();
                }
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

        final String name = mNameEditText.getText().toString().trim();

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
                params.put(Config.KEY_EMP_DESG,"");
                String put = params.put(Config.KEY_EMP_SAL, "0");

                RequestHandler rh = new RequestHandler();
                String res = rh.sendPostRequest(Config.URL_ADD, params);
                return res;
            }
        }

        AddStudent ae = new AddStudent();
        ae.execute();
    }


    private void showID(){
        JSONObject jsonObject = null;
        try
        {
            jsonObject = new JSONObject(JSON_STRING);
            JSONArray result = jsonObject.getJSONArray(Config.TAG_JSON_ARRAY);
            JSONObject jo = result.getJSONObject(0);
            String id = jo.getString(Config.TAG_ID);
            studentID = Integer.parseInt(id.trim());
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }

        Intent pickerIntent = new Intent(EnterNameActivity.this, GamePickerActivity.class);
        Log.d("EnterNameActivity", "Student ID: " + studentID);
        startActivity(pickerIntent);
    }

    private void getJSON(){
        class GetJSON extends AsyncTask<Void,Void,String>{

            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(EnterNameActivity.this,"Loading Games...","Wait...",false,false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                JSON_STRING = s;
                showID();
            }

            @Override
            protected String doInBackground(Void... params) {
                RequestHandler rh = new RequestHandler();
                String s = rh.sendGetRequest(Config.URL_GET_ID);
                return s;
            }
        }
        GetJSON gj = new GetJSON();
        gj.execute();
    }
}
