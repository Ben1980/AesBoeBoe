package robinben.hsr.ch.aesboeboe;

import android.os.AsyncTask;

import ch.schoeb.opendatatransport.IOpenTransportRepository;
import ch.schoeb.opendatatransport.OpenTransportRepositoryFactory;
import ch.schoeb.opendatatransport.model.ConnectionList;

/**
* Created by Ben on 01.03.15.
*/
class Worker extends AsyncTask<Object, Integer, ConnectionList> {
    private IOpenTransportRepository connectionSearch;

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        connectionSearch = OpenTransportRepositoryFactory.CreateOnlineOpenTransportRepository();
    }

    @Override
    protected void onPostExecute(ConnectionList result) {
        super.onPostExecute(result);
    }

    @Override
    protected ConnectionList doInBackground(Object... arg) {
        Globals.connectionList = connectionSearch.searchConnections((String)arg[0]/*from*/, (String)arg[1]/*to*/, (String)arg[2]/*via*/, (String)arg[3]/*date*/, (String)arg[4]/*time*/, (boolean)arg[5]/*isArrivalTime*/);

        return Globals.connectionList;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
    }
}
