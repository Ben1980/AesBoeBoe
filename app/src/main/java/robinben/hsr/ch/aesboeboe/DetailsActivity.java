package robinben.hsr.ch.aesboeboe;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;


public class DetailsActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        TextView tvResultFrom = (TextView) findViewById(R.id.tvResultFrom);
        TextView tvResultTo = (TextView) findViewById(R.id.tvResultTo);
        TextView tvResultDate = (TextView) findViewById(R.id.tvResultDate);
        TextView tvResultTime = (TextView) findViewById(R.id.tvResultTime);
        TextView tvTime = (TextView) findViewById(R.id.tvTime);

        Intent intent = getIntent();

        boolean isArrivalTime = intent.getBooleanExtra("isArrivalTime", false);
        tvResultFrom.setText(intent.getStringExtra("from"));
        tvResultTo.setText(intent.getStringExtra("to"));
        tvResultDate.setText(intent.getStringExtra("date"));
        tvResultTime.setText(intent.getStringExtra("time"));

        selectArrivalDepartureLabel(isArrivalTime, tvTime);

        DetailsAdapter adapter = new DetailsAdapter(this);

        ListView listView = (ListView)findViewById(R.id.detailsView);
        listView.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
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

    private void selectArrivalDepartureLabel (boolean isArrivalTime, TextView tvTime) {
        if (isArrivalTime) {
            tvTime.setText(R.string.timeIsArrival);
        } else
            tvTime.setText(R.string.timeIsDeparture);
    }
}
