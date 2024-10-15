package com.example.transapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;


import android.content.res.AssetManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Spinner;
import android.widget.SimpleAdapter;
import android.view.MotionEvent;  // MotionEventのインポート
import android.view.View;
import android.widget.ScrollView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import java.util.Calendar;
import java.text.SimpleDateFormat; // SimpleDateFormatをインポート

import android.widget.Toast;

import java.util.Date;
import java.util.Locale;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.lang.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.os.AsyncTask;
import android.content.SharedPreferences;
import org.json.JSONObject;
public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private EditText inputText;
    private TextView outputText;
    private Button translateButton;
    private Spinner transStyle;
    private Spinner inputLang;
    private Spinner outputLang;
    private static final String GAS_URL="https://script.google.com/macros/s/AKfycbwQmfZ_q1dZO5oWmVW1YaYsczZqwy_Xk6vwwpwaOdZluD7r3SuAPavMoPCmQLSpcQyy/exec";
    //GAS_URLは翻訳用APIのURLです。これを利用して、英語に翻訳する関数translate()を作成します。translate()関数は、翻訳したい文字列を引数として受け取り、翻訳結果をStringで返します。以下に関数を記述します。
    public static String translate(String text,String i_lang,String o_lang,String style) {
        try {
            URL url = new URL(GAS_URL + "?text=" + URLEncoder.encode(text, "UTF-8")
                    + "&i_lang=" + URLEncoder.encode(i_lang, "UTF-8")
                    + "&o_lang=" + URLEncoder.encode(o_lang, "UTF-8")
                    + "&trans_style=" + URLEncoder.encode(o_lang, "UTF-8"));
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();
            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String result = reader.readLine();
            String line;
            while((line=reader.readLine())!=null){
                result+=line;
            }
            Log.d("DEBUG", "Response: " + result);
            reader.close();
            conn.disconnect();
            JSONObject json_result = new JSONObject(result);
            return json_result.getString("answer");
            //return result;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // レイアウト（activity_main.xml）を表示する
        setContentView(R.layout.activity_main);
        // 「表示」ボタンをタップしたらonClick()を呼び出す
        inputText = findViewById(R.id.inputText);
        outputText = findViewById(R.id.translatedText);
        translateButton = findViewById(R.id.translateButton);
        inputLang=findViewById(R.id.sourceLanguageSpinner);
        outputLang=findViewById(R.id.outputLanguageSpinner);
        transStyle=findViewById(R.id.translationStyleSpinner);
        translateButton.setOnClickListener(this);
    }
    @Override
    public void onClick(View view) {
        // クリックされたボタンを識別
        int getButton = view.getId();
        // 「表示」がクリックされたとき、表示処理用のメソッドを呼び出す
        if (getButton == R.id.translateButton) {
            String input = inputText.getText().toString();
            String i_lang=inputLang.getSelectedItem().toString();
            String o_lang=outputLang.getSelectedItem().toString();
            String style=transStyle.getSelectedItem().toString();
            // 非同期で翻訳処理を実行
            new TranslateTask().execute(input,i_lang,o_lang,style);
        }
    }
    // 非同期で翻訳処理を行うクラス
    private class TranslateTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            String text = params[0];
            String i_lang=params[1];
            String o_lang=params[2];
            String style=params[3];
            return translate(text,i_lang,o_lang,style);
        }
        @Override
        protected void onPostExecute(String result) {
            // 翻訳結果をUIスレッドで表示
            outputText.setText(result);
        }
    }
}