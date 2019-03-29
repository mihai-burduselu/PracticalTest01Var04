package ro.pub.cs.systems.eim.practicaltest01var04;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.Date;
import java.util.Random;

public class ProcessingThread extends Thread{
    private Context context = null;
    private boolean isRunning = true;

    private Random random = new Random();

    final private static String BROADCAST_RECEIVER_EXTRA = "message";
    final private static String BROADCAST_RECEIVER_TAG = "[Message]";

    private String contentS;

    public ProcessingThread(Context context, String contentS) {
        this.context = context;

        this.contentS = contentS;
    }

    @Override
    public void run() {
        Log.d("run() processing thread", "Thread has started!");
        while (isRunning) {
            sendMessage();
            sleep();
        }
        Log.d("run() processing thread", "Thread has stopped!");
    }

    private void sendMessage() {
        Intent intent = new Intent();
        intent.setAction("ro.pub.cs.systems.eim.practicaltest01var04.contentS");
        intent.putExtra(BROADCAST_RECEIVER_EXTRA,
                new Date(System.currentTimeMillis()) + " " + contentS);
        context.sendBroadcast(intent);
    }

    private void sleep() {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException interruptedException) {
            interruptedException.printStackTrace();
        }
    }

    public void stopThread() {
        isRunning = false;
    }
}
