package org.beru.coltecnuspazStudent.ui.model;

import android.widget.Toast;

import com.jcraft.jsch.SftpProgressMonitor;

import org.beru.coltecnuspazStudent.ui.controller.FirstFragment;

public class ProgressMonitor implements SftpProgressMonitor {
    @Override
    public void init(int op, String src, String dest, long max) {
        FirstFragment.instance.getActivity().runOnUiThread(() -> {
            FirstFragment.instance.progressBar.setMax((int)max);
            FirstFragment.instance.progressBar.setProgress(0);
        });
    }
    int progress;
    @Override
    public boolean count(long count) {
        progress += (int) count;
        FirstFragment.instance.getActivity().runOnUiThread(() -> {
            FirstFragment.instance.progressBar.setProgress(progress);
        });
        return true;
    }

    @Override
    public void end() {
        System.out.println("FINISHED!");
        FirstFragment.instance.getActivity().runOnUiThread(() -> {
            FirstFragment.instance.progressBar.setProgress(0);
            Toast.makeText(FirstFragment.instance.getContext(), "Successful", Toast.LENGTH_SHORT).show();
            ConnectorSSH.instance.reconnect();
            FirstFragment.instance.updateView();
        });
    }
}
