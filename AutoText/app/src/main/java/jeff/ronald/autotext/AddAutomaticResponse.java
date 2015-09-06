package jeff.ronald.autotext;

import android.content.ContentValues;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import jeff.ronald.autotext.provider.AutoTextContract;

public class AddAutomaticResponse extends AppCompatActivity {

    private final String TAG = getClass().getSimpleName();

    EditText mTriggerEditText;
    EditText mResponseEditText;
    Button mRegisterButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_automatic_response);

        mTriggerEditText = (EditText) findViewById(R.id.trigger_message_edittext);
        mResponseEditText = (EditText) findViewById(R.id.auto_response_edittext);
        mRegisterButton = (Button) findViewById(R.id.register_autotext_button);

        mRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String trigger = mTriggerEditText.getText().toString();
                String response = mResponseEditText.getText().toString();

                if(trigger.length() == 0){
                    Toast.makeText(getApplicationContext(),"Trigger text needs to be longer"
                            ,Toast.LENGTH_SHORT).show();
                    return;
                } else if(response.length() == 0){
                    Toast.makeText(getApplicationContext(),"Response text needs to be longer"
                            ,Toast.LENGTH_SHORT).show();
                    return;
                }

                // Insert into database

                Uri newAutoTextUri;
                ContentValues mAutoTextValues = new ContentValues();

                mAutoTextValues.put(AutoTextContract.TriggerEntry.COLUMN_RECIEVE_TEXT, trigger.toLowerCase());
                mAutoTextValues.put(AutoTextContract.TriggerEntry.COLUMN_REACT_TEXT, response);

                newAutoTextUri = getContentResolver().insert(
                        AutoTextContract.TriggerEntry.CONTENT_URI, mAutoTextValues);

                Log.e(TAG, "Inserted element successfully!");

                Toast.makeText(getApplicationContext(),"Successfuly inserted AutoText"
                        ,Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_automatic_response, menu);
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
        }

        return super.onOptionsItemSelected(item);
    }
}
