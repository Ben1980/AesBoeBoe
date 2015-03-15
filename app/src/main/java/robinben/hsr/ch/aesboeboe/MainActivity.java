package robinben.hsr.ch.aesboeboe;

import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ToggleButton;
import java.util.concurrent.ExecutionException;


public class MainActivity extends ActionBarActivity  {
    private AutoCompleteTextView from;
    private AutoCompleteTextView via;
    private AutoCompleteTextView to;
    private EditText date;
    private EditText time;
    private String[] stationNameList = new String[]{};
    private ArrayAdapter stationListAdapter;
    private Context mainActivityContext;
    private ToggleButton isArrivalTime;
    private FragmentTransaction fragmentTransaction;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mainActivityContext = this;

        Button search = (Button) findViewById(R.id.btSearch);
        Button oppositeDirection = (Button) findViewById(R.id.btOppositeDirection);
        from = (AutoCompleteTextView) findViewById(R.id.etFromField);
        via = (AutoCompleteTextView) findViewById(R.id.etViaField);
        to = (AutoCompleteTextView) findViewById(R.id.etToField);
        date = (EditText) findViewById(R.id.etDateField);
        time = (EditText) findViewById(R.id.etTimeField);
        isArrivalTime = (ToggleButton) findViewById(R.id.btIsArrivalTime);

        stationListAdapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1,stationNameList);
        from.setAdapter(stationListAdapter);

        stationListAdapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1,stationNameList);
        to.setAdapter(stationListAdapter);

        stationListAdapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1,stationNameList);
        via.setAdapter(stationListAdapter);



        search.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                fragmentTransaction = getFragmentManager().beginTransaction();
                Globals.searchBusyFragment = searchBusyFragment.newInstance();
                Globals.searchBusyFragment.show(fragmentTransaction, "searchBusyFragment");
                startSearch();
            }
        });

        oppositeDirection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fromTemp = from.getText().toString();
                from.setText(to.getText().toString());
                to.setText(fromTemp);

            };
        });


        from.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkAutoCompleteList(s, before, count, from);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        to.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkAutoCompleteList(s, before, count, to);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        via.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkAutoCompleteList(s, before, count, via);
            }

            @Override
            public void afterTextChanged(Editable s) {
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


        getConnections(new Worker());

        Intent intent = new Intent(this, ResultListActivity.class);

        intent.putExtra("from", from.getText().toString());
        intent.putExtra("to", to.getText().toString());
        intent.putExtra("date", date.getText().toString());
        intent.putExtra("time", time.getText().toString());
        intent.putExtra("isArrivalTime", isArrivalTime.isChecked());

        startActivity(intent);


    }

    private void getConnections(Worker worker) {

        try {
            worker.execute(from.getText().toString(), to.getText().toString(), via.getText().toString(), date.getText().toString(), time.getText().toString(),isArrivalTime.isChecked()).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

    }

    private void checkAutoCompleteList(CharSequence s, int before, int count, AutoCompleteTextView view) {
        if (count > before){
            if(s.length() == 3){
                stationNameList = lookupStationNames(s);
                stationListAdapter = new ArrayAdapter(mainActivityContext,android.R.layout.simple_list_item_1,stationNameList);
                view.setAdapter(stationListAdapter);

            }

        }
    }

    private String[] lookupStationNames(CharSequence s) {



        try {
            WorkerAutoComplete workerAutoComplete = new WorkerAutoComplete();
            return workerAutoComplete.execute(s.toString()).get();
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        };


        return new String[0];



    };
}
