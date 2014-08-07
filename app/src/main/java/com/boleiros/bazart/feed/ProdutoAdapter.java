package com.boleiros.bazart.feed;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.boleiros.bazart.R;
import com.boleiros.bazart.modelo.Produto;
import com.boleiros.bazart.util.CustomToast;
import com.boleiros.bazart.util.DoubleClickListener;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;

import java.lang.ref.WeakReference;
import java.util.Date;
import java.util.List;

/**
 * Created by Filipe Ramos on 11/07/14.
 */
public class ProdutoAdapter extends BaseAdapter {
    private Context context;
    private List<Produto> items;
    private Bitmap mPlaceHolderBitmap;
    private LruCache<String, Bitmap> mMemoryCache;

    public ProdutoAdapter(Context context, List<Produto> items) {
        this.context = context;
        this.items = items;
        mPlaceHolderBitmap = decodeSampledBitmapFromResource(context.getResources(),
                R.drawable.placeholderpicture, 300, 300);
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

    public static Bitmap decodeSampledBitmapFromResource(ParseFile pf,
                                                         int reqWidth,
                                                         int reqHeight) throws ParseException {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeByteArray(pf.getData(), 0, pf.getData().length, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth,
                reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeByteArray(pf.getData(), 0, pf.getData().length, options);
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
        final ViewHolder holderPattern;

        LayoutInflater inflaterTemp = (LayoutInflater) context.getSystemService(Context
                .LAYOUT_INFLATER_SERVICE);
        final View viewContent = inflaterTemp.inflate(R.layout.list_element_produto, null);
        ImageView img = null;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context
                    .LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_element_produto, null);
            holderPattern = new ViewHolder();
            holderPattern.textViewSetCidade = (TextView) convertView.findViewById(R.id
                    .textViewSetCidade);
            holderPattern.textViewSetHoraPostagem = (TextView) convertView.findViewById(R.id
                    .textViewSetHoraPostagem);
            holderPattern.textViewSetNomeUsuario = (TextView) convertView.findViewById(R.id
                    .textViewSetNomeUsuario);
            holderPattern.textViewSetContato = (TextView) convertView.findViewById(R.id
                    .textViewSetContato);
            holderPattern.textViewSetPreco = (TextView) convertView.findViewById(R.id
                    .textViewSetPreco);
            holderPattern.textViewSetHashTags = (TextView) convertView.findViewById(R.id
                    .textViewSetHashTags);
            holderPattern.textViewSetLikes = (TextView) convertView.findViewById(R.id.viewLike);
            holderPattern.fotoProduto = (ImageView) convertView.findViewById(R.id.imageView);
            holderPattern.likeButton = (ImageButton) convertView.findViewById(R.id.likeButton);
            //holderPattern.likeFrame = (FrameAnimated) convertView.findViewById(R.id.layout_toast);
            convertView.setTag(holderPattern);
        } else {
            holderPattern = (ViewHolder) convertView.getTag();
        }


        //holderPattern.likeFrame.setVisibility(View.INVISIBLE);
        holderPattern.textViewSetCidade.setText("  em " + items.get(arg0).getCidade());
        holderPattern.textViewSetHoraPostagem.setText(formartaStringData(items.get(arg0)
                .getCreatedAt()));
        holderPattern.textViewSetNomeUsuario.setText(" Anunciante: " + items.get(arg0).getAuthor
                ().getUsername());
        holderPattern.textViewSetContato.setText(items.get(arg0).getPhoneNumber());
        holderPattern.textViewSetPreco.setText(items.get(arg0).getPrice());


        final Flag like = new Flag();
        like.quantidadeLikes = items.get(arg0).getAmoutOfLikes();
        holderPattern.textViewSetLikes.setText(like.quantidadeLikes + ((like.quantidadeLikes > 1)
                ? " recomendações" : " recomendação"));
        like.isLiked = items.get(arg0).isLikedByUser(ParseUser.getCurrentUser());

        final int arg = arg0;
        holderPattern.likeButton.setImageResource(like.isLiked ? R.drawable.like_enable : R
                .drawable.like_disable);

        holderPattern.likeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                items.get(arg).likeProduto(ParseUser.getCurrentUser(), like.isLiked);
                holderPattern.likeButton.setImageResource(like.isLiked ? R.drawable.like_disable
                        : R.drawable.like_enable);

                like.quantidadeLikes = like.isLiked ? --like.quantidadeLikes : ++like
                        .quantidadeLikes;
                Log.d("Quantidade likes: ", "" + like.quantidadeLikes);
                holderPattern.textViewSetLikes.setText(like.quantidadeLikes + ((like
                        .quantidadeLikes > 1) ? " recomendações" : " recomendação"));

                if (!like.isLiked) {
                    CustomToast.makeText(holderPattern.fotoProduto.getContext(), "",
                            Toast.LENGTH_SHORT).show();
                }
                like.isLiked = !like.isLiked;

            }


        });

        holderPattern.fotoProduto.setOnClickListener(new DoubleClickListener() {
            @Override
            public void onDoubleClick(View v) {
                items.get(arg).likeProduto(ParseUser.getCurrentUser(), like.isLiked);
                holderPattern.likeButton.setImageResource(like.isLiked ? R.drawable.like_disable
                        : R.drawable.like_enable);

                like.quantidadeLikes = like.isLiked ? --like.quantidadeLikes : ++like
                        .quantidadeLikes;
                Log.d("Quantidade likes: ", "" + like.quantidadeLikes);
                holderPattern.textViewSetLikes.setText(like.quantidadeLikes + ((like
                        .quantidadeLikes > 1) ? " recomendações" : " recomendação"));
                if (!like.isLiked) {
                    //ViewGroup viewGroup = (ViewGroup) viewContent.findViewById(R.id.layout_toast);
                    CustomToast.makeText(holderPattern.fotoProduto.getContext(), "",
                            Toast.LENGTH_SHORT).show();
                   /* holderPattern.likeFrame.setVisibility(View.VISIBLE);
                    Runnable mRunnable;
                    Handler mHandler=new Handler();

                    mRunnable=new Runnable() {

                        @Override
                        public void run() {
                            // TODO Auto-generated method stub
                            holderPattern.likeFrame.setVisibility(View.INVISIBLE); //If you want
                            just hide the View. But it will retain space occupied by the View.
                            holderPattern.likeFrame.setVisibility(View.GONE); //This will remove
                            the View. and free s the space occupied by the View
                        }
                    };
                    mHandler.postDelayed(mRunnable,2*1000);*/
                }
                like.isLiked = !like.isLiked;

            }
        });


        try {

            String[] array = items.get(arg0).getArrayHashtags();
            if (array != null && context instanceof Feed) {
                if (array.length == 3) {
                    final String primeiro = array[0];
                    final String segundo = array[1];
                    final String terceiro = array[2];
                    String saida = primeiro + " " + segundo + " " + terceiro;

                    SpannableString ss = new SpannableString(saida);
                    ClickableSpan clickableSpan1 = new ClickableSpan() {
                        @Override
                        public void onClick(View widget) {
                            ((Feed) context).changeAct(primeiro);
                        }
                    };
                    ClickableSpan clickableSpan2 = new ClickableSpan() {
                        @Override
                        public void onClick(View widget) {
                            ((Feed) context).changeAct(segundo);

                        }
                    };
                    final ClickableSpan clickableSpan3 = new ClickableSpan() {
                        @Override
                        public void onClick(View widget) {
                            ((Feed) context).changeAct(terceiro);
                        }
                    };

                    ss.setSpan(clickableSpan1, 0, primeiro.length(),
                            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    ss.setSpan(clickableSpan2, primeiro.length() + 1,
                            primeiro.length() + segundo.length() + 1,
                            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    ss.setSpan(clickableSpan3, primeiro.length() + segundo.length() + 2,
                            saida.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

                    holderPattern.textViewSetHashTags.setText(ss);
                    holderPattern.textViewSetHashTags.setMovementMethod(LinkMovementMethod
                            .getInstance());
                } else if (array.length == 2) {
                    final String primeiro = array[0];
                    final String segundo = array[1];
                    final String saida = primeiro + " " + segundo;

                    SpannableString ss = new SpannableString(saida);
                    ClickableSpan clickableSpan1 = new ClickableSpan() {
                        @Override
                        public void onClick(View widget) {
                            ((Feed) context).changeAct(primeiro);
                        }
                    };
                    ClickableSpan clickableSpan2 = new ClickableSpan() {
                        @Override
                        public void onClick(View widget) {
                            ((Feed) context).changeAct(segundo);

                        }
                    };

                    ss.setSpan(clickableSpan1, 0, primeiro.length(),
                            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    ss.setSpan(clickableSpan2, primeiro.length() + 1,
                            primeiro.length() + segundo.length() + 1,
                            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

                    holderPattern.textViewSetHashTags.setText(ss);
                    holderPattern.textViewSetHashTags.setMovementMethod(LinkMovementMethod
                            .getInstance());
                } else if (array.length == 1) {
                    final String saida = array[0];


                    SpannableString ss = new SpannableString(saida);
                    ClickableSpan clickableSpan1 = new ClickableSpan() {
                        @Override
                        public void onClick(View widget) {
                            ((Feed) context).changeAct(saida);
                        }
                    };

                    ss.setSpan(clickableSpan1, 0, saida.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

                    holderPattern.textViewSetHashTags.setText(ss);
                    holderPattern.textViewSetHashTags.setMovementMethod(LinkMovementMethod
                            .getInstance());
                }
            } else {
                if (array.length == 3) {
                    final String primeiro = array[0];
                    final String segundo = array[1];
                    final String terceiro = array[2];
                    String saida = primeiro + " " + segundo + " " + terceiro;

                    holderPattern.textViewSetHashTags.setText(saida);
                    holderPattern.textViewSetHashTags.setMovementMethod(LinkMovementMethod
                            .getInstance());
                } else if (array.length == 2) {
                    final String primeiro = array[0];
                    final String segundo = array[1];
                    final String saida = primeiro + " " + segundo;

                    holderPattern.textViewSetHashTags.setText(saida);
                    holderPattern.textViewSetHashTags.setMovementMethod(LinkMovementMethod
                            .getInstance());
                } else if (array.length == 1) {
                    final String saida = array[0];


                    holderPattern.textViewSetHashTags.setText(saida);
                    holderPattern.textViewSetHashTags.setMovementMethod(LinkMovementMethod
                            .getInstance());
                }
            }
        } catch (Exception e) {
            System.err.println(e.toString());
        }
        ParseFile pf = items.get(arg0).getPhotoFile();
        loadBitmap(pf, holderPattern.fotoProduto);
        return convertView;
    }

    private String formartaStringData(Date data) {
        String minutoAdicionadoComZero = "";
        if (data.getMinutes() < 10) {
            minutoAdicionadoComZero = "0" + data.getMinutes();
        } else {
            minutoAdicionadoComZero = "" + data.getMinutes();
        }

        return "" + data.getDate() + "/" + (data.getMonth() + 1) + " às " + data.getHours() + ":" +
                minutoAdicionadoComZero + "h";
    }

    public void loadBitmap(ParseFile pf, ImageView imageView) {
        if (cancelPotentialWork(pf.hashCode(), imageView)) {
            final BitmapWorkerTask task = new BitmapWorkerTask(imageView);
            final AsyncDrawable asyncDrawable =
                    new AsyncDrawable(context.getResources(), mPlaceHolderBitmap, task);
            imageView.setImageDrawable(asyncDrawable);
            task.execute(pf);
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

    static class Flag {
        public boolean isLiked;
        public int quantidadeLikes = 0;
    }

    static class ViewHolder {
        TextView textViewSetCidade;
        TextView textViewSetHoraPostagem;
        TextView textViewSetNomeUsuario;
        TextView textViewSetPreco;
        TextView textViewSetContato;
        TextView textViewSetHashTags;
        TextView textViewSetLikes;
        ImageView fotoProduto;
        ImageButton likeButton;
        LinearLayout likeFrame;
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

    class BitmapWorkerTask extends AsyncTask<ParseFile, Void, Bitmap> {
        private final WeakReference<ImageView> imageViewReference;
        public int data = 0;

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
            if (getBitmapFromMemCache(String.valueOf(params[0])) != null) {
                return getBitmapFromMemCache(String.valueOf(params[0]));
            }
            try {
                //bitmap  = BitmapFactory.decodeByteArray(params[0].getData(), 0,
                // params[0].getData().length);
                bitmap = decodeSampledBitmapFromResource(params[0], 650, 650);
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
}