package com.boleiros.bazart.profile;

import android.app.ActionBar;
import android.app.Activity;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.boleiros.bazart.R;
import com.boleiros.bazart.feed.ProdutoAdapter;
import com.boleiros.bazart.modelo.Produto;
import com.boleiros.bazart.util.ActivityStore;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;
import java.util.Random;

public class ProdutoClicado extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_produto_clicado);
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.produto_clicado, menu);
        Bundle bundle = getIntent().getExtras();
        Intent intent = getIntent();
        String action = intent.getAction();
        Uri data = intent.getData();
        String idProduto;
        if(data!= null){
            String aux = data.getPath();
            idProduto = aux.substring(1, aux.length());
        }else {
            idProduto = bundle.getString("idProduto");
        }
        consultaAoParse(idProduto);
        return true;
    }

    public void showPhoneOptions(String phone){
        FragmentManager fm = getFragmentManager();
        DialogPhoneOptionsProfile dialogGrid = new DialogPhoneOptionsProfile();
        Bundle bundle = new Bundle();
        bundle.putString("phone", phone);
        dialogGrid.setArguments(bundle);
        dialogGrid.show(fm, "fragment_dialog_phone_options_profile");
    }


    public void povoarLayout(Produto produto){
        ParseUser autor = produto.getAuthor();

        ImageView imgProduto = (ImageView)findViewById(R.id.imageViewProdutoVendido);
       // ImageView imgPerfil = (ImageView)findViewById(R.id.profilePicPerfilUnico);

 //       TextView nomeAutor = (TextView) findViewById(R.id.nomeDoPerfilUnico);
        TextView precoProduto = (TextView)findViewById(R.id.textviewVendidoPor);
        TextView contatoProduto = (TextView)findViewById(R.id.textViewContatoVendido);
        TextView hashtagsProduto = (TextView)findViewById(R.id.textViewHashtagsVendido);
//        try {
//            byte[] byteArray = autor.getParseFile("profilePic").getData();
//            Bitmap bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
//            imgPerfil.setImageBitmap(bitmap);
//        } catch (Exception e) {
//            e.printStackTrace();
//            imgPerfil.setImageResource(R.drawable.ic_launcher);
//        }
        precoProduto.setText(produto.getPrice());
//        nomeAutor.setText(autor.getString("name"));
        try {
            byte[] fotoProduto = produto.getPhotoFile().getData();
            imgProduto.setImageBitmap(BitmapFactory.decodeByteArray(fotoProduto,0,fotoProduto.length));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String[] hashtagsArray = produto.getArrayHashtags();
        String hashtags = "";
        for (int i = 0; i < hashtagsArray.length; i++) {
            hashtags += hashtagsArray[i] + " ";
        }
        hashtagsProduto.setText(hashtags);

        final String contato = produto.getPhoneNumber();
        SpannableString span = new SpannableString(contato);
        ClickableSpan clickPhone = new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                showPhoneOptions(contato);
            }
        };
        span.setSpan(clickPhone,0,span.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        contatoProduto.setText(span);
        contatoProduto.setMovementMethod(LinkMovementMethod.getInstance());
    }

    public void consultaAoParse(String idProduto) {
        int max = ActivityStore.getInstance(this).getFrases().size();
        Random r = new Random();
        int numeroSorteado = r.nextInt(max);
        ParseQuery<Produto> query = ParseQuery.getQuery("Produto");
        query.include("author");

        query.whereEqualTo("objectId", idProduto);
        query.orderByDescending("createdAt");
        query.findInBackground(new FindCallback<Produto>() {
            @Override
            public void done(List<Produto> parseObjects, com.parse.ParseException e) {
                povoarLayout(parseObjects.get(0));
            }
        });
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        if(id == android.R.id.home){
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}
