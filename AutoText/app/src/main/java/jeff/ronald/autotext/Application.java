package jeff.ronald.autotext;

import com.google.android.gms.common.api.GoogleApiClient;

/**
 * Created by jeff on 9/5/15.
 */
public class Application extends android.app.Application {

    public static GoogleApiClient mGoogleApiClient;

    @Override
    public void onCreate() {
        super.onCreate();
    }

}
