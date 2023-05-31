package org.beru.coltecnuspazStudent.ui.controller;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.view.MenuCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.appcompat.app.AppCompatActivity;

import org.beru.coltecnuspazStudent.R;
import org.beru.coltecnuspazStudent.databinding.ActivityClientBinding;
import org.beru.coltecnuspazStudent.ui.model.ConnectorSSH;
import org.jetbrains.annotations.NotNull;

public class ClientActivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private ActivityClientBinding binding;

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityClientBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_client);
        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
    }
    @Override
    public boolean onCreateOptionsMenu(@NonNull @NotNull Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.files_menu, menu);
        MenuCompat.setGroupDividerEnabled(menu, true);
        return true;
    }
    private static final int FILE_SELECT_CODE = 0;
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.close:
                close();
                break;
            default:
                Toast.makeText(getApplicationContext(), "Unrecognized", Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }
    private void close(){
        Intent intent = new Intent(ClientActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_client);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }
}