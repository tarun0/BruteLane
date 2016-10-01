package developer.shivam.brutelane;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class PaymentActivity extends AppCompatActivity {

    private String carrier;
    private String phone;
    private String prepost;
    private String amount;
    String s;
    TextView textView;

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
    }
}
