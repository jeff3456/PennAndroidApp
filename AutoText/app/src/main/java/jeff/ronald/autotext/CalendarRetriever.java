package jeff.ronald.autotext;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.CalendarContract;
import android.util.Log;

import java.util.Calendar;

/**
 * This class will process the Calendar UI and return appropriate information.
 */
public class CalendarRetriever {

    private final String TAG = getClass().getSimpleName();

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
        long oneDayMillis = 86400000L;//== 24 * 60 * 60 * 1000;

        mEventCursor = CalendarContract.Instances.query(
                resolver,
                EVENT_PROJECTION,
                mCurrentTime,
                mCurrentTime + oneDayMillis);

    }

    public String findPresentEventTitle(){

        // Used to index the projections
        final int TITLE_IND = 0;
        final int LOCATION_IND = 1;
        final int BEGINTIME_IND = 2;
        final int ENDTIME_IND = 3;
        final int ALL_DAY_IND = 4;

        // Iterate through all cursors
        if(mEventCursor != null) {
            while (mEventCursor.moveToNext()) {

                String title = mEventCursor.getString(TITLE_IND);

                // Found an all day event so you are busy
                if(mEventCursor.getInt(ALL_DAY_IND)>0) {
                    return title + " all day";
                }


                Log.e(TAG, "Time of event:"+ mEventCursor.getLong(ENDTIME_IND));
                // Calculate if the event happens during present
                long duration = mEventCursor.getLong(ENDTIME_IND)-mEventCursor.getLong(BEGINTIME_IND);
                long endMinusCurrent = mEventCursor.getLong(ENDTIME_IND) - mCurrentTime;

                // Event is happening NOW
                if(duration <= endMinusCurrent) {

                    Calendar cal = Calendar.getInstance();
                    cal.setTimeInMillis(mEventCursor.getLong(ENDTIME_IND));

                    return title + " until " + cal.get(Calendar.HOUR)+":"+ cal.get(Calendar.MINUTE);
                }
            }
        }

        // Processed all events so you are free
        return null;
    }

    public void close(){
        mEventCursor.close();
    }


}
