package org.beru.coltecnuspazStudent.ui.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.jcraft.jsch.ChannelSftp;
import org.beru.coltecnuspazStudent.R;

import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

public class FilesInfoAdapter extends BaseAdapter {

    Context context;
    List<ChannelSftp.LsEntry> files = new ArrayList<>();
    LayoutInflater inflater;
    public FilesInfoAdapter(Context ctx, List<ChannelSftp.LsEntry> files){
        this.context = ctx;
        this.files = files;
        inflater = LayoutInflater.from(ctx);
    }
    @Override
    public int getCount() {
        return files.size();
    }

    @Override
    public ChannelSftp.LsEntry getItem(int i) {
        return files.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }
    public boolean isDir(int id){
        return files.get(id).getAttrs().isDir();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflater.inflate(R.layout.activity_files_info, null);
        ChannelSftp.LsEntry file = files.get(i);
        TextView name = (TextView) view.findViewById(R.id.file_name);
        TextView date = (TextView) view.findViewById(R.id.date);
        TextView weight = (TextView) view.findViewById(R.id.weight);
        ImageView image = (ImageView) view.findViewById(R.id.file_image);
        String time = file.getAttrs().getMtimeString().split("GMT")[0];
        name.setText(file.getFilename());
        date.setText("Fecha: "+time);
        weight.setText("Tamaño: "+String.valueOf((file.getAttrs().getSize()/1024)) + " kB");

        if(file.getAttrs().isDir())
            image.setImageResource(R.drawable.baseline_folder_24);
        else
            image.setImageResource(R.drawable.baseline_insert_drive_file_24);
        return view;
    }
}
