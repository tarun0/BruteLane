package developer.shivam.brutelane;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements TextToSpeech.OnInitListener {

    private static final String TAG = "VoicePayment";
    private TextToSpeech speaker;
    private GestureDetectorCompat gestureDetector;
    String MOBILE_PAYMENTS = "Say 1 for mobile payment";
    private Context mContext = MainActivity.this;
    String ELECTRICITY_PAYMENT = "Say 2 for electricity payment";
    private boolean isWelcomeCompleted = false;
    private boolean listenToUser = false;
    private final int CODE_SPEECH_INPUT = 1;
    Button speechInputButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        speechInputButton = (Button) findViewById(R.id.btnSpeechInput);
        speechInputButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                promptSpeechInput();
            }
        });

        gestureDetector = new GestureDetectorCompat(mContext, new GestureListener());
        speaker = new TextToSpeech(this, this);
        speaker.setSpeechRate(1f);
        sayHello();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        gestureDetector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }

    @Override
    public void onDestroy() {
        if (speaker != null) {
            speaker.stop();
            speaker.shutdown();
        }
        super.onDestroy();
    }

    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            int result = speaker.setLanguage(Locale.US);
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e(TAG, "Language is not available.");
            } else {
                sayHello();
            }
        } else {
            Log.e(TAG, "Could not initialize TextToSpeech.");
        }
    }

    private void sayHello() {
        speaker.speak("Welcome. Please choose any option", TextToSpeech.QUEUE_FLUSH, null);
        speaker.speak(MOBILE_PAYMENTS, TextToSpeech.QUEUE_ADD, null);
        speaker.speak(ELECTRICITY_PAYMENT, TextToSpeech.QUEUE_ADD, null);
        listenToUser = true;
    }

    public void enterMobileNumber() {
        speaker.speak("Please speak you mobile number", TextToSpeech.QUEUE_ADD, null);
    }

    public void promptSpeechInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speech Input");
        try {
            startActivityForResult(intent, CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(getApplicationContext(), "Not Supported", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case CODE_SPEECH_INPUT: {
                if (resultCode == RESULT_OK && null != data) {
                    ArrayList<String> result = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    if (!isWelcomeCompleted) {
                        if (result.get(0).equals("one")) {
                            enterMobileNumber();
                            isWelcomeCompleted = true;
                        }
                    } else {
                        System.out.println(result.get(0));
                        Toast.makeText(this, "Didn't detect", Toast.LENGTH_SHORT).show();
                    }
                } else {

                }
                break;
            }

        }
    }


    public class GestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onDoubleTap(MotionEvent e) {
            return super.onDoubleTap(e);
        }
    }
}