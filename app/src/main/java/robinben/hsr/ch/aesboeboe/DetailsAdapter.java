package robinben.hsr.ch.aesboeboe;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
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

        station.setText(section.getDeparture().getStation().getName());

        String arrivalTime = formateDepartureArrivalTime(section.getArrival().getArrival());
        arrival.setText(arrivalTime);

        String departureTime = formateDepartureArrivalTime(section.getDeparture().getDeparture());
        departure.setText(departureTime);

        rail.setText(section.getDeparture().getPlatform());

        return convertView;
    }

    private String formateDepartureArrivalTime(String dateStr) {
        try {
            return new SimpleDateFormat("HH:mm").format(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").parse(dateStr));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return new String();
    }
}
