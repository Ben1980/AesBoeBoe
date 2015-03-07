package robinben.hsr.ch.aesboeboe;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import ch.schoeb.opendatatransport.model.Connection;

/**
 * Created by Ben on 07.03.15.
 */
public class DetailsAdapter extends BaseAdapter {
    private Context context;
    private Connection connection;
    //private List

    public DetailsAdapter(Context context, Connection connection) {
        this.context = context;
        this.connection = connection;
    }

    @Override
    public int getCount() {
        //connection.

        return 0;
    }

    @Override
    public Object getItem(int position) {
        return connection;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return null;
    }
}
