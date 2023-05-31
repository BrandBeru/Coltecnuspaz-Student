package org.beru.coltecnuspazStudent.ui.model;

import com.jcraft.jsch.*;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Stream;

public class ConnectorSSH {

    public Session session;
    public ChannelSftp sftp;
    public static ConnectorSSH instance;
    public ConnectorSSH(String user, String pass) throws JSchException{

        session = new JSch().getSession(user, "192.168.1.100", 22);
        session.setPassword(pass);
        session.setConfig("PreferredAuthentications", "password");
        session.setConfig("StrictHostKeyChecking", "no");

        instance = this;
    }
    public void connect(String grade) throws Exception{
            Thread t1 = new Thread(() -> {
                try {
                    session.connect();
                } catch (JSchException e) {
                    e.printStackTrace();
                }
            });
            Thread t2 = new Thread(() ->{
                try{
                    sftp.connect();

                    Datas.setPaths(new LinkedList<>());
                    Datas.getPaths().add(sftp.getHome()+"/"+grade+"/tareas");
                }catch (JSchException e){
                    e.printStackTrace();
                } catch (SftpException e) {
                    throw new RuntimeException(e);
                }
            });
            t1.start();t1.join();
            sftp = (ChannelSftp) session.openChannel("sftp");
            t2.start();t2.join();
    }
    public void reconnect(){
        try {
            sftp = (ChannelSftp) session.openChannel("sftp");
        } catch (JSchException e) {
            e.printStackTrace();
        }
        Thread t = new Thread(() -> {
            try {
                sftp.connect();
            } catch (JSchException e) {
                e.printStackTrace();
            }
        });
        t.start();
        try {
            t.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
    public List<ChannelSftp.LsEntry> getFiles(){
        List<ChannelSftp.LsEntry> filesInfo = new ArrayList<>();
        Thread t1 = new Thread(() -> {
            ChannelSftp.LsEntrySelector selector = new ChannelSftp.LsEntrySelector() {
                @Override
                public int select(ChannelSftp.LsEntry entry) {
                    final String fileName = entry.getFilename();
                    if(fileName.equals(".") || fileName.equals(".."))
                        return CONTINUE;
                    else
                        filesInfo.add(entry);
                    return CONTINUE;
                }
            };
            try {
                sftp.ls(Datas.getLastPath(), selector);
            } catch (SftpException e) {
                e.printStackTrace();
            }
        });
        t1.start();
        try {
            t1.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Collections.sort(filesInfo, new SortItems());

        return filesInfo;
    }
    public void downloadFile(String name, String dst){
        Thread t1 = new Thread(() -> {
            try {
                sftp.get(Datas.getLastPath()+"/"+name, dst, new ProgressMonitor());
            } catch (SftpException e) {
                e.printStackTrace();
            }
        });
        t1.start();
    }
}
