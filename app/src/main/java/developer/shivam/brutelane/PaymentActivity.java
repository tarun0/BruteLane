package developer.shivam.brutelane;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.paytm.pgsdk.PaytmMerchant;
import com.paytm.pgsdk.PaytmOrder;
import com.paytm.pgsdk.PaytmPGService;
import com.paytm.pgsdk.PaytmPaymentTransactionCallback;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class PaymentActivity extends AppCompatActivity {

    private String carrier;
    private String phone;
    private String prepost;
    private String amount;
    String s;
    TextView textView;
    private PaytmPGService Service = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        phone = getIntent().getStringExtra(Util.EXTRA_PHONE);
        prepost = getIntent().getStringExtra(Util.EXTRA_PRE_POST);
        amount = getIntent().getStringExtra(Util.EXTRA_AMOUNT);
        carrier = getIntent().getStringExtra(Util.EXTRA_CARRIER);
        textView =(TextView) findViewById(R.id.textView);
        s = phone+"\n"+prepost+"\n"+amount+"\n"+carrier;
        textView.setText(""+s);

        Log.e("AMOUNT", amount);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        onStartTransaction();
    }


    public void onStartTransaction() {
        PaytmPGService Service = PaytmPGService.getStagingService();
        Map<String, String> paramMap = new HashMap<String, String>();

        String MID = "WorldP64425807474247";
        String CUST_ID = "Pay0A0dd123454321678";
        String INDUSTRY_TYPE = "Retail";
        String WEBSITE = "worldpressplg";
        String CHANNEL = "WEB";
        String AMOUNT = amount;
        String MOBILE_NO = "7777777777";
        String THEME = "merchant";

        Random r = new Random(System.currentTimeMillis());
        String orderId = "ORDERABCD" + (1 + r.nextInt(2)) * 10000
                + r.nextInt(10000);

        // these are mandatory parameters

        paramMap.put("ORDER_ID", orderId);
        paramMap.put("MID", MID);
        paramMap.put("CUST_ID", CUST_ID);
        paramMap.put("CHANNEL_ID", CHANNEL);
        paramMap.put("INDUSTRY_TYPE_ID", INDUSTRY_TYPE);
        paramMap.put("WEBSITE", WEBSITE);
        paramMap.put("TXN_AMOUNT", AMOUNT);
        paramMap.put("THEME", THEME);
        paramMap.put("MOBILE_NO", MOBILE_NO);
        paramMap.put("CALLBACK_URL", "https://pguat.paytm.com/paytmchecksum/paytmCheckSumVerify.jsp");
        PaytmOrder Order = new PaytmOrder(paramMap);

        PaytmMerchant Merchant = new PaytmMerchant(
                "https://pguat.paytm.com/paytmchecksum/paytmCheckSumGenerator.jsp",
                "https://pguat.paytm.com/paytmchecksum/paytmCheckSumVerify.jsp");

        Service.initialize(Order, Merchant, null);

        Service.startPaymentTransaction(this, true, true,
                new PaytmPaymentTransactionCallback() {
                    @Override
                    public void someUIErrorOccurred(String inErrorMessage) {
                        // Some UI Error Occurred in Payment Gateway Activity.
                        // // This may be due to initialization of views in
                        // Payment Gateway Activity or may be due to //
                        // initialization of webview. // Error Message details
                        // the error occurred.

                        Log.d("LOG", "Payment Transaction UI Error " + inErrorMessage);
                        Toast.makeText(getApplicationContext(), "Payment Transaction Ui Error ", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onTransactionSuccess(Bundle inResponse) {
                        // After successful transaction this method gets called.
                        // // Response bundle contains the merchant response
                        // parameters.
                        Log.d("LOG", "Payment Transaction is successful " + inResponse);
                        Toast.makeText(getApplicationContext(), "Payment Transaction is successful ", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onTransactionFailure(String inErrorMessage,
                                                     Bundle inResponse) {
                        // This method gets called if transaction failed. //
                        // Here in this case transaction is completed, but with
                        // a failure. // Error Message describes the reason for
                        // failure. // Response bundle contains the merchant
                        // response parameters.
                        Log.d("LOG", "Payment Transaction Failed " + inErrorMessage);
                        Toast.makeText(getBaseContext(), "Payment Transaction Failed ", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void networkNotAvailable() { // If network is not
                        // available, then this
                        // method gets called.

                        Log.d("LOG", "No internet");
                        Toast.makeText(getApplicationContext(), "Please turn On the Internet!!!", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void clientAuthenticationFailed(String inErrorMessage) {
                        // This method gets called if client authentication
                        // failed. // Failure may be due to following reasons //
                        // 1. Server error or downtime. // 2. Server unable to
                        // generate checksum or checksum response is not in
                        // proper format. // 3. Server failed to authenticate
                        // that client. That is value of payt_STATUS is 2. //
                        // Error Message describes the reason for failure.
                        Log.d("LOG", "Payment Transaction Auth failed " + inErrorMessage);
                        Toast.makeText(getApplicationContext(), "Payment Auth Failed", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onErrorLoadingWebPage(int iniErrorCode,
                                                      String inErrorMessage, String inFailingUrl) {

                    }

                    // had to be added: NOTE
                    @Override
                    public void onBackPressedCancelTransaction() {
                        // TODO Auto-generated method stub
                    }

                });
    }

}
