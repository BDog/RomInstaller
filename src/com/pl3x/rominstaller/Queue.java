package com.pl3x.rominstaller;

import com.pl3x.rominstaller.utils.Constants;
import android.content.Context;
import java.io.IOException;
import java.util.ArrayList;

public class Queue {
    private Context mContext;
    private ArrayList<String> queue;

    public Queue(Context context) {
        mContext = context;
        queue = new ArrayList<String>();
    }

    public void addQue(String s) {
        queue.add(s);
    }

    public void deleteQue(String s) {
        queue.remove(s);
    }

    public void writeToCache() {
        try {
            Helper.runSuCommand(mContext, "rm " + Constants.CACHE_FILE);
            for(int x = 0; x < queue.size(); x++) {
                Helper.runSuCommand(mContext, "echo \"" + queue.get(x) + "\" >> " + Constants.CACHE_FILE);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void rebootRecovery() {
        try {
            Helper.runSuCommand(mContext, "reboot recovery");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
