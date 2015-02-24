package princecoder.volleydemo.presenter;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

import princecoder.volleydemo.PlaceHolderFragment;
import princecoder.volleydemo.utils.L;

/**
 * Created by prinzlyngotoum on 2/18/15.
 */
public class Presenter implements IPresenter {

    PlaceHolderFragment myFragment;
    Context mContext;

    public Presenter(PlaceHolderFragment fragment, Context context){
        this.myFragment=fragment;
        this.mContext=context;
    }
    @Override
    public void performTask() {
        if (isOnline()) {
            myFragment.requestData();
        } else {
            Toast.makeText(mContext, "Network isn't available", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Are we online?
     *
     * @return
     */
    protected boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void displayMessage(String s) {
        L.toast(mContext, s);
    }

    @Override
    public void onStop() {
        myFragment.canCellRequest();
    }
}
