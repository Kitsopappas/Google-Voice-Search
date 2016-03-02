package com.chrispapp.redone;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    private TextView txtSpeechInput;
    private ImageView speakButton;
    private final int REQ_CODE_SPEECH_INPUT = 100;

    private static final String TAG_REGEX = "(.*)([+-])(.*)";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        txtSpeechInput = (TextView) findViewById(R.id.txtSpeechInput);
        speakButton = (ImageView) findViewById(R.id.speakb);

        speakButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                promptSpeechInput();
            }
        });
    }


    /**
     * Showing google speech input dialog
     * */
    private void promptSpeechInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                getString(R.string.speech_prompt));
        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(getApplicationContext(),
                    getString(R.string.speech_not_supported),
                    Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Receiving speech input
     * */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQ_CODE_SPEECH_INPUT: {
                if (resultCode == RESULT_OK && null != data) {

                    ArrayList<String> result = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    txtSpeechInput.setText(equation(result.get(0)));
                }
                break;
            }

        }
    }

    /*
    * Regex pattern (.*)([+-])(.*)
    *
    */
    protected String equation(String input){
        // Create a Pattern object
        String result = null;
        Pattern r = Pattern.compile(TAG_REGEX);

        // Now create matcher object.
        Matcher m = r.matcher(input.replaceAll("\\s+",""));
        if (m.find( )) {
            Log.i("Res", m.group(1) + " " + m.group(2) + " " + m.group(3));
            int val1 = Integer.valueOf(m.group(1));
            String symbol = m.group(2);
            int val2 = Integer.valueOf(m.group(3));

            if(symbol.equals("+")){
                result = input + " =" + String.valueOf(val1 + val2);
            }else if(symbol.equals("-")){
                result = input + " =" + String.valueOf(val1 - val2);
            }else if(symbol.equals("product")){
                result = input + " =" + String.valueOf(val1 * val2);
            }else if(symbol.equals("for")) {
                result = input + " =" + String.valueOf(val1 * val2);
            }else if(symbol.equals("/")) {
                result = input + " =" + String.valueOf(val1 / val2);
            }else{
                result = "Can't find equation";
            }
        } else {
            result = input;
        }
        return result;
    }

}
