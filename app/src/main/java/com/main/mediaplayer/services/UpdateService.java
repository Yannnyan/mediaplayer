package com.main.mediaplayer.services;

import android.content.Context;
import android.util.Log;

import com.main.mediaplayer.R;
import com.main.mediaplayer.events.DownloadEvent;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class UpdateService {
    private class UpdateListKeyValue {
        String key;
        Boolean value;
        UpdateListKeyValue(String line) {
            String[] keyValue = getKeyValueFromLine(line);
            key = keyValue[0];
            value = Objects.equals(keyValue[1], "true");
        }
        private String[] getKeyValueFromLine(String line) {
            return line.split(" ");
        }
    }
    private String updateListPath;
    private String downloadPlaylistPath;
    private Map<String, Boolean> updateList = new HashMap<>();
    public UpdateService(Context context) {
        updateListPath = context.getString(R.string.update_list_path);
        downloadPlaylistPath = context.getString(R.string.playlist_path);
    }

    private boolean readUpdateListFromFile() {
        try{
            File list = new File(updateListPath);
            if (list.exists()) {
                FileInputStream inStream = new FileInputStream(list);
                byte[] buffer = new byte[8192];
                StringBuilder builder = new StringBuilder();
                int offset = 0;
                int readAmount;
                do {
                    readAmount = inStream.read(buffer,offset,buffer.length);
                    builder.append(new String(buffer,0,readAmount, StandardCharsets.UTF_8));
                    offset += readAmount;
                }
                while (readAmount > 0);
                // Parse output into the update list
                for (String mem:
                     builder.toString().split("\n")) {
                    UpdateListKeyValue keyValue = new UpdateListKeyValue(mem);
                    updateList.put(keyValue.key, keyValue.value);
                }
                Log.i("INFO", "Finished reading update list");
            }
            else {
                boolean isFileCreated = list.createNewFile();
                if (!isFileCreated) {
                    Log.e("ERROR", "Couldn't create file");
                    return false;
                }
            }
        }
        catch (IOException e) {
            Log.e("ERROR", Objects.requireNonNull(e.getMessage()));
            return false;
        }
        return true;
    }

    private List<String> getDownloadListChanges() {

    }
    private void downloadSchedule() {
        try {
            while (true)
            {
                Thread.sleep(900000);
                if () {

                }
                EventBus.getDefault().post(new DownloadEvent(DownloadEvent.DownloadEventType.DOWNLOADING));
            }
        }
        catch (InterruptedException e) {
            Log.i("INFO", "Download thread stopped got interrupted");
        }
    }
}
