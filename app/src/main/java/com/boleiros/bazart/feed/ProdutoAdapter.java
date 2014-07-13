package com.boleiros.bazart.feed;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.boleiros.bazart.R;
import com.boleiros.bazart.modelo.Produto;
import com.parse.ParseException;
import com.parse.ParseFile;

import java.lang.ref.WeakReference;
import java.util.List;
/**
 * Created by Filipe Ramos on 11/07/14.
 *
 */
public class ProdutoAdapter extends BaseAdapter {
    private Context context;
    private List<Produto> items;
    private Bitmap mPlaceHolderBitmap;
    private LruCache<String, Bitmap> mMemoryCache;
    public ProdutoAdapter(Context context, List<Produto> items) {
        this.context = context;
        this.items = items;
        mPlaceHolderBitmap = decodeSampledBitmapFromResource(context.getResources(), R.drawable.logoresol, 120, 120);
        // Get memory class of this device, exceeding this amount will throw an
        // OutOfMemory exception.
        final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
        // Use 1/8th of the available memory for this memory cache.
        final int cacheSize = maxMemory / 8;
        mMemoryCache = new LruCache<String, Bitmap>(cacheSize) {
            protected int sizeOf(String key, Bitmap bitmap) {
                // The cache size will be measured in bytes rather than number
                // of items.
                if (Integer.valueOf(android.os.Build.VERSION.SDK_INT) >= 12) {
                    return bitmap.getByteCount();
                } else {
                    return (bitmap.getRowBytes() * bitmap.getHeight());
                }
            }

        };
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int arg0) {
        return items.get(arg0);
    }

    @Override
    public long getItemId(int arg0) {
        return arg0;
    }

    @Override
    public View getView(int arg0, View convertView, ViewGroup arg2) {
        ViewHolder holderPattern;
        ImageView img = null;
        if(convertView==null) {
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_element_produto, null);
            holderPattern = new ViewHolder();
            holderPattern.textViewSetNomeUsuario = (TextView)convertView.findViewById(R.id.textViewSetNomeUsuario);
            holderPattern.textViewSetContato = (TextView)convertView.findViewById(R.id.textViewSetContato);
            holderPattern.textViewSetPreco = (TextView)convertView.findViewById(R.id.textViewSetPreco);
            holderPattern.textViewSetHashTags = (TextView)convertView.findViewById(R.id.textViewSetHashTags);
            holderPattern.fotoProduto = (ImageView) convertView.findViewById(R.id.imageView);
            convertView.setTag(holderPattern);
        }else {
            holderPattern = (ViewHolder) convertView.getTag();
        }

        holderPattern.textViewSetNomeUsuario.setText(items.get(arg0).getAuthor().getUsername());
        holderPattern.textViewSetContato.setText(items.get(arg0).getPhoneNumber());
        holderPattern.textViewSetPreco.setText(items.get(arg0).getPrice());
        holderPattern.textViewSetHashTags.setText("");

        // int resId = context.getResources().getIdentifier(items.get(arg0).,
                //"drawable", context.getPackageName());
        ParseFile pf = items.get(arg0).getPhotoFile();
        loadBitmap(pf, holderPattern.fotoProduto);
        return convertView;
    }
    static class ViewHolder {
        TextView textViewSetNomeUsuario;
        TextView textViewSetPreco;
       // TextView textViewContato;
        TextView textViewSetContato;
       // TextView textViewHashTags;
        TextView textViewSetHashTags;
        ImageView fotoProduto;
    }
    public void loadBitmap(ParseFile pf, ImageView imageView) {
        if (cancelPotentialWork(pf.hashCode(), imageView)) {
            final BitmapWorkerTask task = new BitmapWorkerTask(imageView);
            final AsyncDrawable asyncDrawable =
                    new AsyncDrawable(context.getResources(),mPlaceHolderBitmap , task);
            imageView.setImageDrawable(asyncDrawable);
            task.execute(pf);
        }
    }
    static class AsyncDrawable extends BitmapDrawable {
        private final WeakReference<BitmapWorkerTask> bitmapWorkerTaskReference;
        public AsyncDrawable(Resources res, Bitmap bitmap,
                             BitmapWorkerTask bitmapWorkerTask) {
            super(res, bitmap);
            bitmapWorkerTaskReference =
                    new WeakReference<BitmapWorkerTask>(bitmapWorkerTask);
        }
        public BitmapWorkerTask getBitmapWorkerTask() {
            return bitmapWorkerTaskReference.get();
        }
    }
    public static boolean cancelPotentialWork(int data, ImageView imageView) {
        final BitmapWorkerTask bitmapWorkerTask = getBitmapWorkerTask(imageView);
        if (bitmapWorkerTask != null) {
            final int bitmapData = bitmapWorkerTask.data;
            if (bitmapData != data) {
                // Cancel previous task
                bitmapWorkerTask.cancel(true);
            } else {
                // The same work is already in progress
                return false;
            }
        }
        // No task associated with the ImageView, or an existing task was
        // cancelled
        return true;
    }

    private static BitmapWorkerTask getBitmapWorkerTask(ImageView imageView) {
        if (imageView != null) {
            final Drawable drawable = imageView.getDrawable();
            if (drawable instanceof AsyncDrawable) {
                final AsyncDrawable asyncDrawable = (AsyncDrawable) drawable;
                return asyncDrawable.getBitmapWorkerTask();
            }
        }
        return null;
    }



    class BitmapWorkerTask extends AsyncTask<ParseFile, Void, Bitmap> {
        public int data = 0;
        private final WeakReference<ImageView> imageViewReference;

        public BitmapWorkerTask(ImageView imageView) {
            // Use a WeakReference to ensure the ImageView can be garbage
            // collected
            imageViewReference = new WeakReference<ImageView>(imageView);
        }

        // Decode image in background.
        @Override
        protected Bitmap doInBackground(ParseFile... params) {
            data = params[0].hashCode();
            Bitmap bitmap = null;
            if(getBitmapFromMemCache(String.valueOf(params[0]))!=null){
                return getBitmapFromMemCache(String.valueOf(params[0]));
            }
            try {
                 //bitmap  = BitmapFactory.decodeByteArray(params[0].getData(), 0, params[0].getData().length);
               bitmap = decodeSampledBitmapFromResource(params[0],650,650);
            } catch (ParseException e1) {
                e1.printStackTrace();
            }
            addBitmapToMemoryCache(String.valueOf(params[0]), bitmap);
            return bitmap;
        }

        // Once complete, see if ImageView is still around and set bitmap.
        @Override
        protected void onPostExecute(Bitmap bitmap) {
            if (isCancelled()) {
                bitmap = null;
            }
            if (imageViewReference != null && bitmap != null) {
                final ImageView imageView = imageViewReference.get();
                if (imageView != null) {
                    imageView.setImageBitmap(bitmap);
                }
            }
        }
    }
    public void addBitmapToMemoryCache(String key, Bitmap bitmap) {
        if (getBitmapFromMemCache(key) == null) {
            mMemoryCache.put(key, bitmap);
        }
    }

    public Bitmap getBitmapFromMemCache(String key) {
        return (Bitmap) mMemoryCache.get(key);
    }
    public static Bitmap decodeSampledBitmapFromResource(ParseFile pf,
                                                          int reqWidth, int reqHeight) throws ParseException {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeByteArray(pf.getData(), 0, pf.getData().length,options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth,
                reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeByteArray(pf.getData(), 0, pf.getData().length,options);
    }

    public static int calculateInSampleSize(BitmapFactory.Options options,
                                            int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            // Calculate ratios of height and width to requested height and
            // width
            final int heightRatio = Math.round((float) height
                    / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);

            // Choose the smallest ratio as inSampleSize value, this will
            // guarantee
            // a final image with both dimensions larger than or equal to the
            // requested height and width.
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }

        return inSampleSize;
    }
    public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId,
                                                         int reqWidth, int reqHeight) {
        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);
        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, options);
    }
}