package com.example.arrivalmessage;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.Log;

import com.example.arrivalmessage.VK_Module.VKUser;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class DownloadImagesTask extends AsyncTask<CircularImageView, Void, Bitmap> {

    private CircularImageView image;

    public DownloadImagesTask(CircularImageView image) {
        this.image = image;
    }

    CircularImageView imageView = null;

    @SuppressLint("WrongThread")
    @Override
    protected Bitmap doInBackground(CircularImageView... imageViews) {
        this.imageView = imageViews[0];
        VKUser user=(VKUser)image.getTag();
        while (true) {
            Log.i("Загрузка изображения", "Загрузка идёт. Пользователь "+user.firstname+" "+user.lastname );
            if (image.getDrawable() != null) {
                Log.i("Загрузка изображения", "Загрузка завершена");
                break;
            }
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return null;
        /*
        return download_Image((String) imageView.getTag());*/
    }


    @Override
    protected void onPostExecute(Bitmap result) {
        for (CircularImageView i : SelectUserActivity.images.values())
            if (i.getTag() == image.getTag())
                i.setImageDrawable(image.getDrawable());

        // SelectUserActivity.images.get(user.id).setImageBitmap(result);
    }



  /*  private Bitmap download_Image(String url) {


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
    }*/
};