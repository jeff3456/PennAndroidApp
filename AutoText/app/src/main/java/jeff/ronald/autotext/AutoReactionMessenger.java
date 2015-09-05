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
    private boolean mSendLocation = false;
    private String mReturnNumber;
    private Context mContext;

    public AutoReactionMessenger(Context context) {
        this.mContext = context;

    }

    public void sendReaction(String receivedMessage, String returnNumber ,Context context) {
        String defaultReactionMessage = "Hey this is AutoText";

        mContext = context;
        mReturnNumber = returnNumber;

        // get location manager.
        mLocationManager = (LocationManager)
                context.getSystemService(Context.LOCATION_SERVICE);
        mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000, 10, this);
        onLocationChanged(mLocationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER));

        String locationRegexPat = ".*whe{0,1}re*.*a*re*.*y{0,1}o*u*.*";
        String presentActivityRegexPat = ".*wh*a*t.*a{0,1}re*.*y{0,1}o*u.*do*i*ng*.*";

        if(receivedMessage.toLowerCase().matches(locationRegexPat) && mLastLocation != null) {
            Application.sendSMS(mReturnNumber, mLastLocation, mContext);
        }else if (receivedMessage.toLowerCase().matches(presentActivityRegexPat)){
            CalendarRetriever cal = new CalendarRetriever(mContext);

        } else {
//            Log.v(TAG, defaultReactionMessage);
//            Application.sendSMS(mReturnNumber, defaultReactionMessage, context);
        }

        disconnect();
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
