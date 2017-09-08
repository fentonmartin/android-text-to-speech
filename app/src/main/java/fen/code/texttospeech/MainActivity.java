package fen.code.texttospeech;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    Button textToSpeech, speechToText;
    EditText textInput;
    TextView textResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /* Add views declaration */
        textToSpeech = (Button) findViewById(R.id.text_to_speech_btn);
        speechToText = (Button) findViewById(R.id.speech_to_text_btn);
        textInput = (EditText) findViewById(R.id.text_input);
        textResult = (TextView) findViewById(R.id.text_result);
        
    }
}
