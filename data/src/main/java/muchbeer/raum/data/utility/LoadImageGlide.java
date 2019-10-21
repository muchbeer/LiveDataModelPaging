package muchbeer.raum.data.utility;

import android.widget.ImageView;

import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import muchbeer.raum.data.R;

public class LoadImageGlide {

    public static void loadimage(ImageView imageView, String imageUrl, CircularProgressDrawable circularProgressDrawable) {

        RequestOptions options = new RequestOptions()
                .placeholder(circularProgressDrawable)
				.error(android.R.drawable.stat_sys_warning)
                .circleCrop()
                .fitCenter();

        Glide.with(imageView.getContext())
                .setDefaultRequestOptions(options)
                .load(imageUrl)
                .into(imageView);

    }
}
