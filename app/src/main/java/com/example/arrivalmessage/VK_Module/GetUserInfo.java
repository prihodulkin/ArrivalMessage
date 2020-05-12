package com.example.arrivalmessage.VK_Module;

import com.vk.api.sdk.VKApiManager;
import com.vk.api.sdk.VKApiResponseParser;
import com.vk.api.sdk.VKMethodCall;
import com.vk.api.sdk.exceptions.VKApiException;
import com.vk.api.sdk.internal.ApiCommand;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GetUserInfo extends ApiCommand<VKUser>
{

    @Override
    protected VKUser onExecute(@NotNull VKApiManager vkApiManager) throws InterruptedException, IOException, VKApiException {

        VKMethodCall.Builder a = new VKMethodCall.Builder();
        a.method("users.get");
        a.version("5.103");

        return vkApiManager.execute(a.build(), new ResponseApiParser());
    }

    private class ResponseApiParser implements VKApiResponseParser<VKUser>
    {
        @Override
        public VKUser parse(String response) throws VKApiException {
            try
            {

                JSONObject res = new JSONObject(response);
                JSONArray arr = res.getJSONArray("response");
                JSONObject o = (JSONObject) arr.get(0);
                int id = o.getInt("id");
                String last = o.getString("last_name");
                String first = o.getString("first_name");

                VKUser user = new VKUser();
                user.id=id;
                user.lastname=last;
                user.firstname=first;

                return user;
            }
            catch (JSONException ex)
            {
                return null;
            }
        }
    }
}