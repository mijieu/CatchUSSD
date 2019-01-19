package busime.cm.catchussd.service;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import java.util.Collections;
import java.util.List;

import busime.cm.catchussd.MainActivity;
import busime.cm.catchussd.util.EventBus;
import busime.cm.catchussd.util.Util;

public class USSDService extends AccessibilityService {

    public static String TAG = USSDService.class.getSimpleName();

    private static final String ABORTED = "Abbrechen";
    public static final String SEND = "Senden";
    public static final String VIEW_ID_EDIT_TEXT_100 = "com.android.phone:id/input_field";

    //how often the USSD Call can be retried
    public int retry = 2;


    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        Log.d(TAG, "onAccessibilityEvent: " + String.valueOf(event.getClassName()) + " retry: " + retry);

        AccessibilityNodeInfo source = event.getSource();

        if (String.valueOf(event.getClassName()).contains("android.app.Dialog") && retry > 0) {
            List<AccessibilityNodeInfo> abortButtonNodesList = source.findAccessibilityNodeInfosByText(ABORTED);
            List<AccessibilityNodeInfo> sendButtonNodesList = source.findAccessibilityNodeInfosByText(SEND);
            if (abortButtonNodesList.size() > 0) {
                for (int i = 0; i < source.getChildCount(); i++) {
                    for (int j=0; j<source.getChild(i).getChildCount(); j++){
                        if (source.getChild(i).getChild(j).getClassName().equals("android.widget.EditText")){
                            List<AccessibilityNodeInfo> editTextList = source.findAccessibilityNodeInfosByViewId(VIEW_ID_EDIT_TEXT_100);
                            Bundle arguments = new Bundle();
                            arguments.putCharSequence(AccessibilityNodeInfo
                                    .ACTION_ARGUMENT_SET_TEXT_CHARSEQUENCE, "123");
                            editTextList.get(0).performAction(AccessibilityNodeInfo.ACTION_SET_TEXT, arguments);
                            break;
                        }
                    }
                }

                CharSequence ussdReponse = abortButtonNodesList.get(0).getText();

                MainActivity.INSTANCE.ussdExecutor.setResponse(ussdReponse.toString());
                MainActivity.INSTANCE.eventBus.sendEvent(EventBus.Event.USSD_RESULT_RECEIVED);

                retry--;

                // dismiss dialog
                //abortButtonNodesList.get(0).performAction(AccessibilityNodeInfo.ACTION_CLICK);
                sendButtonNodesList.get(0).performAction(AccessibilityNodeInfo.ACTION_CLICK);
            }
        }

        if (retry <= 0)
            Log.d(TAG, "USSD request aborted!!");
/*
        for(AccessibilityNodeInfo resp: cancelButtonNodesList){
            Log.d(TAG, "Button: " + resp.toString());
        }


        // dismiss dialog
        cancelButtonNodesList.get(0).performAction(AccessibilityNodeInfo.ACTION_CLICK);*/

        /* if (event.getEventType() == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED && !event.getClassName().equals("android.app.AlertDialog")) { // android.app.AlertDialog is the standard but not for all phones  */
        /*if (event.getEventType() == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED && !String.valueOf(event.getClassName()).contains("AlertDialog")) {
            Log.d(Util.TAG, "WINDOW_CONTENT_CHANGED 1: " + String.valueOf(event.getClassName()));
            return;
        }
        if (event.getEventType() == AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED && (source == null || !source.getClassName().equals("android.widget.TextView"))) {
            Log.d(Util.TAG, "WINDOW_CONTENT_CHANGED 2: " + String.valueOf(source.getClassName()));
            return;
        }
        if (event.getEventType() == AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED && TextUtils.isEmpty(source.getText())) {
            Log.d(Util.TAG, "WINDOW_CONTENT_CHANGED 3: " + String.valueOf(source.getText()));
            return;
        }*/

        /*if (event.getEventType() == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED && String.valueOf(event.getClassName()).contains("android.app.Dialog")) {
            List<CharSequence> eventText;

            if (event.getEventType() == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
                eventText = event.getText();
            } else {
                eventText = Collections.singletonList(source.getText());
            }

            Log.d(Util.TAG, "USSD Raw:\\n" + eventText);

            String text = processUSSDText(eventText);

            if (TextUtils.isEmpty(text)) {
                Log.d(Util.TAG, "USSD Response empty");
                return;
            }

            // Close dialog
            //performGlobalAction(GLOBAL_ACTION_BACK); // This works on 4.1+ only

            Log.d(Util.TAG, "USSD clean:\\n" +text);
            // Handle USSD response here
        }
        else{
            Log.d(Util.TAG, "WINDOW_CONTENT_CHANGED - Nothing to do: " + String.valueOf(event.getClassName()));
            return;
        }*/


    }

    private String processUSSDText(List<CharSequence> eventText) {
        for (CharSequence s : eventText) {
            String text = String.valueOf(s);
            // Return text if text is the expected ussd response
            if (true) {
                return text;
            }
        }
        return null;
    }

    @Override
    public void onInterrupt() {
    }

    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();
        Log.d(Util.TAG, "onServiceConnected");
        AccessibilityServiceInfo info = new AccessibilityServiceInfo();
        info.flags = AccessibilityServiceInfo.DEFAULT;
        info.packageNames = new String[]{"com.android.phone"};
        info.eventTypes = AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED | AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED;
        info.feedbackType = AccessibilityServiceInfo.FEEDBACK_GENERIC;
        setServiceInfo(info);
    }
}
