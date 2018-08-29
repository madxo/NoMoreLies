package com.example.mohit.nomorelies;

import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;


public class Attendance extends AppCompatActivity {
    private Button setdate;
    private Button settime;
    private Button next;
    private CheckBox ch;
    private Button Submit;
    private TextView name;
    private Button Show;
    private DatabaseReference mRef, nref;
    private ArrayList<String> Slist;
    private int i = 0;
    private DatabaseReference aref;
    private static String DATE;
    private static String TIME;
    private static String temp;

    @Override
    public void onBackPressed() {
        finishAffinity();
        super.onBackPressed();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance);
        setdate = (Button) findViewById(R.id.SetDate);
        settime = (Button) findViewById(R.id.SetTime);
        next = (Button) findViewById(R.id.Next);
        name = (TextView) findViewById(R.id.Name);
        Show = (Button) findViewById(R.id.ShowStudents);
        ch = (CheckBox) findViewById(R.id.rollno);
        Bundle b = getIntent().getExtras();
        assert setdate != null;
        assert settime != null;
        setdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerFragment mDatePicker = new DatePickerFragment();
                mDatePicker.show(getFragmentManager(), "Select date");
            }
        });
        settime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePicker mTimePicker = new TimePicker();
                mTimePicker.show(getFragmentManager(), "Select time");
            }
        });

        final String id = b.getString("id");
        temp = id;
        final String teach = id + " " + "Class_Students";
        mRef = FirebaseDatabase.getInstance().getReference().child(teach);

        Show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(final DataSnapshot dataSnapshot) {
                            Slist = new ArrayList<String>();
                        for (DataSnapshot dsp : dataSnapshot.getChildren()) {
                            Slist.add(String.valueOf(dsp.getValue()));
                            ch.setText("        " + Slist.get(0).replace("{", "").replace("}", "").replace("name=", "").replace("rollno=", ""));
                            next.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    String str = Slist.get(i).replace("{", "").replace("}", "").replace("name=", "").replace("rollno=", "");
                                    int l = str.length();
                                    aref = FirebaseDatabase.getInstance().getReference().child("attendances").child(DATE + " " + TIME + "Attendance");
                                    if (ch.isChecked()) {
                                        aref.child(str.substring(l - 9, l)).setValue("Yes");
                                        ch.toggle();
                                    } else {
                                        aref.child(str.substring(l - 9, l)).setValue("No");
                                        SimpleDateFormat sdf = new SimpleDateFormat("dd");
                                        String date1 = sdf.format(Calendar.getInstance().getTime());
                                        nref = FirebaseDatabase.getInstance().getReference().child("Absent").child(date1 + "Attendance");

                                        nref.push().setValue(str.substring(l - 9, l));
                                    }
                                    i++;

                                    if (i >= Slist.size()) {
                                        next.setVisibility(View.GONE);
                                        Bundle b = new Bundle();
                                        b.putString("id", temp);
                                        Intent intent = new Intent(Attendance.this, Teachrec.class);
                                        intent.putExtras(b);
                                        startActivity(intent);
                                    } else {
                                        str = Slist.get(i).replace("{", "").replace("}", "").replace("name=", "").replace("rollno=", "");
                                        ch.setText("        " + str);
                                    }
                                }
                            });
                        }


                }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
            }

        });


    }

    public static class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);
            String date = day + "" + month + "" + year;
            DATE = date;
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            String date = day + "" + month + "" + year;
            //DATE = date;
        }
    }

    public static class TimePicker extends DialogFragment implements TimePickerDialog.OnTimeSetListener {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar c = Calendar.getInstance();
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);
            String time = hour + " " + minute;
            TIME = time;
            return new TimePickerDialog(getActivity(), this, hour, minute, DateFormat.is24HourFormat(getActivity()));
        }

        @Override
        public void onTimeSet(android.widget.TimePicker view, int hourOfDay, int minute) {
            String time = hourOfDay + " " + minute;
            //  TIME = time;
        }
    }


}
