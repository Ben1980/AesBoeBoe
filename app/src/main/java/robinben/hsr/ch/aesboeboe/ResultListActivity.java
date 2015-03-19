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
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import ch.schoeb.opendatatransport.IOpenTransportRepository;
import ch.schoeb.opendatatransport.OpenTransportRepositoryFactory;
import ch.schoeb.opendatatransport.model.Connection;
import ch.schoeb.opendatatransport.model.ConnectionList;


public class ResultListActivity extends Activity {
    private TextView tvResultFrom;
    private TextView tvResultTo;
    private TextView tvResultDate;
    private TextView tvResultTime;
    private ListView listView;
    private Context context = this;
    private SearchWorker searchWorker = new SearchWorker();

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

        boolean isArrivalTime = intent.getBooleanExtra("isArrivalTime", false);
        tvResultFrom.setText(intent.getStringExtra("from"));
        tvResultTo.setText(intent.getStringExtra("to"));
        tvResultDate.setText(intent.getStringExtra("date"));
        tvResultTime.setText(intent.getStringExtra("time"));
        TextView via = new TextView(this);
        via.setText(intent.getStringExtra("via"));

        selectArrivalDepartureLabel(isArrivalTime, tvTime);

        searchWorker.execute(tvResultFrom.getText().toString(), tvResultTo.getText().toString(), via.getText().toString(), tvResultDate.getText().toString(), tvResultTime.getText().toString(), isArrivalTime);

        listView = (ListView)findViewById(R.id.listView);

        listView.setOnItemClickListener(new ListView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Globals.connection = Globals.connectionList.getConnections().get(position);

                startDetailsView(tvResultFrom.getText().toString(), tvResultTo.getText().toString(), tvResultDate.getText().toString(), tvResultTime.getText().toString());
            }
        });
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
