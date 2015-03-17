package robinben.hsr.ch.aesboeboe;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import ch.schoeb.opendatatransport.IOpenTransportRepository;
import ch.schoeb.opendatatransport.OpenTransportRepositoryFactory;
import ch.schoeb.opendatatransport.model.ConnectionList;


public class ResultListActivity extends ActionBarActivity {
    private TextView tvResultFrom;
    private TextView tvResultTo;
    private TextView tvResultDate;
    private TextView tvResultTime;
    private TextView via;
    private boolean isArrivalTime;
    private TextView tvTime;
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
        tvTime = (TextView) findViewById(R.id.tvTime);

        Intent intent = getIntent();

        isArrivalTime = intent.getBooleanExtra("isArrivalTime", false);
        tvResultFrom.setText(intent.getStringExtra("from"));
        tvResultTo.setText(intent.getStringExtra("to"));
        tvResultDate.setText(intent.getStringExtra("date"));
        tvResultTime.setText(intent.getStringExtra("time"));
        via = new TextView(this);
        via.setText(intent.getStringExtra("via"));
        selectArrivalDepartureLabel();

        searchWorker.execute(tvResultFrom.getText().toString(), tvResultTo.getText().toString(), via.getText().toString(), tvResultDate.getText().toString(), tvResultTime.getText().toString(), isArrivalTime);

        listView = (ListView)findViewById(R.id.listView);

        Globals.searchBusyFragment.dismiss();
        listView.setOnItemClickListener(new ListView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Globals.connection = Globals.connectionList.getConnections().get(position);

                startDetailsView();
            }
        });
    }

    class SearchWorker extends AsyncTask<Object, Integer, ConnectionList> {
        private IOpenTransportRepository connectionSearch;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            connectionSearch = OpenTransportRepositoryFactory.CreateOnlineOpenTransportRepository();
        }

        @Override
        protected void onPostExecute(ConnectionList result) {
            super.onPostExecute(result);

            ConnectionAdapter adapter = new ConnectionAdapter(context, result);
            listView.setAdapter(adapter);

            Globals.connectionList = result;
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

    private void startDetailsView() {
        Intent intent = new Intent(this, DetailsActivity.class);

        intent.putExtra("from", tvResultFrom.getText().toString());
        intent.putExtra("to", tvResultTo.getText().toString());
        intent.putExtra("date", tvResultDate.getText().toString());
        intent.putExtra("time", tvResultTime.getText().toString());

        startActivity(intent);
    }

    private void selectArrivalDepartureLabel () {

        if (isArrivalTime) {
            tvTime.setText(R.string.timeIsArrival);
        } else
            tvTime.setText(R.string.timeIsDeparture);
    }
};
