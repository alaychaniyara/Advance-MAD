package inclass2.group1.inclass02_android;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.ArrayList;

import inclass2.group1.inclass02_android.data.Survey;
import inclass2.group1.inclass02_android.data.SurveyInfo;
import inclass2.group1.inclass02_android.data.UserInfo;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class SurveyActivity extends AppCompatActivity {

    TextView questionNo,questionName;
    SurveyInfo surveyQues = null;
    Survey survey;
    private final OkHttpClient client = new OkHttpClient();
    String token;
    ProgressDialog progressDialog;
    Survey userSurvey;

    RadioGroup rg;
    RadioButton rb;
    int questionIndex = 0;
    int answer2 = -1;
    int answer3 = -2;
    int sumOf2and3 = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey);

        questionNo = findViewById(R.id.textViewQuestionNo);
        questionName = findViewById(R.id.textViewQuestion);
        rg = (RadioGroup)findViewById(R.id.radioGroup);

        if(getIntent().getExtras()!=null){
            if(getIntent().getExtras().get("token")!=null){
                progressDialog = new ProgressDialog(SurveyActivity.this);
                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progressDialog.setTitle("Loading user survey");
                token = (String)getIntent().getExtras().get("token");
                getUserInfo();
            }
        }

        findViewById(R.id.btnNext).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("demo", "onClick: "  + rg.getCheckedRadioButtonId());
                if(rg.getCheckedRadioButtonId() == -1){
                    Toast.makeText(getApplicationContext(),"Choose a valid response",Toast.LENGTH_SHORT);
                }else{
                    //radio button is checked.. so move to next question
                    int id= rg.getCheckedRadioButtonId();
                    userSurvey.getSurveys().get(questionIndex).setUserChoice(String.valueOf(id));
                    if(questionIndex == 0 && id == 0){//first question if user answers 0 go to 9 and 10.
                        questionIndex = 7;
                    }
                    if(questionIndex == 1){
                        answer2 = id;
                        Log.d("demo", "answer2: " + answer2);
                    }
                    if(questionIndex == 2){
                        answer3 = id;
                        Log.d("demo", "answer3: " + answer3);
                    }
                    sumOf2and3 = (answer2+answer3);
                    if(sumOf2and3==0){
                        if(questionIndex == 8){
                            answer2 = -1;
                            answer3 = -2;
                        }else{
                            questionIndex = 7;
                        }
                        Log.d("demo", "adding 2 and 3 : " + (answer2+answer3));
                    }
                    clearAllFields();
                    if(questionIndex < 10){
                        displayQuestions(userSurvey.getSurveys().get(questionIndex));
                    }else{
                        Intent result = new Intent(SurveyActivity.this,ResultActivity.class);
                        result.putExtra("surveyResults",userSurvey);
                        startActivity(result);
                        finish();
                    }
                }
            }
        });
    }

    private void clearAllFields() {
        rg.removeAllViews();
        questionName.setText("");
        questionIndex = questionIndex+1;
    }

    private void getUserInfo() {
        Request request = new Request.Builder().url("http://52.23.253.255:3000/users/userinfo")
                .header("Authorization","BEARER" + token)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("test", "onFailure: " + e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseBody = response.body().toString();
                Log.d("demo", "onResponse: responseBody is :" + responseBody);
                Gson gson = new Gson();
                final UserInfo userInfo = gson.fromJson(responseBody,UserInfo.class);
                Log.d("test", "onResponse: userinfo object is " + userInfo.toString());

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //create a shared preference and save the user data.
                        // start the survey questions
                        try {
                            String readFile = readJsonFile();
                            Log.d("test", "reading json string file : " + readFile);
                            Gson gson = new Gson();
                            userSurvey = gson.fromJson(readFile,Survey.class);
                            for(SurveyInfo surveyInfo:userSurvey.getSurveys()){
                                Log.d("demo", "Survey" + surveyInfo.getQuestionNo() + surveyInfo.toString());
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        progressDialog.dismiss();
                        displayQuestions(userSurvey.getSurveys().get(0));
                    }
                });
            }
        });
    }

    private String readJsonFile() throws IOException {
        InputStream is = getResources().openRawResource(R.raw.survey);
        Writer writer = new StringWriter();
        char[] buffer = new char[1024];
        try {
            Reader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            int n;
            while ((n = reader.read(buffer)) != -1) {
                writer.write(buffer, 0, n);
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            is.close();
        }
        String jsonString = writer.toString();
        return jsonString;
    }

    private void displayQuestions(SurveyInfo surveyQues) {
        Log.d("demo", "question index: " + questionIndex);
        questionName.setText(surveyQues.getQuestion());
        if(surveyQues.getChoice0()!=null){
            rb = new RadioButton(this);
            rb.setVisibility(View.VISIBLE);
            rb.setId(0);
            rb.setText(surveyQues.getChoice0());
            rg.addView(rb);
        }
        if(surveyQues.getChoice1()!=null){
            rb = new RadioButton(this);
            rb.setVisibility(View.VISIBLE);
            rb.setId(Integer.parseInt("1"));
            rb.setText(surveyQues.getChoice1());
            rg.addView(rb);
        }
        if(surveyQues.getChoice2()!=null){
            rb = new RadioButton(this);
            rb.setVisibility(View.VISIBLE);
            rb.setId(Integer.parseInt("2"));
            rb.setText(surveyQues.getChoice2());
            rg.addView(rb);
        }
        if(surveyQues.getChoice3()!=null){
            rb = new RadioButton(this);
            rb.setVisibility(View.VISIBLE);
            rb.setId(Integer.parseInt("3"));
            rb.setText(surveyQues.getChoice3());
            rg.addView(rb);
        }
        if(surveyQues.getChoice4()!=null){
            rb = new RadioButton(this);
            rb.setVisibility(View.VISIBLE);
            rb.setId(Integer.parseInt("4"));
            rb.setText(surveyQues.getChoice4());
            rg.addView(rb);
        }
    }
}
