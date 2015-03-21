package robinben.hsr.ch.aesboeboe;


import android.widget.ShareActionProvider;

import ch.schoeb.opendatatransport.model.Connection;
import ch.schoeb.opendatatransport.model.ConnectionList;

/**
 * Created by Ben on 05.03.15.
 */
public class Globals {
    static public ConnectionList connectionList = null;
    static public Connection connection = new Connection();
    static public ShareActionProvider ShareActionProvider = null;
    static public String headerToShare = "";
}
