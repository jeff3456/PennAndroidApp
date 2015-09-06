package jeff.ronald.autotext;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.ActionBarActivity;
import android.telephony.gsm.SmsManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import jeff.ronald.autotext.provider.AutoTextContract;

public class MainActivity extends ActionBarActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private final String TAG = getClass().getSimpleName();

    private final int AUTOTEXT_LOADER = 0;

    ListView mListView;
    AutoTextAdapter mAutoTextAdapter;

    Context mContext;

    private BroadcastReceiver sendBroadcastReceiver;
    private BroadcastReceiver deliveryBroadcastReceiver;

    private boolean mBroadcastRegistered;

    public static final String[] AUTO_TEXT_COLUMNS = {
        AutoTextContract.TriggerEntry.TABLE_NAME + "." + AutoTextContract.TriggerEntry._ID,
        AutoTextContract.TriggerEntry.COLUMN_RECIEVE_TEXT,
        AutoTextContract.TriggerEntry.COLUMN_REACT_TEXT
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.e(TAG, "created Main Fragment");

        // Inflate the layout for this fragment
        View addButton = findViewById(R.id.add_button);

        mListView = (ListView) findViewById(R.id.listview_main);

        // Create forecast adapter and find listview
        mAutoTextAdapter = new AutoTextAdapter(this, null, 0);
        mListView = (ListView) findViewById(R.id.listview_main);
        mListView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);


        // If you click on item, you can delete it
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                TextView v = (TextView) view.findViewById(R.id.receive_textview);
                String receive = v.getText().toString();

                v = (TextView) view.findViewById(R.id.response_textview);
                String response = v.getText().toString();

                Intent intent = new Intent(mContext, EditResponseActivity.class);
                intent.putExtra("receive", receive);
                intent.putExtra("response", response);
                startActivity(intent);
            }
        });


        mContext = this;

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Perform adding a new automatic expression
                Log.e(TAG, "Click add button registered");
                Intent i = new Intent(mContext, AddAutomaticResponse.class);
                startActivity(i);

            }
        });

        mBroadcastRegistered = false;

        //---when the SMS has been sent---
        sendBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context arg0, Intent arg1) {
                switch (getResultCode()) {
                    case Activity.RESULT_OK:
                        Toast.makeText(getBaseContext(), "SMS sent",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                        Toast.makeText(getBaseContext(), "Generic failure",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_NO_SERVICE:
                        Toast.makeText(getBaseContext(), "No service",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_NULL_PDU:
                        Toast.makeText(getBaseContext(), "Null PDU",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_RADIO_OFF:
                        Toast.makeText(getBaseContext(), "Radio off",
                                Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        };

        //---when the SMS has been delivered---
        deliveryBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context arg0, Intent arg1) {
                switch (getResultCode()) {
                    case Activity.RESULT_OK:
                        Toast.makeText(getBaseContext(), "SMS delivered",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case Activity.RESULT_CANCELED:
                        Toast.makeText(getBaseContext(), "SMS not delivered",
                                Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        };
        if (!mBroadcastRegistered) {
            registerReceiver(deliveryBroadcastReceiver, new IntentFilter(Application.DELIVERED));
            registerReceiver(sendBroadcastReceiver, new IntentFilter(Application.SENT));
        }

        getSupportLoaderManager().initLoader(AUTOTEXT_LOADER, null, this);

        mListView.setAdapter(mAutoTextAdapter);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        } else if (id == R.id.white_list) {
            // Start white list activity

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStop()
    {
        if(mBroadcastRegistered) {
            unregisterReceiver(sendBroadcastReceiver);
            unregisterReceiver(deliveryBroadcastReceiver);
        }
        super.onStop();
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {

        Uri autoTextUri = AutoTextContract.TriggerEntry.CONTENT_URI;

        return new CursorLoader(this,
                autoTextUri,
                AUTO_TEXT_COLUMNS,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mAutoTextAdapter.swapCursor(data);

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mAutoTextAdapter.swapCursor(null);

    }
}
