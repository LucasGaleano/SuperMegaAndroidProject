package com.example.lucasgaleano.micarrera.activities;

import android.arch.lifecycle.Observer;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.example.lucasgaleano.micarrera.R;
import com.example.lucasgaleano.micarrera.database.Exam;
import com.example.lucasgaleano.micarrera.database.Repository;
import com.example.lucasgaleano.micarrera.database.Subject;
import com.example.lucasgaleano.micarrera.view.ListaView;
import com.example.lucasgaleano.micarrera.view.NavigationMenu;

import java.util.Calendar;
import java.util.List;

public class SubjectActivity extends AppCompatActivity {

    private Switch switchAprobada;
    private String subjectName;
    private Repository repo;
    private Subject sub;
    private int subjectState;
    private ListaView listaParciales, listaProfesores, listaExamenes;
    private DrawerLayout drawer;


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subject);

        initNavigationAndToolbar();

        repo = new Repository(getApplication());
        Intent intent = getIntent();
        subjectState = intent.getIntExtra(TreeActivity.EXTRA_STATE_SUBJECT,-1);
        subjectName = intent.getStringExtra(TreeActivity.EXTRA_NAME_SUBJECT);

        sub = new Subject(subjectName,
                subjectState,0,0);

        listaParciales = findViewById(R.id.listaParciales);
        listaProfesores = findViewById(R.id.listaProfesores);
        listaExamenes = findViewById(R.id.listaExamenes);


        this.setTitle(subjectName);
        setSwichts();
        setListas();


        switchAprobada.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if(b)
                    sub.setState(getResources().getInteger(R.integer.APROBADA));
                else
                    sub.setState(getResources().getInteger(R.integer.HABILITADA));
                repo.updateSubject(sub);

            }
        });

        repo.getExamsBySubject(subjectName).observe(this, new Observer<List<Exam>>() {
                    @Override
                    public void onChanged(@Nullable final List<Exam> exams) {
                    listaExamenes.update(exams);
                    }
                });



        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View view) {
                listaParciales.addItem("Audio");
            }
        });
        FloatingActionButton fab2 = (FloatingActionButton) findViewById(R.id.fab2);
        fab2.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View view) {
                listaExamenes.addItem("Foto");
            }
        });

    }

    private void setListas() {

        listaParciales.setHeader("Parciales");
        listaProfesores.setHeader("Profesores");
        listaExamenes.setHeader("Examenes");

    }

    private void setSwichts() {

        switchAprobada = findViewById(R.id.switchAprobada);
        if(sub.getState() != getResources().getInteger(R.integer.APROBADA))
            switchAprobada.setChecked(false);
        else
            switchAprobada.setChecked(true);



    }

    private void initNavigationAndToolbar() {

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        NavigationMenu Nav = new NavigationMenu(this,drawer);
        navigationView.setNavigationItemSelectedListener(Nav.getListener());
    }



    private String formatDate(Calendar cal) {

        return String.valueOf(cal.get(Calendar.DAY_OF_MONTH)).concat(
                "/"+ String.valueOf(cal.get(Calendar.MONTH)).concat(
                        "/"+ String.valueOf(cal.get(Calendar.YEAR))));

    }
}
