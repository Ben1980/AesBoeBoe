package robinben.hsr.ch.aesboeboe;

import android.os.AsyncTask;

import ch.schoeb.opendatatransport.IOpenTransportRepository;
import ch.schoeb.opendatatransport.OpenTransportRepositoryFactory;
import ch.schoeb.opendatatransport.model.ConnectionList;
import ch.schoeb.opendatatransport.model.Station;
import ch.schoeb.opendatatransport.model.StationList;

/**
 * Created by Robin on 05.03.2015.
 */
public class WorkerAutoComplete extends AsyncTask<String, Integer, String[]> {
    private IOpenTransportRepository connectionSearch;
    private StationList stationObjectList;
    private String[] stationNameList;

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        connectionSearch = OpenTransportRepositoryFactory.CreateOnlineOpenTransportRepository();
        }

    @Override
    protected void onPostExecute(String[] result) {
        super.onPostExecute(result);
        result = stationNameList;
        }

    @Override
    protected String[] doInBackground(String... arg) {

        stationObjectList = connectionSearch.findStations(arg[0]);

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

