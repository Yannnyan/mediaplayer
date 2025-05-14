package com.main.mediaplayer.services;

import android.content.Context;
import android.os.Environment;

import com.main.mediaplayer.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;

public class UpdateService {
    private String updateListPath;
    private ArrayList<String> updateList = new ArrayList<>();
    public UpdateService(Context context) throws IOException {
        updateListPath = String.valueOf(Environment.getExternalStorageDirectory());;
        File list = new File(updateListPath);
        if (list.exists()) {

        }
        else {
            list.createNewFile();
        }
    }
}
