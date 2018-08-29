package com.example.mohit.nomorelies;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Teachrec extends AppCompatActivity {
    @Override
    public void onBackPressed() {

        finish();
        super.onBackPressed();
    }

    private TextView mname;
    private DatabaseReference mDatabase;
    private Button mAttendance;
    private Button mAddnew;
    private Button mlogout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teachrec);

        Bundle b = getIntent().getExtras();
        mname = (TextView)findViewById(R.id.Name);
        mAttendance=(Button)findViewById(R.id.Attendancebtn);
        mAddnew=(Button)findViewById(R.id.NewStudentBtn);
        mlogout=(Button)findViewById(R.id.LogoutBtn);
        final String id=b.getString("id","");
        mDatabase= FirebaseDatabase.getInstance().getReference().child("Teachers");
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String name=dataSnapshot.child(id).child("name").getValue().toString().toUpperCase();
                mname.setText("Welcome, " + name);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        mAttendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent TIntent = new Intent(Teachrec.this,Attendance.class);
                Bundle b = new Bundle();
                b.putString("id",id);
                TIntent.putExtras(b);
                startActivity(TIntent);
            }
        });
        mAddnew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent TIntent = new Intent(Teachrec.this,Addnewstudent.class);
                Bundle b = new Bundle();
                b.putString("id",id);
                TIntent.putExtras(b);
                startActivity(TIntent);
            }
        });
        mlogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent TIntent = new Intent(Teachrec.this,MainActivity.class);
                startActivity(TIntent);
            }
        });


    }
}
