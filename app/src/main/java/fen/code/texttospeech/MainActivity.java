package fen.code.texttospeech;

import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    Button textToSpeech, speechToText;
    EditText textInput;
    TextView textResult;
    TextToSpeech speech;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /* Add views declaration */
        textToSpeech = (Button) findViewById(R.id.text_to_speech_btn);
        speechToText = (Button) findViewById(R.id.speech_to_text_btn);
        textInput = (EditText) findViewById(R.id.text_input);
        textResult = (TextView) findViewById(R.id.text_result);

        /* Init Text to Speech */
        speech = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {
                /* Check Text to Speech */
                if (i != TextToSpeech.ERROR) {
                    /* Set Text to Speech Language */
                    speech.setLanguage(Locale.ENGLISH);

                    /* Set Toast Ready */
                    Toast.makeText(MainActivity.this, "Text to Speech is ready :)",
                            Toast.LENGTH_SHORT).show();
                } else {
                    /* Set Toast Error */
                    Toast.makeText(MainActivity.this, "Something is wrong :(",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void onPause() {
        if (speech != null) {
            speech.stop();
            speech.shutdown();
        }
        super.onPause();
    }
}
