package in.th.llun.tent;

import java.net.URL;

import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.util.LruCache;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class ImageLoader extends AsyncTask<String, Void, Bitmap> {
	private ImageView imageView;
	private ViewGroup loadingView;

	// This is for prevent out of memory
	private static int cacheSize = 10 * 1024 * 1024;
	private static LruCache<String, Bitmap> cached = new LruCache<String, Bitmap>(
	    cacheSize) {
		@Override
		@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR1)
		protected int sizeOf(String key, Bitmap bitmap) {
			try {
				return bitmap.getByteCount();
			} catch (NoSuchMethodError e) {
				return bitmap.getRowBytes() * bitmap.getHeight();
			}
		}

	};

	public ImageLoader(ImageView imageView, ViewGroup loadingView) {
		this.imageView = imageView;
		this.loadingView = loadingView;
	}

	@Override
	protected Bitmap doInBackground(String... urls) {
		try {
			URL url = new URL(urls[0]);
			Bitmap cacheBitmap = cached.get(url.toString());
			if (cacheBitmap != null) {
				return cacheBitmap;
			} else {
				Bitmap out = BitmapFactory.decodeStream(url.openConnection()
				    .getInputStream());
				cached.put(url.toString(), out);
				return out;
			}
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	protected void onPostExecute(Bitmap bitmap) {
		imageView.setImageBitmap(bitmap);
		if (loadingView != null) {
			loadingView.setVisibility(View.GONE);
		}
	}
}
