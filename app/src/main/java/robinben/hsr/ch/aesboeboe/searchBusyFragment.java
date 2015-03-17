package robinben.hsr.ch.aesboeboe;

import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;



public class searchBusyFragment extends DialogFragment {

    public static searchBusyFragment newInstance() {

        return new searchBusyFragment();
    }

    public searchBusyFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setCancelable(false);
        setStyle(STYLE_NO_TITLE, 0);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

       return inflater.inflate(R.layout.fragment_search_busy_dialog, container, false);

    }


}

