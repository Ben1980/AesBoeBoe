package robinben.hsr.ch.aesboeboe;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;

import java.util.concurrent.ExecutionException;

import ch.schoeb.opendatatransport.model.ConnectionList;


public class ResultListActivity extends ActionBarActivity {
    private TextView tvResultFrom;
    private TextView tvResultTo;
    private TextView tvResultDate;
    private TextView tvResultTime;
    private ConnectionList list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_list);

        tvResultFrom = (TextView) findViewById(R.id.tvResultFrom);
        tvResultTo = (TextView) findViewById(R.id.tvResultTo);
        tvResultDate = (TextView) findViewById(R.id.tvResultDate);
        tvResultTime = (TextView) findViewById(R.id.tvResultTime);

        Intent intent = getIntent();

        tvResultFrom.setText(intent.getStringExtra("from"));
        tvResultTo.setText(intent.getStringExtra("to"));
        tvResultDate.setText(intent.getStringExtra("date"));
        tvResultTime.setText(intent.getStringExtra("time"));

        Worker worker = new Worker();
        worker.execute(intent.getStringExtra("from"),intent.getStringExtra("to"));
        try {
            //new Worker().execute(intent.getStringExtra("from"), intent.getStringExtra("to"));
            list = worker.get();

            //ListView listview = (ListView) findViewById(R.id.listView);
            //listview.add
        }
        catch (ExecutionException e) {
            e.printStackTrace();
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_result_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
