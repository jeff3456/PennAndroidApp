package jeff.ronald.autotext.provider;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by jeff on 9/5/15.
 */
public class AutoTextContract {
    public static final String CONTENT_AUTHORITY = "jeff.ronald.autotext";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_TRIGGERS = "triggers";

    public static final class TriggerEntry implements BaseColumns {
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_TRIGGERS).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_TRIGGERS;

        public static final String TABLE_NAME = "triggers";


        // Columns for TriggerEntry

        public static final String COLUMN_RECIEVE_TEXT = "title";

        public static final String COLUMN_REACT_TEXT = "likes";

        public static Uri buildAutoTextUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static String getTriggerFromUri(Uri uri) {
            return uri.getPathSegments().get(1);
        }
    }
}
