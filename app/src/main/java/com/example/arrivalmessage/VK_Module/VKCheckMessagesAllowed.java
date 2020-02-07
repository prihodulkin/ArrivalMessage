package com.example.arrivalmessage.VK_Module;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

public class VKCheckMessagesAllowed {

    private String acceskey ;
    private String group_id;
    private RequestQueue queue;
    private String querly = "https://api.vk.com/method/messages.isMessagesFromGroupAllowed";

    private boolean ans;

    VKCheckMessagesAllowed(String key,String group_idk, RequestQueue queuet)
    {
        group_id = group_idk;
        acceskey = key;
        queue = queuet;
    }


    public boolean CheckID(int id)
    {

        querly=querly + "?group_id="+group_id;
        querly=querly + "&user_id="+id;
        querly=querly + "&v=5.103";
        querly=querly + "&access_token="+acceskey;

        ans=false;

        StringRequest stringRequest = new StringRequest(Request.Method.GET, querly,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject res = new JSONObject(response);
                            ans = res.getJSONObject("response").getInt("is_allowed") == 1 ? true: false;
                            System.out.println(ans);
                        }
                        catch (JSONException e)
                        {

                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("ERROR");
            }
        });


        queue.add(stringRequest);

        return ans;
    }

}
