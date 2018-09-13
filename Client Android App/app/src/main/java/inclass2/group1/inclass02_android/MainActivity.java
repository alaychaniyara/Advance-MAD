package inclass2.group1.inclass02_android;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import inclass2.group1.inclass02_android.data.SurveyInfo;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    SurveyInfo userSurvey = new SurveyInfo();
    ProgressDialog progressDialog;
    EditText userName,password;
    private final OkHttpClient client = new OkHttpClient();
    JSONObject tokenObject;
    String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        userName = findViewById(R.id.editTextUserLogin);
        password = findViewById(R.id.editTextPassword);

        findViewById(R.id.buttonLogin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(userName.getText().toString()==null || userName.getText().toString().equals("")){
                    Toast.makeText(getApplicationContext(),"Enter valid user name",Toast.LENGTH_SHORT);
                }else if(password.getText().toString()==null || password.getText().toString().equals("")){
                    Toast.makeText(getApplicationContext(),"Enter valid password",Toast.LENGTH_SHORT);
                }else{
                    progressDialog = new ProgressDialog(MainActivity.this);
                    progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    progressDialog.setTitle("Signing Up....");
                    progressDialog.show();
                    performLogin(userName.getText().toString(),password.getText().toString());
                }
            }
        });
    }

    private void performLogin(String userName, String password) {
        //creating he json object and hit the api
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("email",userName);
        jsonObject.addProperty("password",password);
        MediaType JSON = MediaType.parse("application/json;charset=utf-8");
        RequestBody formBody = RequestBody.create(JSON,jsonObject.toString());
        final Request request = new Request.Builder().url("http://52.23.253.255:3000/users/login")//replace the ip here
                .post(formBody)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("test", "onFailure: " + e.getMessage());
                Toast.makeText(getApplicationContext(),"Login Failed",Toast.LENGTH_SHORT);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    tokenObject = new JSONObject(String.valueOf(response));
                    token  = tokenObject.getString("token");
                } catch (JSONException e) {
                   e.printStackTrace();
                }
                Log.d("test", "onResponse: Successful Login" + response.body().toString());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressDialog.dismiss();
                        saveUserData();
                        Intent survey = new Intent(MainActivity.this,SurveyActivity.class);
                        survey.putExtra("token",token);
                        startActivity(survey);
                        finish();
                    }
                });
            }
        });
    }

    private void saveUserData() {
        SharedPreferences sharedPreferences = getSharedPreferences("My_Pref",MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("email",userName.toString());
    }
}
