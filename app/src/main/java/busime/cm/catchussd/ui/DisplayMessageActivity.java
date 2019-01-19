package busime.cm.catchussd.ui;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import busime.cm.catchussd.R;
import busime.cm.catchussd.util.Util;

public class DisplayMessageActivity extends AppCompatActivity {

    private static TelephonyManager myTel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_message);

        /**
         * Get the intent that started the activity and extract the activity
         */
        Intent intent = getIntent();
        String message = intent.getStringExtra(Util.EXTRA_MESSAGE);

        /**
         * Captute the layout's Textview and set the string as its text
         */
        TextView textView = findViewById(R.id.textView);
        textView.setText(message);

        Context context = getBaseContext();
        myTel = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            Log.d(Util.TAG,"READ PHONE STATE permission not granted request it to the User");
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_PHONE_STATE},
                    Util.PERMISSIONS_READ_PHONE_STATE);
            return;
        }
        else{
            Log.d(Util.TAG,"READ PHONE STATE permission already granted");
            Log.d("DEVICE VERSION: ", myTel.getDeviceSoftwareVersion());
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        Log.d(Util.TAG, "dialog onRequestPermissionsResult");
        switch (requestCode) {
            case Util.PERMISSIONS_READ_PHONE_STATE:
                // Check Permissions Granted or not
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d(Util.TAG,"READ PHONE STATE permission is granted");
                    //Log.d("DEVICE VERSION: ", myTel.getDeviceSoftwareVersion());
                } else {
                    // Permission Denied
                    Log.d(Util.TAG,"CALL permission is denied");
                    Toast.makeText(this, "READ PHONE STATE permission is denied", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
}
