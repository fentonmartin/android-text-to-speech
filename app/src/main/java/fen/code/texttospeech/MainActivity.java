package fen.code.texttospeech;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private final int REQ_CODE_SPEECH_INPUT = 100;
    Button textToSpeech, speechToText;
    EditText textInput;
    TextView textResult;
    TextToSpeech speech;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setLog("onCreate");

        /* Add views declaration */
        textToSpeech = (Button) findViewById(R.id.text_to_speech_btn);
        speechToText = (Button) findViewById(R.id.speech_to_text_btn);
        textInput = (EditText) findViewById(R.id.text_input);
        textResult = (TextView) findViewById(R.id.text_result);

        /* Init Text to Speech */
        speechInit();

        /* Init Button Text to Speech */
        textToSpeech.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setLog("onClick: textToSpeech");
                /* Get Text from Text Input */
                String text = textInput.getText().toString();
                textResult.setText(text);

                /* Speak Text with Text to Speech */
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    speech.speak(text, TextToSpeech.QUEUE_FLUSH, null, null);
                    setLog("textToSpeech: Speak Lollipop");
                } else {
                    // noinspection deprecation
                    speech.speak(text, TextToSpeech.QUEUE_FLUSH, null);
                    setLog("textToSpeech: Speak (Deprecated)");
                }
            }
        });

        /* Init Button Speech to Text */
        speechToText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setLog("onClick: speechToText");
                /* Start Speech Recognition */
                speechRecognition();
            }
        });
    }

    public void onPause() {
        setLog("onPause");
        /* Shutdown Text to Speech */
        if (speech != null) {
            speech.stop();
            speech.shutdown();
            setLog("Speech Shutdown");
        }
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();

        /* Init Text to Speech */
        speechInit();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        setLog("onActivityResult");

        /* Add Recognizer Result */
        switch (requestCode) {
            case REQ_CODE_SPEECH_INPUT: {
                if (resultCode == RESULT_OK && null != data) {
                    /* Get Recognizer Result */
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);

                    /* Set Recognizer Result */
                    textResult.setText(result.get(0));
                    setLog("onActivityResult: Speech Success");
                }
                break;
            }
        }
    }

    private void speechInit() {
        /* Init Text to Speech */
        speech = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {
                setLog("onInit TTS");
                /* Check Text to Speech */
                if (i != TextToSpeech.ERROR) {
                    /* Set Text to Speech Language */
                    speech.setLanguage(Locale.ENGLISH);

                    /* Set Toast Ready */
                    Toast.makeText(MainActivity.this, "Text to Speech is ready :)",
                            Toast.LENGTH_SHORT).show();
                    setLog("Success TTS");

                    /* Set OnUtterance Progress Listener */
                    speech.setOnUtteranceProgressListener(new UtteranceProgressListener() {
                        @Override
                        public void onStart(String s) {
                            /* Set Speech Started */
                            Toast.makeText(MainActivity.this, "Speech Started",
                                    Toast.LENGTH_SHORT).show();
                            setLog("Listener TTS onStart: " + s);
                        }

                        @Override
                        public void onDone(String s) {
                            /* Set Speech Done */
                            Toast.makeText(MainActivity.this, "Speech Done",
                                    Toast.LENGTH_SHORT).show();
                            setLog("Listener TTS onDone: " + s);
                        }

                        @Override
                        public void onError(String s) {
                            /* Set Speech Error */
                            Toast.makeText(MainActivity.this, "Speech Error",
                                    Toast.LENGTH_SHORT).show();
                            setLog("Listener TTS onError: " + s);
                        }
                    });
                } else {
                    /* Set Toast Error */
                    Toast.makeText(MainActivity.this, "Something is wrong :(",
                            Toast.LENGTH_SHORT).show();
                    setLog("Phone not supported");
                }
            }
        });
    }

    /* Add Speech Recognition */
    public void speechRecognition() {
        setLog("speechRecognition Initiated");
        /* Add Recognizer Intent */
        Intent intent = new Intent(RecognizerIntent
                .ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent
                .EXTRA_LANGUAGE_MODEL, RecognizerIntent
                .LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent
                .EXTRA_LANGUAGE, Locale.ENGLISH);
        intent.putExtra(RecognizerIntent
                .EXTRA_PROMPT, getString(R.string.speech_prompt));

        try {
            /* Start Activity For Result */
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
            setLog("speechRecognition: StartActivityForResult");
        } catch (ActivityNotFoundException a) {
            /* Set Phone Not Supported Recognizer */
            Toast.makeText(getApplicationContext(), getString(R.string.speech_not_supported),
                    Toast.LENGTH_SHORT).show();
            setLog("speechRecognition: ActivityNotFoundException");
        }
    }

    private void setLog(String log) {
        Log.d(getClass().getSimpleName(), log);
    }
}
