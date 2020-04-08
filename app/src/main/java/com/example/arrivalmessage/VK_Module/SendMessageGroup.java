package com.example.arrivalmessage.VK_Module;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;

import java.net.HttpURLConnection;

public class SendMessageGroup {

    private String acceskey ;
    private RequestQueue queue;
    private String querly;
    private static final String TAG = "MES";
    private static int count ;
    private static StringRequest prev;

    SendMessageGroup(String key, RequestQueue queuet)
    {
        prev = null;
        acceskey = key;
        queue = queuet;
        count = 0;
    }


    public String SendMessage(int id,String message)
    {
        querly = "https://api.vk.com/method/messages.send";
        querly=querly + "?message="+message;
        querly=querly + "&peer_id="+id;
        querly=querly + "&v=5.67";
        querly=querly + "&access_token="+acceskey;

        if(prev!=null)
            prev.cancel();
        String ans=null;


        StringRequest stringRequest = new StringRequest(Request.Method.GET, querly,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println(response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("ERROR");
            }
        });

        prev = stringRequest;
        queue.add(stringRequest);

        return ans;
    }
}
