package com.immagine.workok.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.immagine.workok.Constants;
import com.immagine.workok.PreferencesUtil;
import com.immagine.workok.R;
import com.immagine.workok.adapter.ProjectAdapter;
import com.immagine.workok.adapter.TaskProjectAdapter;
import com.immagine.workok.model.Project;
import com.immagine.workok.model.Task;
import com.immagine.workok.model.User;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MyTasksActivity extends AppCompatActivity implements TaskProjectAdapter.OnItemClickListener,SearchView.OnQueryTextListener {

    private RecyclerView recycler;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager lManager;
    private TextView message;
    private TaskAssignedListTask mTask = null;
    private List<Task> items = new ArrayList<>();
    private List<Project> projects = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        setContentView(R.layout.activity_my_tasks);
        message = (TextView) findViewById(R.id.message);
        recycler = (RecyclerView) findViewById(R.id.reciclador);
        lManager = new LinearLayoutManager(this);
        recycler.setLayoutManager(lManager);
    }


    @Override
    protected void onResume() {
        super.onResume();
        PreferencesUtil preference = new PreferencesUtil(this);
        User.user.setUser_id(preference.getUserId());
        User.user.setFullname(preference.getUserName());
        getTasks(User.user.getUser_id());

    }

    private void getTasks(final int user_id) {
        mTask = new TaskAssignedListTask(user_id,MyTasksActivity.this);
        mTask.execute((Void) null);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_search, menu);
        MenuItem searchItem = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(this);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if(item.getItemId() == android.R.id.home){
            onBackPressed();
            return true;
        }
        else
            return false;

    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String query) {
        query = query.toLowerCase();

        final List<Task> filteredList = new ArrayList<>();

        for (int i = 0; i < items.size(); i++) {

            final String text = items.get(i).getTitle().toLowerCase();
            if (text.contains(query)) {

                filteredList.add(items.get(i));
            }
        }
        adapter = new TaskProjectAdapter(filteredList);
        recycler.setAdapter(adapter);
        adapter.notifyDataSetChanged();  // data set changed
        return true;
    }

    @Override
    public void onClick(View view, int index) {

        Task task = items.get(index);
        Intent intent = new Intent(this,NewTaskActivity.class);
        intent.putExtra("task",task);
        Bundle bundle = new Bundle();
        bundle.putInt("edit",1);
        intent.putExtras(bundle);
        intent.putExtra("project",projects.get(index));
        startActivity(intent);
    }


    public class TaskAssignedListTask extends AsyncTask<Void, Void, Boolean> {

        private final int userId;

        ProgressDialog progressDialog;

        TaskAssignedListTask(int userId,Activity a) {
            this.userId = userId;
            progressDialog = new ProgressDialog(a,R.style.AppTheme_Dark_Dialog);


        }

        @Override
        protected void onPreExecute() {
            progressDialog.setMessage("Cargando...");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.



            String dataUrl = "http://www.jexsantofagasta.cl/workok/wotask.php";
            String dataUrlParameters = "user_id="+userId+"&action="+ Constants.ACTION_LIST_MY_TASKS;
            URL url;
            HttpURLConnection connection = null;
            try {
                // Create connection
                url = new URL(dataUrl);
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
                connection.setRequestProperty("Content-Length","" + Integer.toString(dataUrlParameters.getBytes().length));
                connection.setRequestProperty("Content-Language", "en-US");
                connection.setUseCaches(false);
                connection.setDoInput(true);
                connection.setDoOutput(true);
                // Send request
                DataOutputStream wr = new DataOutputStream(
                        connection.getOutputStream());
                wr.writeBytes(dataUrlParameters);
                wr.flush();
                wr.close();
                // Get Response
                InputStream is = connection.getInputStream();
                BufferedReader rd = new BufferedReader(new InputStreamReader(is));
                String line;
                StringBuffer response = new StringBuffer();

                while ((line = rd.readLine()) != null) {
                    response.append(line);
                    response.append('\r');
                }
                rd.close();

                String responseStr = response.toString();
                JSONObject jsonObj = new JSONObject(responseStr);
                if(jsonObj.getString("success").equals("1")){
                    JSONArray dataArray = jsonObj.getJSONArray("data");
                    items.clear();
                    for (int i = 0;i<dataArray.length();i++) {

                        JSONObject data = dataArray.getJSONObject(i);
                        Task task = new Task(data.getString("title"),
                                data.getString("description"),data.getString("date_start"),data.getString("date_end"),data.getInt("percentage"),
                                data.getInt("status_id"),data.getInt("project_id"),data.getInt("user_id"),data.getInt("task_id"),data.getString("fullname"),
                                data.getInt("owner"),data.getString("project_title"),data.getString("owner_fullname"));
                        Project project = new Project();
                        project.setDateStart(data.getString("project_start"));
                        project.setDateEnd(data.getString("project_end"));
                        projects.add(project);
                        items.add(task);
                    }
                    Log.d("Server response",responseStr);
                    return true;
                }else{


                    return false;
                }


            } catch (Exception e) {

                e.printStackTrace();
                return false;

            } finally {

                if (connection != null) {
                    connection.disconnect();
                }
            }

        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mTask = null;

            //showProgress(false);
            if (success) {

                if (!items.isEmpty())
                    message.setVisibility(View.GONE);

                adapter = new TaskProjectAdapter(items,MyTasksActivity.this);
                recycler.setAdapter(adapter);

            }else{


            }
            progressDialog.dismiss();
        }

        @Override
        protected void onCancelled() {
            mTask = null;
            //showProgress(false);
        }
    }
}
