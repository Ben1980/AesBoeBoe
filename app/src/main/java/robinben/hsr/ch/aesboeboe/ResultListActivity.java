package robinben.hsr.ch.aesboeboe;


import android.app.ProgressDialog;
import android.content.Context;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import ch.schoeb.opendatatransport.IOpenTransportRepository;
import ch.schoeb.opendatatransport.OpenTransportRepositoryFactory;
import ch.schoeb.opendatatransport.model.Connection;
import ch.schoeb.opendatatransport.model.ConnectionList;


public class ResultListActivity extends Activity {
    private TextView tvResultFrom;
    private TextView tvResultTo;
    private TextView tvResultDate;
    private TextView tvResultTime;
    private TextView via;
    private ListView listView;
    private boolean isArrivalTime;
    private Context context = this;
    //private SearchWorker searchWorker = new SearchWorker();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_list);

        tvResultFrom = (TextView) findViewById(R.id.tvResultFrom);
        tvResultTo = (TextView) findViewById(R.id.tvResultTo);
        tvResultDate = (TextView) findViewById(R.id.tvResultDate);
        tvResultTime = (TextView) findViewById(R.id.tvResultTime);
        TextView tvTime = (TextView) findViewById(R.id.tvTime);

        Intent intent = getIntent();

        isArrivalTime = intent.getBooleanExtra("isArrivalTime", false);
        tvResultFrom.setText(intent.getStringExtra("from"));
        tvResultTo.setText(intent.getStringExtra("to"));
        tvResultDate.setText(intent.getStringExtra("date"));
        tvResultTime.setText(intent.getStringExtra("time"));
        via = new TextView(this);
        via.setText(intent.getStringExtra("via"));

        selectArrivalDepartureLabel(isArrivalTime, tvTime);

        new SearchWorker().execute(tvResultFrom.getText().toString(), tvResultTo.getText().toString(), via.getText().toString(), tvResultDate.getText().toString(), tvResultTime.getText().toString(), isArrivalTime);

        listView = (ListView)findViewById(R.id.listView);

        listView.setOnItemClickListener(new ListView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Globals.connection = Globals.connectionList.getConnections().get(position);

                startDetailsView(tvResultFrom.getText().toString(), tvResultTo.getText().toString(), tvResultDate.getText().toString(), tvResultTime.getText().toString());
            }
        });

        LinearLayout earlier = (LinearLayout) findViewById(R.id.earlier);
        earlier.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int lastIndex = Globals.connectionList.getConnections().size() - 1;
                if(lastIndex >= 0) {
                    Connection firstConnection = Globals.connectionList.getConnections().get(0);
                    Connection lastConnection = Globals.connectionList.getConnections().get(lastIndex);

                    String first = firstConnection.getFrom().getDeparture();
                    String second = lastConnection.getFrom().getDeparture();
                    String newSearchStr = calcTime(first, second);

                    String departureDateStr = new String();
                    String departureTimeStr = new String();
                    try {
                        departureDateStr = new SimpleDateFormat("yyyy-MM-dd").format(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").parse(newSearchStr));
                        departureTimeStr = new SimpleDateFormat("HH:mm").format(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").parse(newSearchStr));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    new SearchWorker().execute(tvResultFrom.getText().toString(), tvResultTo.getText().toString(), via.getText().toString(), departureDateStr, departureTimeStr, isArrivalTime);
                }
            }
        });

        LinearLayout later = (LinearLayout) findViewById(R.id.later);
        later.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int lastIndex = Globals.connectionList.getConnections().size() - 1;
                if(lastIndex >= 0) {
                    Connection connection = Globals.connectionList.getConnections().get(lastIndex);
                    String departure = connection.getFrom().getDeparture();
                    String departureDateStr = new String();
                    String departureTimeStr = new String();
                    try {
                        departureDateStr = new SimpleDateFormat("yyyy-MM-dd").format(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").parse(departure));
                        departureTimeStr = new SimpleDateFormat("HH:mm").format(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").parse(departure));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    new SearchWorker().execute(tvResultFrom.getText().toString(), tvResultTo.getText().toString(), via.getText().toString(), departureDateStr, departureTimeStr, isArrivalTime);
                }
            }
        });
    }

    protected String calcTime(String first, String second) {
        String result = new String();

        try {
            String a = new SimpleDateFormat("HH:mm").format(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").parse(first));
            String b = new SimpleDateFormat("HH:mm").format(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").parse(second));

            String[] arr = a.split("[d:]+");
            int hoursA = Integer.parseInt(arr[0]);
            int minutesA = Integer.parseInt(arr[1]);

            arr = b.split("[d:]+");
            int hoursB = Integer.parseInt(arr[0]);
            int minutesB = Integer.parseInt(arr[1]);

            int diffMinutes = minutesB - minutesA;
            int diffHours = (hoursB - hoursA);
            int newMinutes = minutesA - diffMinutes;
            int newHoures = hoursA - diffHours;
            newHoures -= newMinutes < 0 ? 1 : 0;
            newMinutes = newMinutes < 0 ? 60 - Math.abs(newMinutes) : newMinutes;


            result = new SimpleDateFormat("yyyy-MM-dd'T'").format(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").parse(first));
            result += newHoures + ":" + newMinutes + ":00" + new SimpleDateFormat("Z").format(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").parse(first));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return result;
    }

    class SearchWorker extends AsyncTask<Object, Integer, ConnectionList> {
        private IOpenTransportRepository connectionSearch;
        private ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(ResultListActivity.this);
            progressDialog.setMessage("Suche Verbindung...");
            progressDialog.setIndeterminate(false);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setCancelable(true);
            progressDialog.show();

            connectionSearch = OpenTransportRepositoryFactory.CreateOnlineOpenTransportRepository();
        }

        @Override
        protected void onPostExecute(ConnectionList result) {
            super.onPostExecute(result);

            if(result != null && result.getConnections().size() > 0) {
                Globals.connectionList = result;

                ConnectionAdapter adapter = new ConnectionAdapter(context);
                listView.setAdapter(adapter);

                checkFromTo();
            }

            progressDialog.dismiss();

            if(result == null || result.getConnections().size() == 0) {
                Toast.makeText(context, R.string.sorry, Toast.LENGTH_LONG).show();
                finish();
            }
        }

        @Override
        protected ConnectionList doInBackground(Object... arg) {
            return connectionSearch.searchConnections((String)arg[0]/*from*/, (String)arg[1]/*to*/, (String)arg[2]/*via*/, (String)arg[3]/*date*/, (String)arg[4]/*time*/, (boolean)arg[5]/*isArrivalTime*/);
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            case R.id.action_about:
                final Intent intent = new Intent(this, AboutActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void startDetailsView(String from, String to, String date, String time) {
        Intent intent = new Intent(this, DetailsActivity.class);

        intent.putExtra("from", from);
        intent.putExtra("to", to);
        intent.putExtra("date", date);
        intent.putExtra("time", time);

        startActivity(intent);
    }

    private void selectArrivalDepartureLabel(boolean isArrivalTime, TextView tvTime) {
        if (isArrivalTime) {
            tvTime.setText(R.string.timeIsArrival);
        } else
            tvTime.setText(R.string.timeIsDeparture);
    }

    protected void checkFromTo() {
        if(!Globals.connectionList.getConnections().isEmpty()) {
            Connection connection = Globals.connectionList.getConnections().get(0);
            String from = connection.getFrom().getLocation().getName().toString();
            if (!tvResultFrom.getText().toString().equals(from)) {
                tvResultFrom.setText(from);
            }

            String to = connection.getTo().getLocation().getName().toString();
            if (!tvResultTo.getText().toString().equals(to)) {
                tvResultTo.setText(to);
            }
        }
    }
};
