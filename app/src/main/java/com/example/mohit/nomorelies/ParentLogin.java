package com.example.mohit.nomorelies;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ParentLogin extends AppCompatActivity {
    SharedPreferences sharedPreferences;
    private EditText mrollno;
    private EditText mloginemailField;
    private EditText mloginpasswordField;
    private Button mloginBtn;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private Button mlinkBtn;
    private ProgressDialog mProgess;
    SharedPreferences.Editor edit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parent_login);
        mAuth = FirebaseAuth.getInstance();
        sharedPreferences=getSharedPreferences("mydata", Context.MODE_PRIVATE);
        edit=sharedPreferences.edit();
        if(sharedPreferences.getString("rollno","").equals(""))
        {

        }
        else
        {
            Intent mainIntent= new Intent(ParentLogin.this,Sturecord.class);
            startActivity(mainIntent);

        }
        mDatabase= FirebaseDatabase.getInstance().getReference().child("Students");
        mloginemailField=(EditText)findViewById(R.id.loginemailField);
        mloginpasswordField=(EditText)findViewById(R.id.loginpasswordField);
        mloginBtn=(Button)findViewById(R.id.loginBtn);
        mlinkBtn=(Button)findViewById(R.id.linkbtn);
        mrollno=(EditText)findViewById(R.id.rollno);
        mProgess = new ProgressDialog(this);
        mlinkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ParentLogin.this,ParentRegister.class));
            }
        });
        mloginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                //    startService(new Intent(getBaseContext(), MyService.class));

                checkLogin();
            }
        });
    }



    private void checkLogin() {
        final String rollno=mrollno.getText().toString().trim();
        String email= mloginemailField.getText().toString().trim();
        String password = mloginpasswordField.getText().toString().trim();
        if(!TextUtils.isEmpty(rollno) && !TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)){
            mProgess.setMessage("logging in");
            mProgess.show();
            mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        mProgess.dismiss();
                        checkuserexist(rollno);
                    }
                    else{
                        Toast.makeText(ParentLogin.this,"Invalid email or password",Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }

    private void checkuserexist(final String rollno) {
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild(rollno)){
                    Intent mainIntent= new Intent(ParentLogin.this,Sturecord.class);
                    Bundle b =new Bundle();
                    b.putString("rollno",rollno);
                    mainIntent.putExtras(b);
                    mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                    edit.putString("rollno",rollno);
                    edit.commit();
                    startActivity(mainIntent);
                }else{
                    Toast.makeText(ParentLogin.this,"Register an account first",Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}