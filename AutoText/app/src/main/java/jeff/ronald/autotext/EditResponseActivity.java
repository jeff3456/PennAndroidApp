package jeff.ronald.autotext;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import jeff.ronald.autotext.provider.AutoTextContract;

public class EditResponseActivity extends AppCompatActivity {
    TextView mResponseText;
    TextView mReceiveText;
    Button mDeleteButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_response);

        mResponseText = (TextView) findViewById(R.id.edit_response_textview);
        mReceiveText = (TextView) findViewById(R.id.edit_received_textview);
        mDeleteButton = (Button) findViewById(R.id.delete_response);

        Intent intent = getIntent();

        final String receiveString = intent.getStringExtra("receive");

        mResponseText.setText(intent.getStringExtra("response"));
        mReceiveText.setText(intent.getStringExtra("receive"));

        mDeleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getApplicationContext().getContentResolver().delete(AutoTextContract.TriggerEntry.CONTENT_URI,
                        AutoTextContract.TriggerEntry.COLUMN_RECIEVE_TEXT + " = ? ",
                        new String[] {receiveString});

                finish();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_edit_response, menu);
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
