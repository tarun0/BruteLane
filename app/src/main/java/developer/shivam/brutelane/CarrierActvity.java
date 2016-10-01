package developer.shivam.brutelane;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
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

public class CarrierActvity extends AppCompatActivity implements TextToSpeech.OnInitListener, RecognitionListener {

    private static final String TAG = "VoicePayment";
    private TextToSpeech speaker;
    private GestureDetectorCompat gestureDetector;
    String MOBILE_PAYMENTS = "Say 1 for mobile payment";
    private Context mContext = CarrierActvity.this;
    String ELECTRICITY_PAYMENT = "Say 2 for electricity payment";
    private boolean isWelcomeCompleted = false;
    private boolean listenToUser = false;
    private final int CODE_SPEECH_INPUT = 1;
    Button speechInputButton;
    private Intent recognizerIntent;

    private String phone;
    private String prepost;
    private String amount;
    private String carrier;

    boolean flag_mobile = false;
    boolean flag_postpaid_prepaid = false;

    private SpeechRecognizer speech = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main5);
        speaker = new TextToSpeech(this, this);
        speaker.setSpeechRate(1f);
        sayHello();

        phone = getIntent().getStringExtra(Util.EXTRA_PHONE);
        prepost = getIntent().getStringExtra(Util.EXTRA_PRE_POST);
        amount = getIntent().getStringExtra(Util.EXTRA_AMOUNT);

        speechInputButton = (Button) findViewById(R.id.btnSpeechInput);
        speechInputButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                promptSpeechInput();
            }
        });

        gestureDetector = new GestureDetectorCompat(mContext, new GestureListener());

        speech = SpeechRecognizer.createSpeechRecognizer(this);
        speech.setRecognitionListener(this);
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
        speaker.speak("Please enter the carrier.", TextToSpeech.QUEUE_FLUSH, null);
        listenToUser = true;
    }

    private void resayHello() {
        speaker.speak("Carrier not recognized. Please speak the carrier again.", TextToSpeech.QUEUE_FLUSH, null);
        listenToUser = true;
    }


    public void promptSpeechInput() {

        recognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE,
                "en");
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE,
                this.getPackageName());
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_WEB_SEARCH);
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 3);


        try {
            //startActivityForResult(intent, CODE_SPEECH_INPUT);
            speech.startListening(recognizerIntent);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(getApplicationContext(), "Not Supported", Toast.LENGTH_SHORT).show();
        }
    }



    @Override
    public void onReadyForSpeech(Bundle bundle) {

    }

    @Override
    public void onBeginningOfSpeech() {

    }

    @Override
    public void onRmsChanged(float v) {

    }

    @Override
    public void onBufferReceived(byte[] bytes) {

    }

    @Override
    public void onEndOfSpeech() {

    }

    @Override
    public void onError(int errorCode) {
        String errorMessage = getErrorText(errorCode);
        //Log.d(LOG_TAG, "FAILED " + errorMessage);

        Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onResults(Bundle results) {
        ArrayList<String> matches = results
                .getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);

        String text2 = matches.get(0);
        switch (text2) {
            case "Airtel":
                carrier = "airtel";
                break;
            case "Idea":
                carrier = "idea";
                break;
            case "vodafone":
                carrier = "vodafone";
                break;
            case "Vodaphone":
                carrier = "vodafone";
                break;
            case "Aircel":
                carrier = "aircel";
                break;
            default:
                resayHello();
        }

        Intent toPayment = new Intent(this, PaymentActivity.class);
        toPayment.putExtra(Util.EXTRA_AMOUNT, amount);
        toPayment.putExtra(Util.EXTRA_PHONE, phone);
        toPayment.putExtra(Util.EXTRA_PRE_POST, prepost);
        toPayment.putExtra(Util.EXTRA_CARRIER, carrier);
        startActivity(toPayment);

        Toast.makeText(this,text2, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPartialResults(Bundle bundle) {

    }

    @Override
    public void onEvent(int i, Bundle bundle) {

    }

    public static String getErrorText(int errorCode) {
        String message;
        switch (errorCode) {
            case SpeechRecognizer.ERROR_AUDIO:
                message = "Audio recording error";
                break;
            case SpeechRecognizer.ERROR_CLIENT:
                message = "Client side error";
                break;
            case SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS:
                message = "Insufficient permissions";
                break;
            case SpeechRecognizer.ERROR_NETWORK:
                message = "Network error";
                break;
            case SpeechRecognizer.ERROR_NETWORK_TIMEOUT:
                message = "Network timeout";
                break;
            case SpeechRecognizer.ERROR_NO_MATCH:
                message = "No match";
                break;
            case SpeechRecognizer.ERROR_RECOGNIZER_BUSY:
                message = "RecognitionService busy";
                break;
            case SpeechRecognizer.ERROR_SERVER:
                message = "error from server";
                break;
            case SpeechRecognizer.ERROR_SPEECH_TIMEOUT:
                message = "No speech input";
                break;
            default:
                message = "Didn't understand, please try again.";
                break;
        }
        return message;
    }

    public class GestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onDoubleTap(MotionEvent e) {
            return super.onDoubleTap(e);
        }
    }
}