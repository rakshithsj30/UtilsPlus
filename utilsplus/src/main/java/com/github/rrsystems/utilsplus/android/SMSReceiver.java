package com.github.rrsystems.utilsplus.android;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;


/**
 * Created by Clear Bits on 4/28/2017.
 */
public class SMSReceiver extends BroadcastReceiver {
    SMSListener smsListener;
    String[] phoneNos;
    // Get the object of SmsManager
    final SmsManager sms = SmsManager.getDefault();

    public interface SMSListener {

        public void onSMSReceived(String phone_no, String message);
    }

    public void registerReceiver(Activity activity, SMSReceiver smsReceiver, String... phoneNumbers) {
        IntentFilter mIntentFilter;
        mIntentFilter = new IntentFilter();
        mIntentFilter.addAction("android.provider.Telephony.SMS_RECEIVED");
        mIntentFilter.setPriority(2147483647);
        activity.registerReceiver(smsReceiver, mIntentFilter);
        phoneNos = phoneNumbers;


    }

    public void unregisterReceiver(Activity activity, SMSReceiver smsReceiver) {
        activity.unregisterReceiver(smsReceiver);

    }

    public String extractCode(String message, int firstIndex, int lastIndex) {
        if (message == null || message.isEmpty()) {
            return "";
        } else {
            try {
                if (lastIndex == -999) {
                    return message.substring(firstIndex);
                } else {
                    return message.substring(firstIndex, lastIndex);
                }
            } catch (Exception e) {
                e.printStackTrace();
                return "";
            }
        }


    }


    public void setSMSListener(SMSListener listener) {
        this.smsListener = listener;
    }


    public void onReceive(Context context, Intent intent) {

        // Retrieves a map of extended data from the intent.
        final Bundle bundle = intent.getExtras();

        try {

            if (bundle != null) {

                final Object[] pdusObj = (Object[]) bundle.get("pdus");

                for (int i = 0; i < pdusObj.length; i++) {

                    SmsMessage currentMessage = SmsMessage.createFromPdu((byte[]) pdusObj[i]);
                    String phoneNumber = currentMessage.getDisplayOriginatingAddress();

                    String senderNum = phoneNumber;
                    String message = currentMessage.getDisplayMessageBody();

                    if (smsListener != null) {

                        List<String> phone_nos = Arrays.asList(phoneNos);
                        for (int j = 0; j < phone_nos.size(); j++) {
                            if (phone_nos.get(i).equals(senderNum)) {
                                smsListener.onSMSReceived(phoneNumber, message);
                            }
                        }


                    }
                    Log.i("SmsReceiver", "senderNum: " + senderNum + "; message: " + message);
                } // end for loop
            }

        } catch (Exception e) {


        }
    }
}
