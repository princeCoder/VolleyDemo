package princecoder.volleydemo;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.android.volley.toolbox.ImageLoader;

import java.util.ArrayList;

import princecoder.volleydemo.model.Actors;
import princecoder.volleydemo.utils.ViewHolder;

public class ActorAdapter extends ArrayAdapter<Actors> {
    private ArrayList<Actors> actorList;
    private LayoutInflater vi;
    private int Resource;
    private ViewHolder holder;
    private LruCache<Integer, Bitmap> imageCache;
    private Context mContext;


    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR1)
    public ActorAdapter(Context context, int resource, ArrayList<Actors> objects) {
        super(context, resource, objects);
        vi = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        Resource = resource;
        actorList = objects;
        mContext = context;

        final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
        final int cacheSize = maxMemory / 8;
        imageCache = new LruCache<>(cacheSize);

    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR1)
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;
        if (v == null) {
            v = vi.inflate(Resource, parent, false);
            holder = new ViewHolder(v);
            v.setTag(holder);
        } else {
            holder = (ViewHolder) v.getTag();
        }

        //holder.imageview.setImageResource(R.drawable.ic_launcher);
        final Actors actor = actorList.get(position);
        holder.tvName.setText(actor.getName());
        holder.tvDescription.setText(actor.getDescription());
        holder.tvDOB.setText("B'day: " + actor.getDob());
        holder.tvCountry.setText(actor.getCountry());
        holder.tvHeight.setText("Height: " + actor.getHeight());
        holder.tvSpouse.setText("Spouse: " + actor.getSpouse());
        holder.tvChildren.setText("Children: " + actor.getChildren());

        // Display the actor image in the NetworkImageview Widget
        String imageUrl = actor.getImage();

        // Here I use the Image cache which is not good when we have lot of images
        ImageLoader imageLoader = VolleyLauncher.getInstance().getImageLoader();

        // Here I use the Disk cache
//        ImageLoader.ImageCache imageCache = new LruBitmapCache();
//        ImageLoader imageLoader = new ImageLoader(VolleyLauncher.getInstance().newRequestQueue(getContext()), imageCache);

        holder.imageview.setImageUrl(imageUrl, imageLoader);
        return v;
    }
}
