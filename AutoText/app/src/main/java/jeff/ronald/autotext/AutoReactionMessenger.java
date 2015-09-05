package jeff.ronald.autotext;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;

import java.io.IOException;
import java.util.List;
import java.util.Locale;



/**
 * This class will handle logic for sending appropriate reaction message
 */
public class AutoReactionMessenger implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, android.location.LocationListener {

    private static final String TAG = AutoReactionMessenger.class.getSimpleName();

    private GoogleApiClient mGoogleApiClient;
    private String mLastLocation;
    protected LocationManager mLocationManager;
    private String mReturnNumber;
    private Context mContext;

    public AutoReactionMessenger(Context context) {
        this.mContext = context;

    }

    public void sendReaction(String receivedMessage, String returnNumber ,Context context) {

        final String defaultReactionMessage = "Hey this is AutoText";
        final String freePresentlyMessage = "I am doing nothing";
        final String busyMessage = "I am busy with ";
        final String whatIsQueryPrefix = "What is";

        String locationRegexPat = ".*whe{0,1}re*.*a*re*.*y{0,1}o*u*.*";
        String locationRegexPat1 = ".*whe{0,1}re*.*y{0,1}o*u{1,100}.*at.*";
        String presentActivityRegexPat = ".*wh*a*t.*a{0,1}re*.*y{0,1}o*u.*do*i*ng*.*";
        String presentActivityRegexPat1 = ".*what'*s.*up.*";

        mContext = context;
        mReturnNumber = returnNumber;

        String smilesAndHearts = generateSmilesAndHearts(receivedMessage);


        // get location manager.
        mLocationManager = (LocationManager)
                context.getSystemService(Context.LOCATION_SERVICE);
        mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000, 10, this);
        onLocationChanged(mLocationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER));

        String lowercaseReceived = receivedMessage.toLowerCase();



        if( (lowercaseReceived.matches(locationRegexPat) ||
                lowercaseReceived.matches(locationRegexPat1) )
                && mLastLocation != null) {
            Application.sendSMS(mReturnNumber, mLastLocation + "\n " + smilesAndHearts, mContext);

        }else if (lowercaseReceived.matches(presentActivityRegexPat) ||
                lowercaseReceived.matches(presentActivityRegexPat1)){

            CalendarRetriever cal = new CalendarRetriever(mContext);
            String eventTitle = cal.findPresentEventTitle();
            cal.close();

            // if eventTitle is null then we assume that we are free.
            if(eventTitle == null) {
                Application.sendSMS(mReturnNumber, freePresentlyMessage
                        + "\n " + smilesAndHearts, mContext);
            } else {
                Application.sendSMS(mReturnNumber, busyMessage + eventTitle
                        + "\n " + smilesAndHearts, mContext);
            }

        } else {
//            Log.v(TAG, defaultReactionMessage);
//            Application.sendSMS(mReturnNumber, defaultReactionMessage, context);
        }

        disconnect();
    }

    private String generateSmilesAndHearts(String message) {
        String smile = ":)";
        String heart = "<3";
        int smileyCount = 0;
        int heartCount = 0;

        String generatedString = "";

        // Count the number of smiley faces
        for(int i = 0; i < message.length() - 1; i ++) {

            if ((message.charAt(i) == ':' && message.charAt(i + 1) == ')') ||
                    (message.charAt(i) == '(' && message.charAt(i + 1) == ')')) {
                smileyCount++;
            }
        }

        // Count the number of hearts
        for(int i = 0; i < message.length() - 1; i ++) {
            if((message.charAt(i) == '<' && message.charAt(i+1) == '3')){
                heartCount++;
            }
        }

        while (smileyCount > 0 && heartCount > 0) {
            generatedString += smile + heart;
        }
        while(smileyCount > 0) {
            generatedString += smile;
        }
        while(heartCount > 0) {
            generatedString += heart;
        }

        return generatedString;
    }

    @Override
    public void onConnected(Bundle connectionHint) {
        // Connected to Google Play services!
        // The good stuff goes here.
    }

    @Override
    public void onConnectionSuspended(int cause) {
        // The connection has been interrupted.
        // Disable any UI components that depend on Google APIs
        // until onConnected() is called.
    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        // This callback is important for handling errors that
        // may occur while attempting to connect with Google.
        //
        // More about this in the 'Handle Connection Failures' section.

    }

    public void disconnect(){
        if(mGoogleApiClient != null) {
//            mGoogleApiClient.disconnect();
        }
    }

    @Override
    public void onLocationChanged(Location location) {

        Log.e(TAG, location.toString());

        mLastLocation = "";

        String longitude = "Longitude: " + location.getLongitude();
        Log.v(TAG, longitude);
        String latitude = "Latitude: " + location.getLatitude();
        Log.v(TAG, latitude);


        String cityName = null;
        Geocoder gcd = new Geocoder(mContext, Locale.getDefault());
        List<Address> addresses;
        try {
            addresses = gcd.getFromLocation(location.getLatitude(),
                    location.getLongitude(), 1);
            if (addresses.size() > 0)
                System.out.println(addresses.get(0).getLocality());
            cityName = addresses.get(0).getLocality();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        String s = longitude + "\n" + latitude + "\n\nMy Current City is: "
                + cityName;
//        editLocation.setText(s);

        mLastLocation = s;
    }


    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
