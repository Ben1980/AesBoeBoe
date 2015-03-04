package robinben.hsr.ch.aesboeboe;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


public class MainActivity extends ActionBarActivity {
    private EditText from;
    private EditText to;
    private EditText date;
    private EditText time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button search = (Button) findViewById(R.id.btSearch);
        Button oppositeDirection = (Button) findViewById(R.id.btOppositeDirection);
        from = (EditText) findViewById(R.id.etFromField);
        to = (EditText) findViewById(R.id.etToField);
        date = (EditText) findViewById(R.id.etDateField);
        time = (EditText) findViewById(R.id.etTimeField);

        search.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startSearch();
            }
        });

        oppositeDirection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fromTemp = from.getText().toString();
                from.setText(to.getText().toString());
                to.setText(fromTemp);

            }
        });


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

    private void startSearch() {
        Intent intent = new Intent(this, ResultListActivity.class);

        intent.putExtra("from", from.getText().toString());
        intent.putExtra("to", to.getText().toString());
        intent.putExtra("date", date.getText().toString());
        intent.putExtra("time", time.getText().toString());

        startActivity(intent);
    }
}
