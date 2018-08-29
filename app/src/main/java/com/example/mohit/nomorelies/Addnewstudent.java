package com.example.mohit.nomorelies;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Addnewstudent extends AppCompatActivity {
    private EditText mname;
    private EditText mrollno;
    private Button madd;
    private DatabaseReference mdatabase;
    private ProgressDialog mProgress;
    private Button btn;
    private long i;
    static String temp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addnewstudent);

        Bundle b = getIntent().getExtras();
        mname = (EditText) findViewById(R.id.name);
        mrollno = (EditText) findViewById(R.id.rollno);
        madd = (Button) findViewById(R.id.add);
        mProgress=new ProgressDialog(this);
        final String id = b.getString("id");
        temp=id;
        final String teach=id+" "+"Class_Students";
        mdatabase = FirebaseDatabase.getInstance().getReference().child(teach);
        mdatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                i = dataSnapshot.getChildrenCount();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        madd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              addnewstudent(i);
            }

        });
    }
    private void addnewstudent(long i){
        final String rollno=mrollno.getText().toString().trim();
        final String name=mname.getText().toString().trim();
        if(!TextUtils.isEmpty(rollno) && !TextUtils.isEmpty(name)){
            mProgress.setMessage("adding");
            mProgress.show();
            DatabaseReference d = mdatabase.child("Student"+i);
            d.child("name").setValue(name);
            d.child("rollno").setValue(rollno);
            mProgress.dismiss();
            Intent mainIntent = new Intent(Addnewstudent.this,Teachrec.class);
            Bundle b = new Bundle();
            b.putString("id",temp);
            mainIntent.putExtras(b);
            mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(mainIntent);
        }
    }
}


