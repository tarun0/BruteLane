package developer.shivam.brutelane;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

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

        Random r = new Random();
        int i1 = r.nextInt(99999 - 10000) + 10000;
        String orderId = 8+"HACK"+i1;

        Service = PaytmPGService.getStagingService();
        Map<String, String> paramMap = new HashMap<String, String>();
        paramMap.put("REQUEST_TYPE", "DEFAULT");
        paramMap.put("ORDER_ID", orderId);
        paramMap.put("MID", "Payady47250449092675");
        paramMap.put("CUST_ID","CUST110");          //CHECK THIS CUST_ID VALUE
        paramMap.put("CHANNEL_ID", "WAP");
        paramMap.put("INDUSTRY_TYPE_ID", "Retail");
        paramMap.put("WEBSITE", "Paytmadd");
        paramMap.put("TXN_AMOUNT", amount);
        paramMap.put("THEME ", "merchant");

        PaytmOrder order = new PaytmOrder(paramMap);
        PaytmMerchant Merchant = new PaytmMerchant( "https://pguat.paytm.com/paytmchecksum/paytmCheckSumGenerator.jsp",
                "https://pguat.paytm.com/paytmchecksum/paytmCheckSumVerify.jsp");  //BACKEND HAS TO BE DEPLOYED

        Service.initialize(order, Merchant, null);
        Service.startPaymentTransaction(this, true, true, new PaytmPaymentTransactionCallback() {
            @Override
            public void onTransactionSuccess(Bundle bundle) {

            }

            @Override
            public void onTransactionFailure(String s, Bundle bundle) {

            }

            @Override
            public void networkNotAvailable() {

            }

            @Override
            public void clientAuthenticationFailed(String s) {

            }

            @Override
            public void someUIErrorOccurred(String s) {

            }

            @Override
            public void onErrorLoadingWebPage(int i, String s, String s1) {

            }

            @Override
            public void onBackPressedCancelTransaction() {

            }
        });


    }
}
