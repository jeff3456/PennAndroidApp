package jeff.ronald.autotext;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.gsm.SmsMessage;

/**
 * Created by jeff on 9/5/15.
 */
public class SmsReceiver extends BroadcastReceiver {
    AutoReactionMessenger mReactionMessenger;

    @Override
    public void onReceive(Context context, Intent intent)
    {
        //---get the SMS message passed in---
        Bundle bundle = intent.getExtras();

        SmsMessage[] msgs = null;
        String returnNumber = "";
        String receivedMessage = "";

        if (bundle != null)
        {
            //---retrieve the SMS message received---
            Object[] pdus = (Object[]) bundle.get("pdus");
            msgs = new SmsMessage[pdus.length];

            for (int i=0; i<msgs.length; i++){
                msgs[i] = SmsMessage.createFromPdu((byte[])pdus[i]);
                // Received the return number
                returnNumber = msgs[i].getOriginatingAddress().toString();
                receivedMessage = msgs[i].getMessageBody().toString();
            }

            //---display the new SMS message---
//            Toast.makeText(context, str, Toast.LENGTH_SHORT).show();

            mReactionMessenger = new AutoReactionMessenger(context);

            // Generate the message to send
            String reactionMessage = mReactionMessenger.generateReaction(receivedMessage);

            // Send text based on reactionMessage
            MainActivity.sendSMS(returnNumber, reactionMessage, context);

            mReactionMessenger.disconnect();
        }
    }

}
