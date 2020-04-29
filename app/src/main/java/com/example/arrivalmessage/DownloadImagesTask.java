package com.example.arrivalmessage;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class DownloadImagesTask extends AsyncTask<CircularImageView, Void, Bitmap> {
    private  int id;

    public DownloadImagesTask(int id)
    {
        this.id=id;
    }
    CircularImageView imageView = null;

    @Override
    protected Bitmap doInBackground(CircularImageView... imageViews) {
        this.imageView = imageViews[0];
        return download_Image((String) imageView.getTag());
    }

    @Override
    protected void onPostExecute(Bitmap result) {

        imageView.setImageBitmap(result);
        SelectUserActivity.images.put(id,imageView);
    }



    private Bitmap download_Image(String url) {


        Bitmap bmp =null;
        try{
            URL ulrn = new URL(url);
            HttpURLConnection con = (HttpURLConnection)ulrn.openConnection();
            InputStream is = con.getInputStream();
            bmp = BitmapFactory.decodeStream(is);
            if (null != bmp)
                return bmp;

        }catch(Exception e){}
        return bmp;
    }
};