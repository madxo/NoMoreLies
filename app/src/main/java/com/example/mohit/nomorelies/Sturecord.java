package com.example.mohit.nomorelies;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class Sturecord extends AppCompatActivity {
    private Button mlogout;
    private TextView mname;
    private TextView attendanceview;
    private DatabaseReference mDatabase, mRef,nref;
    private ArrayList<String> atten;
    private int count;
    private float no,n,y;
    private int zz=1;
    SharedPreferences.Editor edit;
    SharedPreferences sharedPreferences;

    @Override
    public void onBackPressed() {

        finishAffinity();
        System.exit(0);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sturecord);
        mname = (TextView) findViewById(R.id.name);
        mlogout=(Button)findViewById(R.id.button2);
        attendanceview = (TextView) findViewById(R.id.attendanceview);
       // Bundle b = getIntent().getExtras();
        sharedPreferences=getSharedPreferences("mydata", Context.MODE_PRIVATE);
        edit=sharedPreferences.edit();

        final String rollno=sharedPreferences.getString("rollno","");
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Students");
        mRef = FirebaseDatabase.getInstance().getReference().child("attendances");
        SimpleDateFormat sdf=new SimpleDateFormat("dd");
        String date=sdf.format(Calendar.getInstance().getTime());
        nref=FirebaseDatabase.getInstance().getReference().child("Absent").child(date + "Attendance");

        mlogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent TIntent = new Intent(Sturecord.this,MainActivity.class);
                edit.putString("rollno","");
                edit.commit();
                startActivity(TIntent);
            }
        });

        nref.addChildEventListener(new ChildEventListener() {

            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {


                if(dataSnapshot.getValue().equals(rollno)){
                    String str=dataSnapshot.getValue().toString()+" Subject Bunk : "+zz;
                    zz++;
                    ff(str);
                }
                else{
                   // Toast.makeText(Sturecord.this, "hi", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseerror) {

            }
        });
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String name = dataSnapshot.child(rollno).child("name").getValue().toString().toUpperCase();
                mname.setText("Welcome, " + name);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        mRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(final DataSnapshot dataSnapshot) {
                no=0;
                y=0;
                n=0;;
                for (DataSnapshot dsp : dataSnapshot.getChildren()) {
                    for(DataSnapshot dsp1:dsp.getChildren()){
                        String str=dsp1.getKey();
                        if(str.equals(rollno)){
                            no++;
                            if(dsp1.getValue().equals("Yes"))
                                y++;
                            else
                                n++;
                        }
                    }
                }
                float p=(y/no)*100;
                attendanceview.setText("Your attendance is "+p+"% ,\n\n"+"No of presents:"+y+"\n\n No of absents:"+n+" ");
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }
    public void ff(String roll){
       NotificationCompat.Builder builder=new NotificationCompat.Builder(this);
        NotificationCompat.BigTextStyle style=new NotificationCompat.BigTextStyle(builder);
        style.bigText("Your Ward is Absent Today from class ");
        style.setBigContentTitle("Warning!!! Bunk Notice");
        style.setSummaryText("Your Ward is Absent Today from class ");
        Notification notification=builder.setContentTitle(roll).setContentText("Your Ward is Absent Today from class ").setSmallIcon(R.mipmap.ic_launcher).build();
        NotificationManagerCompat notificationmanager = NotificationManagerCompat.from(this);
        notificationmanager.notify(123456789,notification);




    }
}
