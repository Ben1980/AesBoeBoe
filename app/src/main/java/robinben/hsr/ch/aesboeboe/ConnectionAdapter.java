package robinben.hsr.ch.aesboeboe;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

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

        TextView departure = (TextView) convertView.findViewById(R.id.departure);
        TextView arrival = (TextView) convertView.findViewById(R.id.arrival);
        TextView duration = (TextView) convertView.findViewById(R.id.duration);
        TextView delay = (TextView) convertView.findViewById(R.id.delay);

        String departureTime = formateDepartureArrivalTime(connection.getFrom().getDeparture());
        departure.setText(departureTime);

        String arrivalTime = formateDepartureArrivalTime(connection.getTo().getArrival());
        arrival.setText(arrivalTime);

        String durationTime = formatDurationtime(connection.getDuration());
        duration.setText(durationTime);

        //String delayTime = formateTime(connection.getTo().getDelay());
        //delay.setText(delayTime);


        //from.setText();

        //TextView to = (TextView) convertView.findViewById(R.id.to);
        //ConnectionStation toStation = connection.getTo();
        //to.setText(toStation.getStation().getName());

        return convertView;
    }

    private String formateDepartureArrivalTime(String dateStr) {
        Date date = new Date();

        try {
            date = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return new SimpleDateFormat("HH:mm").format(date);
    }

    private String formatDurationtime(String delayStr) {
        String[] arr = delayStr.split("[d:]+");
        int days = Integer.parseInt(arr[0]);
        int hours = Integer.parseInt(arr[1]);
        int minutes = Integer.parseInt(arr[2]);

        String delay = new String("");
        if(days != 0) {
            delay += String.valueOf(days) + "d, ";
        }
        if(hours != 0) {
            delay += String.valueOf(hours) + "h, ";
        }
        if(minutes != 0) {
            delay += String.valueOf(minutes) + "m";
        }

        return delay;
    }
}
