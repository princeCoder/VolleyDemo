package princecoder.volleydemo;

import java.util.ArrayList;

import princecoder.volleydemo.model.Actors;

/**
 * Created by prinzlyngotoum on 12/4/14.
 */
public interface IActivity {

    public void updateAdapter(boolean result);

    public void beforeFetching();

    public void afterFetching();

    public void Detach();

    public ArrayList<Actors> getLisOfActors();
}
