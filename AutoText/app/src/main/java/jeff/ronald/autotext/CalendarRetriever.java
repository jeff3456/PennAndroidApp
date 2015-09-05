package jeff.ronald.autotext;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CalendarContract;

/**
 * This class will process the Calendar UI and return appropriate information.
 */
public class CalendarRetriever {

    private Cursor mEventCursor;

    private long mCurrentTime;

    public CalendarRetriever(Context context) {

        String[] EVENT_PROJECTION = new String[]{
                CalendarContract.Events.TITLE,          // String
                CalendarContract.Events.EVENT_LOCATION, // String
                CalendarContract.Instances.BEGIN,       // Long
                CalendarContract.Instances.END,         // Long
                CalendarContract.Events.ALL_DAY};       // Boolean

        ContentResolver resolver = context.getContentResolver();

        mCurrentTime = System.currentTimeMillis();
        long oneDay = 86400000L;//== 24 * 60 * 60 * 1000;

        Uri.Builder eventsUriBuilder = CalendarContract.Instances.CONTENT_URI.buildUpon();
        ContentUris.appendId(eventsUriBuilder, mCurrentTime);
        ContentUris.appendId(eventsUriBuilder, mCurrentTime + oneDay );

        Uri eventUri = eventsUriBuilder.build();

        String selection = "";
        String[] selectionArgs = new String[]{""};

        mEventCursor = resolver.query(eventUri,
                EVENT_PROJECTION,
                selection,
                selectionArgs,
                CalendarContract.Instances.BEGIN + " ASC");

    }

    public String findPresentEvent(){
        String eventTitle;



        return null;
    }

    public void close(){
        mEventCursor.close();
    }


}
