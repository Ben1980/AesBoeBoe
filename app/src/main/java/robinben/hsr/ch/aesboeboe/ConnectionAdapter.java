package robinben.hsr.ch.aesboeboe;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import ch.schoeb.opendatatransport.model.Connection;
import ch.schoeb.opendatatransport.model.ConnectionList;
import ch.schoeb.opendatatransport.model.ConnectionStation;
import ch.schoeb.opendatatransport.model.Station;

/**
 * Created by Ben on 01.03.15.
 */
public class ConnectionAdapter extends BaseAdapter {
    private Context context;
    private List<Connection> connections;

    public ConnectionAdapter(Context context, ConnectionList connections) {
        this.context = context;
        this.connections = connections.getConnections();
    }

    @Override
    public int getCount() {
        return connections.size();
    }

    @Override
    public Object getItem(int position) {
        return connections.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.result_list_item, parent, false);
        }

        Connection connection = (Connection) getItem(position);

        TextView from = (TextView) convertView.findViewById(R.id.from);
        ConnectionStation fromStation = connection.getFrom();
        from.setText(fromStation.getStation().getName());

        TextView to = (TextView) convertView.findViewById(R.id.to);
        ConnectionStation toStation = connection.getTo();
        to.setText(toStation.getStation().getName());

        return convertView;
    }
}
