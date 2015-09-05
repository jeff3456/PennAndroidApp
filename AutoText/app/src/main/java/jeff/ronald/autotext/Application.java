package jeff.ronald.autotext;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.telephony.gsm.SmsManager;

import com.google.android.gms.common.api.GoogleApiClient;

/**
 * Created by jeff on 9/5/15.
 */
public class Application extends android.app.Application {

    public static GoogleApiClient mGoogleApiClient;

    static String SENT = "SMS_SENT";
    static String DELIVERED = "SMS_DELIVERED";


    @Override
    public void onCreate() {
        super.onCreate();
    }

    public static void sendSMS(String phoneNumber, String message, Context context)
    {
        PendingIntent sentPI = PendingIntent.getBroadcast(context, 0,
                new Intent(SENT), 0);

        PendingIntent deliveredPI = PendingIntent.getBroadcast(context, 0,
                new Intent(DELIVERED), 0);


        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(phoneNumber, null, message, sentPI, deliveredPI);
    }

}
