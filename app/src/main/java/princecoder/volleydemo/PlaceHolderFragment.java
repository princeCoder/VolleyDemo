package princecoder.volleydemo;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.List;

import princecoder.volleydemo.model.Actors;
import princecoder.volleydemo.parsers.ActorJSONParser;
import princecoder.volleydemo.utils.L;

/**
 * Created by prinzlyngotoum on 12/4/14.
 */
public class PlaceHolderFragment extends Fragment {

    private IActivity mActivity;
    private StringRequest mRequest;
    private String TAG=getClass().getSimpleName();

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        setActivity((IActivity) activity);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setRetainInstance(true);// I don't want the fragment to get destroyed
    }

    public void setActivity(IActivity activity){
        this.mActivity=activity;
    }


    @Override
    public void onDetach() {
        super.onDetach();
        mActivity.Detach();
    }

    /**
     * Request data from the server
     */
    public void requestData() {

        mActivity.beforeFetching();
        mRequest =new StringRequest(getResources().getString(R.string.url),
                new Response.Listener<String>(){
                    @Override
                    public void onResponse(String response) {
                        List<Actors> actorsList = ActorJSONParser.parseFeed(response);
                      mActivity.getLisOfActors().clear();
                        for (Actors actor : actorsList) {
                            mActivity.getLisOfActors().add(actor);
                        }
                        //Update elements
                        mActivity.updateAdapter(true);

                        // hide the progress dialog
                        mActivity.afterFetching();
                    }
                },new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError ex) {
                // hide the progress dialog
                mActivity.afterFetching();
                L.toast(getActivity(), ex.getMessage());
            }
        }){
            @Override
            public Priority getPriority() {
                return Priority.IMMEDIATE;
            }
        };

        // Have to add the request to the queue
        VolleyLauncher.getInstance().addToRequestQueue(mRequest,TAG);
    }

    /**
     * Cancel all the current request in the queue
     */
    public void canCellRequest(){
        VolleyLauncher.getInstance().cancelAll(TAG);
    }

}
