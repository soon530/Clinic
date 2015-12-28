package com.wmtw.clinic;

import android.content.Context;

import com.google.gson.Gson;
import com.squareup.okhttp.OkHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;

import de.greenrobot.event.EventBus;

public class ServerApis {
    private static final OkHttpClient client = new OkHttpClient();

    public static String parseResource(Context context, int resource) throws IOException {
        InputStream is = context.getResources().openRawResource(resource);
        Writer writer = new StringWriter();
        char[] buffer = new char[1024];
        try {
            Reader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            int n;
            while ((n = reader.read(buffer)) != -1) {
                writer.write(buffer, 0, n);
            }
        } finally {
            is.close();
        }

        return writer.toString();
    }

    public static <T> void asyncPostFake(String jsonData, Class<T> clazz) {
        Gson gson = new Gson();
        T myReturn = gson.fromJson(jsonData, clazz);
        EventBus.getDefault().post(myReturn);
    }

}
