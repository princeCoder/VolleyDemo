package princecoder.volleydemo.utils;

import android.view.View;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;

import princecoder.volleydemo.R;

/**
 * Created by prinzlyngotoum on 12/4/14.
 */
public class ViewHolder {
    public NetworkImageView imageview;
    public TextView tvName;
    public TextView tvDescription;
    public TextView tvDOB;
    public TextView tvCountry;
    public TextView tvHeight;
    public TextView tvSpouse;
    public TextView tvChildren;

    public ViewHolder(View v) {
        this.imageview = (NetworkImageView) v.findViewById(R.id.ivImage);
        this.tvName = (TextView) v.findViewById(R.id.tvName);
        this.tvDescription = (TextView) v.findViewById(R.id.tvDescriptionn);
        this.tvDOB = (TextView) v.findViewById(R.id.tvDateOfBirth);
        this.tvCountry = (TextView) v.findViewById(R.id.tvCountry);
        this.tvHeight = (TextView) v.findViewById(R.id.tvHeight);
        this.tvSpouse = (TextView) v.findViewById(R.id.tvSpouse);
        this.tvChildren = (TextView) v.findViewById(R.id.tvChildren);
    }
}
