package com.boleiros.bazart.profile;

import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;

import com.boleiros.bazart.R;
import com.boleiros.bazart.modelo.Produto;
import com.parse.DeleteCallback;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.RefreshCallback;
import com.parse.SaveCallback;

import java.util.List;

/**
 * Created by diego on 8/4/14.
 */
public class DialogGrid extends DialogFragment {
    public DialogGrid() {
        // Empty constructor required for DialogFragment
    }
    public void removeDoParse(final String id) {
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
                                    myObjectWasDeletedSuccessfully();
                                }
                            }
                        });
                }
            }
        });
    }

    public void marcarVendido(final String id){
        ParseQuery<Produto> query = ParseQuery.getQuery("Produto");

        query.include("author");
        query.whereEqualTo("objectId", id);
        query.findInBackground(new FindCallback<Produto>() {
            @Override
            public void done(List<Produto> parseObjects, ParseException e) {
                if (e == null) {
                    Produto produto = parseObjects.get(0);
                    produto.setVendido(Boolean.TRUE);
                    produto.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            produtoMarcadoComSucesso();
                        }
                    });
                }
            }
        });
    }
    public void produtoMarcadoComSucesso(){
        Toast.makeText(getActivity(),"Produto marcado como vendido",Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getActivity(),ProfileActivity.class);
        startActivity(intent);
    }
    public void myObjectWasDeletedSuccessfully(){
        Toast.makeText(getActivity(),"Produto removido",Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getActivity(),ProfileActivity.class);
        startActivity(intent);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_grid_item, container);
        getDialog().setTitle("Opções");

        Button remover = (Button)view.findViewById(R.id.buttonRemover);
        remover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeDoParse(getArguments().getString("id"));

            }
        });

        Button vendido = (Button)view.findViewById(R.id.buttonMarcarVendido);
        vendido.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                marcarVendido(getArguments().getString("id"));
            }
        });
        return view;
    }
}
