package robinben.hsr.ch.aesboeboe;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import ch.schoeb.opendatatransport.model.Section;

/**
 * Created by Ben on 07.03.15.
 */
public class DetailsAdapter extends BaseAdapter {
    private Context context;
    private List<Section> sections;

    public DetailsAdapter(Context context) {
        this.context = context;

        sections = Globals.connection.getSections();
    }

    @Override
    public int getCount() {
        if(sections.size() == 1) {
            return sections.size() + 1;
        }

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

        TextView station = (TextView) convertView.findViewById(R.id.station);
        TextView arrival = (TextView) convertView.findViewById(R.id.arrival);
        TextView departure = (TextView) convertView.findViewById(R.id.departure);
        TextView rail = (TextView) convertView.findViewById(R.id.rail);

        setStation(position, station, arrival, departure, rail);

        return convertView;
    }

    private void setStation(int position, TextView station, TextView arrival, TextView departure, TextView rail) {
        Section section;
        if(sections.size() > 1) {
            section = (Section) getItem(position);
        }
        else {
            section = (Section) getItem(position == 0 ? position : position - 1);
        }

        if(position == 0) { //First Station: Only departure should be shown
            station.setText(section.getDeparture().getStation().getName());
            String departureTime = formateDepartureArrivalTime(section.getDeparture().getDeparture());
            departure.setText(departureTime);
            rail.setText(section.getDeparture().getPlatform());
        }
        if (position == getCount() - 1) {    //Last Station: Only arrival should be shown
            station.setText(section.getArrival().getStation().getName());
            String arrivalTime = formateDepartureArrivalTime(section.getArrival().getArrival());
            arrival.setText(arrivalTime);
            rail.setText(section.getArrival().getPlatform());
        }
        if(position > 0 && position < getCount() - 1) {
            station.setText(section.getDeparture().getStation().getName());

            String departureTime = formateDepartureArrivalTime(section.getDeparture().getDeparture());
            departure.setText(departureTime);

            Section lastsection = (Section) getItem(position - 1);
            String arrivalTime = formateDepartureArrivalTime(lastsection.getArrival().getArrival());
            arrival.setText(arrivalTime);

            rail.setText(section.getDeparture().getPlatform());
        }
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
