package com.example.mohit.nomorelies;

import android.app.ProgressDialog;
import android.content.Intent;
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

import org.w3c.dom.Text;

public class TeacherLogin extends AppCompatActivity {
    private EditText mid;
    private EditText mloginemailField;
    private EditText mloginpasswordField;
    private Button mloginBtn;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private Button mlinkBtn;
    private ProgressDialog mProgess;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_login);
        mAuth = FirebaseAuth.getInstance();
        mDatabase= FirebaseDatabase.getInstance().getReference().child("Teachers");
        mloginemailField=(EditText)findViewById(R.id.loginemailField);
        mloginpasswordField=(EditText)findViewById(R.id.loginpasswordField);
        mloginBtn=(Button)findViewById(R.id.loginBtn);
        mlinkBtn=(Button)findViewById(R.id.linkbtn);
        mid=(EditText)findViewById(R.id.id);
        mProgess = new ProgressDialog(this);
        mlinkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(TeacherLogin.this,TeacherRegister.class));
            }
        });
        mloginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                checkLogin();
            }
        });
    }

    private void checkLogin() {
        final String id=mid.getText().toString().trim();
        String email= mloginemailField.getText().toString().trim();
        String password = mloginpasswordField.getText().toString().trim();
        if(!TextUtils.isEmpty(id) && !TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)){
            mProgess.setMessage("logging in");
            mProgess.show();
            mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        mProgess.dismiss();
                        checkuserexist(id);
                    }
                    else{
                        Toast.makeText(TeacherLogin.this,"Invalid email or password",Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }

    private void checkuserexist(final String id) {
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild(id)){
                    Intent mainIntent= new Intent(TeacherLogin.this,Teachrec.class);
                    mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    Bundle b=new Bundle();
                    b.putString("id",id);
                    mainIntent.putExtras(b);
                    startActivity(mainIntent);
                }else{
                    Toast.makeText(TeacherLogin.this,"Register an account first",Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}