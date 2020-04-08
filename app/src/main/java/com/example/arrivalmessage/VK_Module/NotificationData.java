package com.example.arrivalmessage.VK_Module;
public class NotificationData {
    public int[] idChosenFriends_;
    public double latitude_;
    public double longitude_;
    public String writtenText_;
    public String location_;
    public int isEnabled_;

    public NotificationData(String isCF, double lat, double longt, String wT, int isEn, String location) {
        String[] idss = isCF.split(" ");
        idChosenFriends_ = new int[idss.length];
        for (int i = 0; i < idss.length; i++)
            idChosenFriends_[i] = Integer.parseInt(idss[i]);
        latitude_ = lat;
        longitude_ = longt;
        writtenText_ = wT;
        isEnabled_ = isEn;
        location_ = location;
    }
}