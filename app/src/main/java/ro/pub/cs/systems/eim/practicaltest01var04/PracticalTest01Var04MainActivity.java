package ro.pub.cs.systems.eim.practicaltest01var04;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class PracticalTest01Var04MainActivity extends AppCompatActivity {

    private EditText text_field;
    private Button navigate_to_secondary_activity_button;
    private Button top_left;
    private Button top_right;
    private Button center;
    private Button bot_left;
    private Button bot_right;

    final private static String BROADCAST_RECEIVER_EXTRA = "message";
    final private static String BROADCAST_RECEIVER_TAG = "[Message]";

    final public static int SERVICE_STOPPED = 0;
    final public static int SERVICE_STARTED = 1;

    private int serviceStatus = SERVICE_STOPPED;

    private int nrClicks = 0;
    private int SECONDARY_ACTIVITY_REQUEST_CODE = 1;
    private IntentFilter intentFilter = new IntentFilter();

    private ButtonClickListener buttonClickListener = new ButtonClickListener();

    private class ButtonClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            nrClicks++;
            switch (view.getId()) {
                case R.id.navigate_to_secondary_activity_button:
                    Intent intent = new Intent(getApplicationContext(), PracticalTest01Var04SecondaryActivity.class);
                    String content = text_field.getText().toString();
                    intent.putExtra("Content", content);
                    startActivityForResult(intent, SECONDARY_ACTIVITY_REQUEST_CODE);
                    break;
                case R.id.top_left:
                    text_field.setText(text_field.getText().toString() + "Top Left");
                    break;
                case R.id.top_right:
                    text_field.setText(text_field.getText().toString() + "Top Right");
                    break;
                case R.id.center:
                    text_field.setText(text_field.getText().toString() + "Center");
                    break;
                case R.id.bot_left:
                    text_field.setText(text_field.getText().toString() + "Bot Left");
                    break;
                case R.id.bot_right:
                    text_field.setText(text_field.getText().toString() + "Bot Right");
                    break;
            }
            Intent intent = new Intent(getApplicationContext(), PracticalTest01Var04Service.class);
            intent.putExtra("ContentS", text_field.getText().toString());
            getApplicationContext().startService(intent);
            serviceStatus = SERVICE_STARTED;
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);

        savedInstanceState.putString("nrClicks", String.valueOf(nrClicks));
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practical_test01_var04_main);

        text_field = findViewById(R.id.text_field);
        navigate_to_secondary_activity_button = findViewById(R.id.navigate_to_secondary_activity_button);
        top_left = findViewById(R.id.top_left);
        top_right = findViewById(R.id.top_right);
        center = findViewById(R.id.center);
        bot_left = findViewById(R.id.bot_left);
        bot_right = findViewById(R.id.bot_right);

        navigate_to_secondary_activity_button.setOnClickListener(buttonClickListener);
        top_left.setOnClickListener(buttonClickListener);
        top_right.setOnClickListener(buttonClickListener);
        center.setOnClickListener(buttonClickListener);
        bot_left.setOnClickListener(buttonClickListener);
        bot_right.setOnClickListener(buttonClickListener);

        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey("nrClicks")) {
                nrClicks = Integer.parseInt(savedInstanceState.getString("nrClicks"));
            }
        }
        Toast.makeText(this, "nrclicks=" + nrClicks, Toast.LENGTH_LONG).show();
        Log.d("onCreate", String.valueOf(nrClicks));

        intentFilter.addAction("ro.pub.cs.systems.eim.practicaltest01var04.contentS");
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(messageBroadcastReceiver, intentFilter);
    }

    @Override
    protected void onPause() {
        unregisterReceiver(messageBroadcastReceiver);
        super.onPause();
    }

    private MessageBroadcastReceiver messageBroadcastReceiver = new MessageBroadcastReceiver();

    private class MessageBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d(BROADCAST_RECEIVER_TAG, intent.getStringExtra(BROADCAST_RECEIVER_EXTRA));
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == SECONDARY_ACTIVITY_REQUEST_CODE) {
            Toast.makeText(this, "The activity returned with result " + resultCode, Toast.LENGTH_LONG).show();
            nrClicks = 0;
            text_field.setText("");
        }
    }

    @Override
    protected void onDestroy() {
        Intent intent = new Intent(this, PracticalTest01Var04Service.class);
        stopService(intent);
        super.onDestroy();
    }
}
