package inclass2.group1.inclass02_android;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import inclass2.group1.inclass02_android.data.Survey;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ResultActivity extends AppCompatActivity {

    private final OkHttpClient client = new OkHttpClient();
    JSONObject tokenObject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        if(getIntent().getExtras()!=null){
            if(getIntent().getExtras().getSerializable("surveyResults")!=null){
                Survey userSurvey = (Survey)getIntent().getExtras().getSerializable("surveyResults");
                Log.d("demo", "onCreate: " + userSurvey.toString());
                submitSurvey(userSurvey);
            }
        }
    }

    private void submitSurvey(Survey userSurvey) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("answer1",userSurvey.getSurveys().get(0).getUserChoice());
        jsonObject.addProperty("answer2",userSurvey.getSurveys().get(1).getUserChoice());
        jsonObject.addProperty("answer3",userSurvey.getSurveys().get(2).getUserChoice());
        jsonObject.addProperty("answer4",userSurvey.getSurveys().get(3).getUserChoice());
        jsonObject.addProperty("answer5",userSurvey.getSurveys().get(4).getUserChoice());
        jsonObject.addProperty("answer6",userSurvey.getSurveys().get(5).getUserChoice());
        jsonObject.addProperty("answer7",userSurvey.getSurveys().get(6).getUserChoice());
        jsonObject.addProperty("answer8",userSurvey.getSurveys().get(7).getUserChoice());
        jsonObject.addProperty("answer9",userSurvey.getSurveys().get(8).getUserChoice());
        jsonObject.addProperty("answer10",userSurvey.getSurveys().get(9).getUserChoice());
        MediaType JSON = MediaType.parse("application/json;charset=utf-8");
        //make the api call....
        RequestBody formBody = RequestBody.create(JSON,jsonObject.toString());
        final Request request = new Request.Builder().url("http://user/surveys")//replace the ip here
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
                    String token  = tokenObject.getString("token");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
