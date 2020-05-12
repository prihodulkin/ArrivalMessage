package com.example.arrivalmessage.VK_Module;

public class NotificationData {
    public int[] idChosenFriends;
    public Double latitude;
    public Double longitude;
    public String writtenText;
    public String location;
    public int isEnabled;
    public String[] displayFriends;
    public int user_id;
    public int hours;
    public int minutes;
    public int days;
    public int flag;

    public NotificationData() {
        idChosenFriends = new int[0];
        writtenText = "";
    }


    public NotificationData(String isCF, double lat, double longt, String wT, int isEn, String loc, int us_id, int d, int h, int m, int f) {
        String[] idss = isCF.split(" ");
        idChosenFriends = new int[idss.length];
        for (int i = 0; i < idss.length; i++)
            idChosenFriends[i] = Integer.parseInt(idss[i]);
        latitude = lat;
        longitude = longt;
        writtenText = wT;
        isEnabled = isEn;
        location = loc;
        user_id = us_id;
        days = d;
        hours = h;
        minutes = m;
        flag = f;

    }
}