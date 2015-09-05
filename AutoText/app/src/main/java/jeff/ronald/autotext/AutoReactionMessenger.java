package jeff.ronald.autotext;

import android.content.Context;
import android.location.Location;
import android.os.Bundle;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

/**
 * This class will handle logic for sending appropriate reaction message
 */
public class AutoReactionMessenger implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;

    public AutoReactionMessenger(Context context) {

        // Connect to
        GoogleApiClient mGoogleApiClient = new GoogleApiClient.Builder(context)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        mGoogleApiClient.connect();
    }

    public String generateReaction(String receivedMessage) {
        String reactionMessage = "Hey";
        if( receivedMessage.toLowerCase().contains("where are you")){
            return getLocation();
        }

        return reactionMessage;
    }

    private String getLocation() {
        // Acquire a reference to the system Location Manager
        if(mLastLocation != null) {

            String locationMessage = String.valueOf(mLastLocation.getLatitude()) +
                    "," +
                    String.valueOf(mLastLocation.getLongitude());
            return locationMessage;
        }

        return null;
    }

    @Override
    public void onConnected(Bundle connectionHint) {
        // Connected to Google Play services!
        // The good stuff goes here.
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
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
        mGoogleApiClient.disconnect();
    }
}
