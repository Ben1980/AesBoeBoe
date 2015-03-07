package robinben.hsr.ch.aesboeboe;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ch.schoeb.opendatatransport.model.Connection;
import ch.schoeb.opendatatransport.model.ConnectionStation;
import ch.schoeb.opendatatransport.model.Section;

/**
 * Created by Ben on 07.03.15.
 */
public class DetailsAdapter extends BaseAdapter {
    private Context context;
    private Connection connection;
    private List<Section> sections;

    public DetailsAdapter(Context context, Connection connection) {
        this.context = context;
        this.connection = connection;

        sections = connection.getSections();
    }

    @Override
    public int getCount() {
        return sections.size();
    }

    @Override
    public Object getItem(int position) {
        return sections.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.details_list_item, parent, false);
        }

        Section section = (Section) getItem(position);

        TextView station = (TextView) convertView.findViewById(R.id.station);
        TextView arrival = (TextView) convertView.findViewById(R.id.arrival);
        TextView departure = (TextView) convertView.findViewById(R.id.departure);
        TextView rail = (TextView) convertView.findViewById(R.id.rail);

        

        //String departureTime = formateDepartureArrivalTime(connection.getFrom().getDeparture());
        //departure.setText(departureTime);

        //String arrivalTime = formateDepartureArrivalTime(connection.getTo().getArrival());
        //arrival.setText(arrivalTime);

        //String durationTime = formatDurationtime(connection.getDuration());
        //duration.setText(durationTime);

        //int delayTime = Integer.parseInt(connection.getTo().getDelay());
        //delay.setText(String.valueOf(delayTime));

        return convertView;
    }
}
