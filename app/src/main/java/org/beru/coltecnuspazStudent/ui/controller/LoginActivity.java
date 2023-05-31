package org.beru.coltecnuspazStudent.ui.controller;

import android.content.Intent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

import org.beru.coltecnuspazStudent.R;
import org.beru.coltecnuspazStudent.ui.model.ConnectorSSH;
import org.beru.coltecnuspazStudent.ui.model.Passwords;

public class LoginActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        final Spinner teacher = findViewById(R.id.docente);
        final Spinner grade = findViewById(R.id.grado);

        ArrayAdapter<String> teachAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.teachers));
        teachAdapter.setDropDownViewResource(androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);
        teacher.setAdapter(teachAdapter);

        ArrayAdapter<String> gradeAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.grades));
        gradeAdapter.setDropDownViewResource(androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);
        grade.setAdapter(gradeAdapter);

        final Button connection_btn = findViewById(R.id.connect);

        connection_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String user = teacher.getSelectedItem().toString().toLowerCase();
                connect(user, Passwords.getPassword(user), grade.getSelectedItem().toString());
            }
        });
    }
    public void connect(String u, String ps, String grade){
        try {
            new ConnectorSSH(u, ps).connect(grade);Intent intent = new Intent(LoginActivity.this, ClientActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
        }
    }

}