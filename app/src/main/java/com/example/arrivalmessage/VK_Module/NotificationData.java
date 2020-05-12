package com.example.arrivalmessage.VK_Module;

public class NotificationData {
    public int[] idChosenFriends;
    public Double latitude;
    public Double longitude;
    public String writtenText;
    public String location;
    public int isEnabled;
    public String[] displayFriends;
    public int hours;
    public int minutes;
    public int days;
    public int flag;

    public NotificationData() {
        idChosenFriends = new int[0];
        writtenText = "";
    }


    public NotificationData(String isCF, double lat, double longt, String wT, int isEn, String loc) {
        String[] idss = isCF.split(" ");
        idChosenFriends = new int[idss.length];
        for (int i = 0; i < idss.length; i++)
            idChosenFriends[i] = Integer.parseInt(idss[i]);
        latitude = lat;
        longitude = longt;
        writtenText = wT;
        isEnabled = isEn;
        location = loc;
        days=1;
        hours=0;
        minutes=0;
        flag=1;

    }
}