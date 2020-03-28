package com.example.raama.ams;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class genrateotp extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_genrateotp);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("OTP Generation");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
    public void generateRandom(View view)
    {
        //generating 4 digit random no
        int max=9999;
        int min=1000;
        int random_num = (int)((Math.random()*(max-min)+1)+min);
        String s = String.valueOf(random_num);

        FirebaseDatabase database=FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("otp/otp");
        myRef.setValue(random_num);




        TextView textView = findViewById(R.id.ran);
        textView.setText(s);

    }

    public void gotoBleSearch(View view)
    {
        Intent intent = new Intent(genrateotp.this, blesearch.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
}
