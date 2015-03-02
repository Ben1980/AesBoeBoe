package robinben.hsr.ch.aesboeboe;

import android.os.AsyncTask;

import ch.schoeb.opendatatransport.IOpenTransportRepository;
import ch.schoeb.opendatatransport.OpenTransportRepositoryFactory;
import ch.schoeb.opendatatransport.model.ConnectionList;

/**
* Created by Ben on 01.03.15.
*/
class Worker extends AsyncTask<String, Integer, ConnectionList> {
    private IOpenTransportRepository connectionSearch;
    private ConnectionList connectionList;

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        connectionSearch = OpenTransportRepositoryFactory.CreateOnlineOpenTransportRepository();
    }

    @Override
    protected void onPostExecute(ConnectionList result) {
        super.onPostExecute(result);
        result = connectionList;
    }

    @Override
    protected ConnectionList doInBackground(String... arg) {

        connectionList = connectionSearch.searchConnections(arg[0], arg[1]);

        return connectionList;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
    }
}