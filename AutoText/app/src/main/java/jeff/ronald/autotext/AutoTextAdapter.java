package jeff.ronald.autotext;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by jeff on 9/6/15.
 */
public class AutoTextAdapter extends CursorAdapter {

    public final int COL_ID = 0;
    public final int COL_RECEIVE = 1;
    public final int COL_RESPONSE = 2;


    public static class ViewHolder {
        public final TextView receiveText;
        public final TextView responseText;

        public ViewHolder(View view) {
            receiveText = (TextView) view.findViewById(R.id.receive_textview);
            responseText = (TextView) view.findViewById(R.id.response_textview);
        }
    }

    public AutoTextAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        // Choose the layout type
        View view = LayoutInflater.from(context).inflate(R.layout.auto_text_list_item, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);
        view.setTag(viewHolder);

        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        ViewHolder viewHolder = (ViewHolder) view.getTag();

        String response = cursor.getString(COL_RESPONSE);
        viewHolder.responseText.setText(response);

        String receive = cursor.getString(COL_RECEIVE);
        viewHolder.receiveText.setText(receive);
    }
}
