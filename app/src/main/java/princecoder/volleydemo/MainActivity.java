package princecoder.volleydemo;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import java.util.ArrayList;

import princecoder.volleydemo.model.Actors;
import princecoder.volleydemo.presenter.Presenter;
import princecoder.volleydemo.utils.L;


/**
 * @author prinzlyngotoum
 */
public class MainActivity extends ActionBarActivity implements IActivity {

    private ArrayList<Actors> mActorsList;
    private ListView mlistview;
    private String TAG = getClass().getSimpleName();
    private ActorAdapter mAdapter;
    private ProgressDialog mProgressDialog;
    private PlaceHolderFragment mFragment;
    private Presenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mActorsList = new ArrayList<Actors>();

        if (savedInstanceState == null) {
            mFragment = new PlaceHolderFragment();
            getFragmentManager().beginTransaction().add(mFragment, "placeHolderFragment").commit();
        } else {
            mFragment = (PlaceHolderFragment) getFragmentManager().findFragmentByTag("placeHolderFragment");
            if (savedInstanceState.getSerializable("listOfActorSaved") != null) {
                setLisOfActors((ArrayList<Actors>) savedInstanceState.getSerializable("listOfActorSaved"));
            }
        }
        //Initialize the adapter
        mAdapter = new ActorAdapter(getApplicationContext(), R.layout.row, getLisOfActors());

        //Initialize the presenter
        mPresenter = new Presenter(mFragment, this);

        mlistview = (ListView) findViewById(R.id.list);
        // Set the adapter
        mlistview.setAdapter(mAdapter);
        mlistview.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position,
                                    long id) {
                // TODO Auto-generated method stub
                mPresenter.displayMessage(mActorsList.get(position).getName());
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_getactors) {
            if (mFragment.isAdded()) {//Check if the fragment is added
                //I use the presenter to perform task
                mPresenter.performTask();
            }
        }
        return false;
    }

    /**
     * Get the list of Actors
     *
     * @return
     */
    @Override
    public ArrayList<Actors> getLisOfActors() {
        return mActorsList;
    }


    /**
     * Set the list of actors
     *
     * @param list
     */
    public void setLisOfActors(ArrayList<Actors> list) {
        mActorsList = list;
    }


    public ActorAdapter getAdapter() {
        return mAdapter;
    }

    public void setAdapter(ActorAdapter mAdapter) {
        this.mAdapter = mAdapter;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // Save the list of actors
        outState.putSerializable("listOfActorSaved", getLisOfActors());
    }

    @Override
    public void updateAdapter(boolean result) {
        mAdapter.notifyDataSetChanged();
        if (result == false)
            L.toast(getApplicationContext(), "Unable to fetch data from server");
    }

    @Override
    public void beforeFetching() {
        mProgressDialog = ProgressDialog.show(this, "Working", "Please wait !!!");
    }

    @Override
    public void afterFetching() {
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
        }
    }

    //To avoid the :Activity has leaked window com.android.internal.policy... exception
    @Override
    public void Detach() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        L.m("Main activity is stopped");
        mPresenter.onStop();
    }
}
