package com.example.shoppingapplication.User;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shoppingapplication.R;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;


import org.json.JSONObject;

public class PaymentOptionActivity extends AppCompatActivity implements PaymentResultListener {

    private Button podButton, onlineButton;
    private EditText nameEditText, phoneEditText, addressEditText, cityEditText;
    private String totalAmount = "";
    private TextView productPrice;
    private ImageView backButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_option);
        totalAmount = getIntent().getStringExtra("Total Price = ");
        Checkout.preload(getApplicationContext());

        productPrice = findViewById(R.id.product_price);

        podButton = findViewById(R.id.pod_button);
        onlineButton = findViewById(R.id.online_button);

        onlineButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startPayment();
            }
        });


        podButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PaymentOptionActivity.this, HomeActivity.class);
                Toast.makeText(PaymentOptionActivity.this, "Your Order has been Placed Successfully", Toast.LENGTH_SHORT).show();
                startActivity(intent);
            }
        });

        backButton = findViewById(R.id.payment_back_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PaymentOptionActivity.this, ConfirmFinalOrderActivity.class);
                startActivity(intent);

            }
        });
    }

    private void startPayment() {
        Checkout checkout = new Checkout();

        checkout.setImage(R.drawable.ic_baseline_search);


        final Activity activity = this;


        try {
            JSONObject options = new JSONObject();

            options.put("name", "Mini Project");
            options.put("description", "Product Payment");
            options.put("image", "https://s3.amazonaws.com/rzp-mobile/images/rzp.png");
            options.put("theme.color", "#3399cc");
            options.put("currency", "INR");
        //    String payment = productPrice.getText().toString();
            double total = Double.parseDouble("500");
            total = total * 100;
            options.put("amount", total);


            JSONObject preFill = new JSONObject();
            preFill.put("email", "sanambir123@gmail.com");
            preFill.put("contact", "9834198107");
            options.put("prefill", preFill);


            JSONObject retryObj = new JSONObject();
            retryObj.put("enabled", true);
            retryObj.put("max_count", 4);
            options.put("retry", retryObj);

            checkout.open(activity, options);

        } catch(Exception e) {
            Log.e("TAG", "Error in starting Razorpay Checkout", e);
        }

    }

    @Override
    public void onPaymentSuccess(String s) {
        Toast.makeText(this, "Payment Successful", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(PaymentOptionActivity.this, HomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    @Override
    public void onPaymentError(int i, String s) {
        Toast.makeText(this, "Payment failed", Toast.LENGTH_SHORT).show();
    }
}