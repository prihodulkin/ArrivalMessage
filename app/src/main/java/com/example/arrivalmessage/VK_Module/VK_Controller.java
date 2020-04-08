package com.example.arrivalmessage.VK_Module;


import com.android.volley.RequestQueue;
import com.vk.api.sdk.VK;
import com.vk.api.sdk.VKApiCallback;
import com.vk.api.sdk.exceptions.VKApiExecutionException;

import org.jetbrains.annotations.NotNull;

import java.sql.Time;
import java.util.Date;
import java.util.List;

import kotlin.time.Clock;

public class VK_Controller {

    private VKFriendsRequest friendsRequest;
    private SendMessageGroup sendMessageGroup;
    private VKCheckMessagesAllowed vkcheckid;
    private GetUserInfo userInfo;

    public static List<VKUser> friends;
    public static int UserID;
    public static String FirstName;
    public static String SecondName;

    public VK_Controller(RequestQueue queue, String key, String group_id)
    {
        friendsRequest = new VKFriendsRequest();
        sendMessageGroup = new SendMessageGroup(key,queue);
        vkcheckid = new VKCheckMessagesAllowed(key,group_id, queue);
        userInfo = new GetUserInfo();
    }

    public List<VKUser> GetFriends()
    {
        UpdateFriends();
        return friends;
    }

    public void UpdateUserID()
    {
        VKApiCallback<VKUser> callback = new VKApiCallback<VKUser>() {
            @Override
            public void success(VKUser vkUser) {
                UserID = vkUser.id;
                FirstName=vkUser.firstname;
                SecondName = vkUser.lastname;
            }

            @Override
            public void fail(@NotNull Exception e) {

            }
        };
        VK.execute(userInfo, callback);
    }


    public void UpdateFriends()
    {
        VKApiCallback<List<VKUser>> callback = new VKApiCallback<List<VKUser>>() {
            @Override
            public void success(List<VKUser> vkUsers) {
                friends = vkUsers;
            }

            @Override
            public void fail(@NotNull Exception e) {

            }
        };
        VK.execute(friendsRequest, callback);

    }

    public void SendMessage(int id, String message)
    {
        sendMessageGroup.SendMessage(id,message);
    }

    public boolean CheckId(int id)
    {
        return vkcheckid.CheckID(id);
    }

}
