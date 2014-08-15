package com.boleiros.bazart.profile;

import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.boleiros.bazart.R;
import com.boleiros.bazart.modelo.Produto;
import com.boleiros.bazart.util.ActivityStore;
import com.parse.DeleteCallback;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.Random;

/**
 * Created by diego on 8/4/14.
 */
public class DialogGrid extends DialogFragment {
    public DialogGrid() {
        // Empty constructor required for DialogFragment
    }

    public static Bitmap mark(Bitmap src) {
        int w = src.getWidth();
        int h = src.getHeight();
        int size = 130;
        Point location = new Point(100, 60);
        Bitmap result = Bitmap.createBitmap(w, h, src.getConfig());

        Canvas canvas = new Canvas(result);
        canvas.drawBitmap(src, 0, 0, null);

        Paint paint = new Paint();
        paint.setColor(Color.RED);
        paint.setFakeBoldText(true);
//        paint.setAlpha(alpha);
        paint.setTextSize(size);

        paint.setAntiAlias(true);
        paint.setUnderlineText(false);
        canvas.rotate(45);
        canvas.drawText("V E N D I D O", location.x, location.y, paint);
        return result;
    }

    public void removeDoParse(final String id) {
        final ProgressDialog pDialog;
        int max = ActivityStore.getInstance(getActivity()).getFrases().size();
        Random r = new Random();
        int numeroSorteado = r.nextInt(max);
        pDialog = ProgressDialog.show(getActivity(), null,
                ActivityStore.getInstance(getActivity()).getFrases().get(numeroSorteado));

        ParseQuery<Produto> query = ParseQuery.getQuery("Produto");
        query.include("author");
        query.whereEqualTo("objectId", id);
        query.findInBackground(new FindCallback<Produto>() {
            @Override
            public void done(List<Produto> parseObjects, ParseException e) {
                if (e == null) {
                    parseObjects.get(0).deleteInBackground(new DeleteCallback() {
                        public void done(ParseException e) {
                            if (e == null) {
                                pDialog.dismiss();
                                myObjectWasDeletedSuccessfully();
                            } else {
                                produtoRemovidoComFalha();
                            }
                        }
                    });
                }
            }
        });
    }

    public void marcarVendido(final String id) {
        ParseQuery<Produto> query = ParseQuery.getQuery("Produto");

        query.include("author");
        query.whereEqualTo("objectId", id);
        query.findInBackground(new FindCallback<Produto>() {
            @Override
            public void done(List<Produto> parseObjects, ParseException e) {
                if (e == null) {
                    Produto produto = parseObjects.get(0);
                    produto.setVendido(Boolean.TRUE);
                    try {
                        byte[] arrayImage = produto.getPhotoFile().getData();
                        new BitmapWorker(produto).execute(arrayImage);

                    } catch (ParseException e1) {
                        e1.printStackTrace();
                    }

                }
            }
        });
    }

    public void produtoMarcadoComSucesso() {
        Toast.makeText(getActivity(), "Produto marcado como vendido", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getActivity(), ProfileActivity.class);
        startActivity(intent);
    }

    public void produtoMarcadoComFalha() {
        Toast.makeText(getActivity(), "Ops... Tente salvar novamente", Toast.LENGTH_SHORT).show();
    }

    public void produtoRemovidoComFalha() {
        Toast.makeText(getActivity(), "Ops... Tente remover novamente", Toast.LENGTH_SHORT).show();
    }

    public void myObjectWasDeletedSuccessfully() {
        Toast.makeText(getActivity(), "Produto removido", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getActivity(), ProfileActivity.class);
        startActivity(intent);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_grid_item, container);
        getDialog().setTitle("Opções");
        Button remover = (Button) view.findViewById(R.id.buttonRemover);
        Button vendido = (Button) view.findViewById(R.id.buttonMarcarVendido);

        if (getArguments().getBoolean("vendido")) {
            vendido.setEnabled(false);
        }

        remover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeDoParse(getArguments().getString("id"));

            }
        });

        vendido.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                marcarVendido(getArguments().getString("id"));
            }
        });
        return view;
    }

    class BitmapWorker extends AsyncTask<byte[], Void, byte[]> {
        private Produto produto;
        private ProgressDialog progressDialog;

        public BitmapWorker(Produto produto) {
            this.produto = produto;
        }

        @Override
        protected void onPreExecute() {
            int max = ActivityStore.getInstance(getActivity()).getFrases().size();
            Random r = new Random();
            int numeroSorteado = r.nextInt(max);
            progressDialog = ProgressDialog.show(getActivity(), null,
                    ActivityStore.getInstance(getActivity()).getFrases().get(numeroSorteado));

        }

        // Decode image in background.
        @Override
        protected byte[] doInBackground(byte[]... params) {
            Bitmap image = BitmapFactory.decodeByteArray(params[0], 0,
                    params[0].length);
            Bitmap imageMarked = mark(image);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            imageMarked.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            byte[] markedPhotoArray = bos.toByteArray();
            return markedPhotoArray;
        }

        @Override
        protected void onPostExecute(byte[] markedPhotoArray) {
            produto.setPhotoFile(new ParseFile("fotoProduto.jpg", markedPhotoArray));
            produto.saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    if (e == null) {
                        progressDialog.dismiss();
                        produtoMarcadoComSucesso();
                    } else {
                        progressDialog.dismiss();
                        produtoMarcadoComFalha();
                    }
                }
            });
        }
    }
}