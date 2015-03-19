package robinben.hsr.ch.aesboeboe;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.ShareActionProvider;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.ToggleButton;
import ch.schoeb.opendatatransport.IOpenTransportRepository;
import ch.schoeb.opendatatransport.OpenTransportRepositoryFactory;
import ch.schoeb.opendatatransport.model.Station;
import ch.schoeb.opendatatransport.model.StationList;


public class MainActivity extends Activity {
    private AutoCompleteTextView from;
    private AutoCompleteTextView via;
    private AutoCompleteTextView to;
    private DatePicker date;
    private TimePicker time;
    private String[] stationNameList = new String[]{};
    private ArrayAdapter stationListAdapter;
    private Context mainActivityContext;
    private ToggleButton isArrivalTime;
    private ShareActionProvider mShareActionProvider;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mainActivityContext = this;

        ImageButton search = (ImageButton) findViewById(R.id.btSearch);
        ImageButton oppositeDirection = (ImageButton) findViewById(R.id.btOppositeDirection);
        from = (AutoCompleteTextView) findViewById(R.id.etFromField);
        via = (AutoCompleteTextView) findViewById(R.id.etViaField);
        to = (AutoCompleteTextView) findViewById(R.id.etToField);
        date = (DatePicker) findViewById(R.id.datePicker);
        time = (TimePicker) findViewById(R.id.timePicker);
        time.setIs24HourView(true);
        isArrivalTime = (ToggleButton) findViewById(R.id.btIsArrivalTime);
        isArrivalTime.setTextOff(getString(R.string.departureTime));
        isArrivalTime.setTextOn(getString(R.string.arrivalTime));

        stationListAdapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,stationNameList);
        from.setAdapter(stationListAdapter);

        stationListAdapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,stationNameList);
        to.setAdapter(stationListAdapter);

        stationListAdapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,stationNameList);
        via.setAdapter(stationListAdapter);

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

            };
        });

        ImageButton discardFrom = (ImageButton) findViewById(R.id.discard1);
        ImageButton discardTo = (ImageButton) findViewById(R.id.discard2);
        ImageButton discardVia = (ImageButton) findViewById(R.id.discard3);

        discardFrom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                from.setText("");
            }
        });

        discardTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                to.setText("");
            }
        });

        discardVia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                via.setText("");
            }
        });

        ImageButton home = (ImageButton) findViewById(R.id.btHome);
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences settings = getSharedPreferences("Home", 0);
                to.setText(settings.getString("home", ""));

            }

        });


        home.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                SharedPreferences settings = getSharedPreferences("Home", 0);
                SharedPreferences.Editor editor = settings.edit();
                editor.putString("home", from.getText().toString());
                editor.apply();

                CharSequence text = from.getText().toString() + "  " + getString(R.string.homeSavedToast);
                Toast toast = Toast.makeText(mainActivityContext, text, Toast.LENGTH_LONG);
                toast.setGravity(Gravity.TOP|Gravity.LEFT, 250, 400);
                toast.show();
                return true;
            }
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
        protected void onResume(){
            super.onResume();

           SharedPreferences settings = getSharedPreferences("AesBoeBoe", 0);
           from.setText(settings.getString("from", ""));
           via.setText(settings.getString("via", ""));
           to.setText(settings.getString("to", ""));
           isArrivalTime.setChecked(settings.getBoolean("isArrivalTime", isArrivalTime.isChecked()));

        };


    @Override
    protected void onPause(){
        super.onPause();

        SharedPreferences settings = getSharedPreferences("AesBoeBoe", 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("from", from.getText().toString());
        editor.putString("via", via.getText().toString());
        editor.putString("to", to.getText().toString());
        editor.putBoolean("isArrivalTime", isArrivalTime.isChecked());

        editor.commit();

    };



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        MenuItem menuItem = menu.findItem(R.id.menu_item_share);
        mShareActionProvider = (ShareActionProvider) menuItem.getActionProvider();
        mShareActionProvider.setShareIntent(doShare());


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
    public Intent doShare() {

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, getTextToShare());
        return intent;
    }

    private void startSearch() {
        Intent intent = new Intent(this, ResultListActivity.class);

        intent.putExtra("from", from.getText().toString());
        intent.putExtra("to", to.getText().toString());
        intent.putExtra("date", getDateString(date));
        intent.putExtra("time", getTimeString(time));
        intent.putExtra("via", via.getText().toString());
        intent.putExtra("isArrivalTime", isArrivalTime.isChecked());

        startActivity(intent);
    }

    private void checkAutoCompleteList(CharSequence s, int before, int count, AutoCompleteTextView view) {
        if (count > before){
            if(s.length() >= 2){

                new WorkerAutoComplete().execute(s.toString());
                stationListAdapter = new ArrayAdapter<>(mainActivityContext,android.R.layout.simple_list_item_1,stationNameList);
                view.setAdapter(stationListAdapter);

            }

        }
    }

    public class WorkerAutoComplete extends AsyncTask<String, Integer, String[]> {
        private IOpenTransportRepository connectionSearch;
        private StationList stationObjectList;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            connectionSearch = OpenTransportRepositoryFactory.CreateOnlineOpenTransportRepository();
        }

        @Override
        protected void onPostExecute(String[] result) {
            super.onPostExecute(result);
            stationNameList = result;
        }

        @Override
        protected String[] doInBackground(String... arg) {

            stationObjectList = connectionSearch.findStations(arg[0]);
            if (stationObjectList == null){
                return new String[0];
            }

            stationNameList = new String[stationObjectList.getStations().size()];

            int i = 0;
            for (Station s : stationObjectList.getStations()) {
                stationNameList[i] = s.getName();
                i++;
            }
            return stationNameList;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }
    };

    private String getDateString(DatePicker date) {
        int day = date.getDayOfMonth();
        int month = date.getMonth() + 1; // -> datepicker starts with January == 0
        int year = date.getYear();

        return new String(day + "." + month + "." + year);
    }

    private String getTimeString(TimePicker time) {
        int hour = time.getCurrentHour();
        int minute = time.getCurrentMinute();

        return new String(hour + ":" + (minute > 0 ? minute > 9 ? minute : "0" + minute : "00"));
    }

    private String getTextToShare(){
         String shareText =
               "Von:" + "\t"  + from.getText().toString() + "\r\n" +
               "Nach:"+ "\t"  +to.getText().toString() + "\r\n" +
               "Datum:"+ "\t"  + getDateString(date) + "\r\n" +
               "Zeit:"+ "\t" +getTimeString(time);



        return shareText;
    };



}
