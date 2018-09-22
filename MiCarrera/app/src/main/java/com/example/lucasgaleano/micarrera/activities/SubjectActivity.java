package com.example.lucasgaleano.micarrera.activities;

import android.arch.lifecycle.Observer;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.example.lucasgaleano.micarrera.R;
import com.example.lucasgaleano.micarrera.database.Exam;
import com.example.lucasgaleano.micarrera.database.Repository;
import com.example.lucasgaleano.micarrera.database.Subject;
import com.example.lucasgaleano.micarrera.view.ListaView;

import java.util.Calendar;
import java.util.List;

public class SubjectActivity extends AppCompatActivity {

    private TextView mtv;
    private Switch mSwitch;
    private String subjectName;
    private Repository repo;
    private Subject sub;
    private int subjectState;
    private ListaView LM,LM2;


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subject);

        repo = new Repository(getApplication());
        Intent intent = getIntent();
        subjectState = intent.getIntExtra(TreeActivity.EXTRA_STATE_SUBJECT,-1);
        subjectName = intent.getStringExtra(TreeActivity.EXTRA_NAME_SUBJECT);

        sub = new Subject(subjectName,
                subjectState,0,0);


        mtv = findViewById(R.id.textView2);
        mtv.setText(subjectName);
        mSwitch = findViewById(R.id.switch1);
        boolean b = subjectState==1;
        mSwitch.setChecked(b);
        mSwitch.setText("Dejar de cursar");

        mSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if(b)
                    sub.setState(getResources().getInteger(R.integer.HABILITADA));
                repo.updateSubject(sub);

            }
        });

        repo.getAllExam().observe(this, new Observer<List<Exam>>() {
                    @Override
                    public void onChanged(@Nullable final List<Exam> exams) {
                    LM.update(exams);
                    }
                });

        LM = findViewById(R.id.ListaM);
        LM.setHeader("Parciales");


        LM2 = findViewById(R.id.ListaM2);
        LM2.setHeader("Profesores:");
        LM2.addItem("Item");

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View view) {
                LM.addItem("Audio");
            }
        });
        FloatingActionButton fab2 = (FloatingActionButton) findViewById(R.id.fab2);
        fab2.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View view) {
                LM2.addItem("Foto");
            }
        });

    }

    private String formatDate(Calendar cal) {

        return String.valueOf(cal.get(Calendar.DAY_OF_MONTH)).concat(
                "/"+ String.valueOf(cal.get(Calendar.MONTH)).concat(
                        "/"+ String.valueOf(cal.get(Calendar.YEAR))));

    }
}
