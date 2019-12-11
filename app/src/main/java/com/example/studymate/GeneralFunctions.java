package com.example.studymate;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.JsonObject;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;

public class GeneralFunctions {
    public static String getEmail(Activity context) {
        FirebaseUser acct = FirebaseAuth.getInstance().getCurrentUser();
        try {
            return acct.getEmail();
        } catch (Exception e) {
            System.out.println("Could not return email of user");
            e.printStackTrace();
            return "";
        }
    }



    public static Bitmap getProfilePic(Activity context) {
        FirebaseUser acct = FirebaseAuth.getInstance().getCurrentUser();
        Uri uri = acct.getPhotoUrl();


        return getBitmapFromURL(uri.toString());
    }

    protected static Bitmap getBitmapFromURL(String str) {
        try {
            java.net.URL url = new java.net.URL(str);
            HttpURLConnection connection = (HttpURLConnection) url
                    .openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * obtained from https://gist.github.com/jewelzqiu/c0633c9f3089677ecf85
     * @param bitmap bitmap to turn into a circle
     * @return circle for bitmap
     */
    public static Bitmap getCircledBitmap(Bitmap bitmap) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        canvas.drawCircle(bitmap.getWidth() / 2, bitmap.getHeight() / 2, bitmap.getWidth() / 2, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return output;
    }

    /**
     * Saves any data type to a specified location in the current database.
     * @param reference The name of the location in the database
     * @param value The data to save
     */
    public static void writeToDatabase(String reference, JsonObject value, String postRequest) {
        FirebaseDatabase currentDatabase = FirebaseDatabase.getInstance();
        switch(postRequest) {
            case "sitDown":
                // send shit w/ info about user sitting down
                String email = value.getAsJsonPrimitive("Email").getAsString();
                String path = "users" + email;

                DatabaseReference referenceToWrite = currentDatabase.getReference(path);
                referenceToWrite.setValue(email);

                referenceToWrite = currentDatabase.getReference(path + "Latitude");
                referenceToWrite.setValue(value.getAsJsonPrimitive("Latitude").getAsDouble());

                referenceToWrite = currentDatabase.getReference(path + "Longitude");
                referenceToWrite.setValue(value.getAsJsonPrimitive("Longitude").getAsDouble());
        }

    }

    /**
     * Deletes all the data at the specified location in the database
     * @param reference The name of the location in the database
     */
    public static void deleteDatafromDatabase(String reference) {
        FirebaseDatabase currentDatabase = FirebaseDatabase.getInstance();
        DatabaseReference referenceToClear = currentDatabase.getReference(reference);
        referenceToClear.removeValue();
    }

    public static SearchResultData[] pullUsersFromDatabase() {

    }
}
