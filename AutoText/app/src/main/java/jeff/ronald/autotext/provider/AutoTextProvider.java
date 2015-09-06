package jeff.ronald.autotext.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

public class AutoTextProvider extends ContentProvider {

    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private AutoTextDBHelper mOpenHelper;

    static final int AUTO_TEXT = 100;

    private static final SQLiteQueryBuilder sAutoTextQueryBuilder;

    static {
        sAutoTextQueryBuilder = new SQLiteQueryBuilder();

    }

    private static final String sTriggerSelection =
            AutoTextContract.TriggerEntry.COLUMN_RECIEVE_TEXT + "= ?";

    public AutoTextProvider() {
    }

    private Cursor getTriggerByReceiveText(Uri uri, String[] projection, String trigger, String sortOrder) {
        String[] selectionArgs;
        String selection;

        selection = sTriggerSelection;
        selectionArgs = new String[] {trigger};

        return sAutoTextQueryBuilder.query(mOpenHelper.getReadableDatabase(),
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder
        );

    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsDeleted;

        if ( selection == null ) selection = "1";
        rowsDeleted = db.delete(AutoTextContract.TriggerEntry.TABLE_NAME, selection, selectionArgs);

        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsDeleted;
    }

    @Override
    public String getType(Uri uri) {
        return AutoTextContract.TriggerEntry.CONTENT_TYPE;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        Uri returnUri;

        long _id = db.insert(AutoTextContract.TriggerEntry.TABLE_NAME, null, values);
        if ( _id > 0 )
            returnUri = AutoTextContract.TriggerEntry.buildAutoTextUri(_id);
        else
            throw new android.database.SQLException("Failed to insert row into " + uri);

        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public boolean onCreate() {
        mOpenHelper = new AutoTextDBHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        Cursor retCursor;
        switch (sUriMatcher.match(uri)) {
            case AUTO_TEXT: {
                retCursor = mOpenHelper.getReadableDatabase().query(
                        AutoTextContract.TriggerEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);

                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return retCursor;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsUpdated;

        rowsUpdated = db.update(AutoTextContract.TriggerEntry.TABLE_NAME, values, selection,
                selectionArgs);

        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return rowsUpdated;
    }

    public static UriMatcher buildUriMatcher(){

        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = AutoTextContract.CONTENT_AUTHORITY;

        // For each type of URI you want to add, create a corresponding code.
        matcher.addURI(authority, AutoTextContract.PATH_TRIGGERS, AUTO_TEXT);

        return matcher;
    }
}
