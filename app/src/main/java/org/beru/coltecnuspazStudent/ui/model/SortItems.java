package org.beru.coltecnuspazStudent.ui.model;

import com.jcraft.jsch.ChannelSftp;

import java.util.Comparator;

public class SortItems implements Comparator<ChannelSftp.LsEntry> {
    @Override
    public int compare(ChannelSftp.LsEntry o1, ChannelSftp.LsEntry o2) {
        String time = o1.getAttrs().getMtimeString().split("GMT")[0];
        String time2 = o2.getAttrs().getMtimeString().split("GMT")[0];
        return time2.compareTo(time);
    }
}
