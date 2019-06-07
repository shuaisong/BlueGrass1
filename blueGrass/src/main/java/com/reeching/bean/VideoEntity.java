package com.reeching.bean;

import android.support.annotation.NonNull;

import java.io.Serializable;

public class VideoEntity implements Serializable, Comparable<VideoEntity> {
    public int ID;
    public String title;
    public String filePath;
    public long size;
    public int duration;
    public boolean checked;
    public long date;
    public boolean compress;
    public int type;

    @Override
    public int compareTo(@NonNull VideoEntity o) {

        long i = this.date - o.date;//先按照时间排序
        if (i == 0) {
            i = this.ID - o.ID;//如果时间相等了再用Id进行排序
        }
        return (int) -i;
    }
}
