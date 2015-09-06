package jeff.ronald.autotext;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Large screen devices (such as tablets) are supported by replacing the ListView
 * with a GridView.
 * <p/>
 * interface.
 */
public class MainFragment extends Fragment {

    private Context mContext;

    private final String TAG = getClass().getSimpleName();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent,
                             Bundle savedInstanceState) {

        Log.e(TAG, "created Main Fragment");

        // Inflate the layout for this fragment
        View v;
        v = inflater.inflate(R.layout.fragment_main, parent, false);

        View addButton = v.findViewById(R.id.add_button);

        mContext = getActivity();

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Perform adding a new automatic expression
                Log.e(TAG, "Click add button registered");
                Intent i = new Intent(mContext, AddAutomaticResponse.class);
                startActivity(i);

            }
        });
        return v;
    }


}
