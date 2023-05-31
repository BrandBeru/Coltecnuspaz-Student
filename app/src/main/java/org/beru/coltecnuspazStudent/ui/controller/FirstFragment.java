package org.beru.coltecnuspazStudent.ui.controller;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.storage.StorageManager;
import android.provider.Settings;
import android.view.*;

import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import org.beru.coltecnuspazStudent.R;
import org.beru.coltecnuspazStudent.databinding.FragmentFirstBinding;
import org.beru.coltecnuspazStudent.ui.model.ConnectorSSH;
import org.beru.coltecnuspazStudent.ui.model.Datas;
import org.beru.coltecnuspazStudent.ui.model.Utils;
import org.beru.coltecnuspazStudent.ui.view.FilesInfoAdapter;

import java.util.ArrayList;
import java.util.List;

public class FirstFragment extends Fragment {

    private FragmentFirstBinding binding;

    ListView viewPane;
    View view;
    FilesInfoAdapter adapter;
    public ProgressBar progressBar;

    public List<String> filesSelected = new ArrayList<>();

    public static FirstFragment instance;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        adapter = new FilesInfoAdapter(getContext(), ConnectorSSH.instance.getFiles());
        binding = FragmentFirstBinding.inflate(inflater, container, false);

        instance = this;
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setHasOptionsMenu(true);

        this.view = view;

        progressBar = view.findViewById(R.id.progress);
        
        updateView();
        binding.update.setOnClickListener(view1 -> {
            updateView();
        });
        binding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Datas.getPaths().size() > 1){
                    Datas.getPaths().remove(Datas.getPaths().size()-1);
                    updateView();
                }
            }
        });

        viewPane.setOnItemClickListener((parent, view12, position, id) -> {
            if(adapter.isDir(position)) {
                Datas.addPath(adapter.getItem(position).getFilename());
                updateView();
            }
            else
                downloadFile(adapter.getItem(position).getFilename());
            System.out.println("id: "+Datas.path_id);
            Datas.path_id++;
        });
    }
    private void downloadFile(String name){
        AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
        alert.setTitle("¿Desea descargar el archivo?");
        alert.setMessage("Nombre del archivo: "+name);
        alert.setPositiveButton("Descargar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                try{
                    StorageManager storage = (StorageManager) getContext().getSystemService(Context.STORAGE_SERVICE);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                        ConnectorSSH.instance.downloadFile(name, storage.getPrimaryStorageVolume().getDirectory().getAbsolutePath() + "/Download/" + name);
                    } else
                        ConnectorSSH.instance.downloadFile(name, Environment.getExternalStorageDirectory().getPath() + "/Download/" + name);
                    Toast.makeText(getContext(), "Downloaded successfully", Toast.LENGTH_SHORT).show();
                }catch(Exception e) {
                    Toast.makeText(getContext(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
        alert.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                return;
            }
        });
        alert.show();
    }
    public void updateView(){
        binding.path.setText(Datas.getLastPath());
        adapter = new FilesInfoAdapter(getContext(), ConnectorSSH.instance.getFiles());
        viewPane = view.findViewById(R.id.listViewPane);
        viewPane.setAdapter(adapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        if(!Utils.isPermissionGranted(getContext()))
        {
            new AlertDialog.Builder(getContext())
                    .setTitle("All files permission")
                    .setMessage("Due to android 11 restrictions, this app requires all files permission")
                    .setPositiveButton("Allow", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            takePermission();
                        }
                    })
                    .setNegativeButton("Deny", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            return;
                        }
                    }).setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        }
        else
            Toast.makeText(getContext(),"Permission already granted", Toast.LENGTH_SHORT).show();
    }

    private void takePermission() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            try {
                Intent intent = new Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
                intent.addCategory("android.intent.category.DEFAULT");
                Uri uri = Uri.fromParts("package", getContext().getPackageName(), null);
                intent.setData(uri);
                startActivityForResult(intent, 101);
            } catch (Exception e) {
                e.printStackTrace();
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
                startActivityForResult(intent, 101);
            }
        }else{
            ActivityCompat.requestPermissions(getActivity(), new String[]{
                    Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE
            },101);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults.length>0){
            if(requestCode==10){
                boolean readExt = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                if(!readExt)
                    takePermission();
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}