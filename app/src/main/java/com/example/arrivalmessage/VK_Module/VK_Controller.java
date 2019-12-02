package com.example.arrivalmessage.VK_Module;


import com.android.volley.RequestQueue;
import com.vk.api.sdk.VK;
import com.vk.api.sdk.VKApiCallback;
import com.vk.api.sdk.exceptions.VKApiExecutionException;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class VK_Controller {

    private VKFriendsRequest friendsRequest;
    private SendMessageGroup sendMessageGroup;
    List<VKUser> friends;

    public VK_Controller(RequestQueue queue, String key)
    {
        friendsRequest = new VKFriendsRequest();
        sendMessageGroup = new SendMessageGroup(key,queue);

        UpdateFriends();
    }

    public List<VKUser> GetFriends()
    {
        return friends;
    }

    public void UpdateFriends()
    {
        VK.execute(new VKFriendsRequest(), new VKApiCallback<List<VKUser>>() {
            @Override
            public void success(List<VKUser> vkUsers) {
                friends = vkUsers;
            }

            @Override
            public void fail(@NotNull Exception e) {

            }
        });
    }

    public void SendMessage(int id, String message)
    {
        sendMessageGroup.SendMessage(id,message);
    }

}
