package com.tcarrbraint.dmorgan.awiseman.interactiveandroidbeaconsystem;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class TeacherActivity extends Activity implements SwipeRefreshLayout.OnRefreshListener
{

    private String JSON_STRING = "";
    private ListView mStudentListView;
    private Button mTeacherBackButton;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher);

        mTeacherBackButton = (Button) findViewById(R.id.teacher_back_button);
        mTeacherBackButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent mainMenuIntent = new Intent(TeacherActivity.this, MainMenu.class);
                mainMenuIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(mainMenuIntent);
            }
        });

        mStudentListView = (ListView) findViewById(R.id.students_listview);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        mSwipeRefreshLayout.setOnRefreshListener(TeacherActivity.this);

        mSwipeRefreshLayout.post(new Runnable()
                                 {
                                     @Override
                                     public void run()
                                     {
                                         getJSON();
                                     }
                                 }
        );
    }

    @Override
    public void onRefresh()
    {
        getJSON();
    }

    private void showStudents()
    {
        JSONObject jsonObject = null;
        ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
        try
        {
            jsonObject = new JSONObject(JSON_STRING);
            JSONArray result = jsonObject.getJSONArray(Config.TAG_JSON_ARRAY);

            for (int i = 0; i < result.length(); i++)
            {
                JSONObject jo = result.getJSONObject(i);
                String name = jo.getString(Config.TAG_NAME);
                String location = jo.getString(Config.TAG_LOC);
                String score = jo.getString(Config.TAG_SCORE);

                HashMap<String, String> students = new HashMap<>();
                students.put(Config.TAG_NAME, name);
                students.put(Config.TAG_LOC, location);
                students.put(Config.TAG_SCORE, score + "/3");
                list.add(students);
            }

        } catch (JSONException e)
        {
            e.printStackTrace();
        }

        ListViewAdapter adapter = new ListViewAdapter(this, list);

        mStudentListView.setAdapter(adapter);
    }

    private void getJSON()
    {
        mSwipeRefreshLayout.setRefreshing(true);
        class GetJSON extends AsyncTask<Void, Void, String>
        {

            ProgressDialog loading;

            @Override
            protected void onPreExecute()
            {
                super.onPreExecute();
            }

            @Override
            protected void onPostExecute(String s)
            {
                super.onPostExecute(s);
                JSON_STRING = s;
                showStudents();
            }

            @Override
            protected String doInBackground(Void... params)
            {
                RequestHandler rh = new RequestHandler();
                String s = rh.sendGetRequest(Config.URL_GET_ALL);
                return s;
            }
        }
        GetJSON gj = new GetJSON();
        gj.execute();
        mSwipeRefreshLayout.setRefreshing(false);
    }

}
