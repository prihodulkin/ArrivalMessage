package com.example.arrivalmessage.VK_Module;

import com.vk.api.sdk.requests.VKRequest;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class VKFriendsRequest extends VKRequest<List<VKUser>>
{
    int uid;

    VKFriendsRequest()
    {
        super("friends.get");
        uid = 0;

        if (uid != 0)
        {
            addParam("user_id", uid);
        }

        addParam("fields", "photo_200");
    }

    @Override
    public List<VKUser> parse(@NotNull JSONObject r) throws Exception {

        JSONArray res = r.getJSONObject("response").getJSONArray("items");
        List<VKUser> users = new ArrayList<VKUser>();

        for(int i=0; i<res.length();i++)
            users.add(VKUser.parse(res.getJSONObject(i)));

        return users;
    }
}
