package com.boleiros.bazart.modelo;

import android.util.Log;

import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseUser;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
/**
 * Created by Filipe on 06/07/14.
 */
@ParseClassName("Produto")
public class Produto extends ParseObject {

    public Produto() {
    }

    public String getPrice() {
        return getString("price");
    }

    public void setPrice(String price) {
        put("price", price);
    }

    public String getPhoneNumber() {
        return getString("phone");
    }

    public void setPhoneNumber(String title) {
        put("phone", title);
    }

    public ParseUser getAuthor() {
        return getParseUser("author");
    }

    public void setAuthor(ParseUser user) {
        put("author", user);
    }

    public String[] getArrayHashtags() {
        Object[] obj = getList("tags").toArray();
        String[] array = Arrays.copyOf(obj, obj.length, String[].class);
        System.out.println("tamanho" + array.length);
        return array;
    }

    public void setArrayHashtags(String[] arrayHashtags) {
        addAllUnique("tags", Arrays.asList(arrayHashtags));
    }

    public ParseGeoPoint getLocation() {
        return getParseGeoPoint("location");
    }

    public void setLocation(ParseGeoPoint value) {
        put("location", value);
    }


    public void setCity(String cidade) {
        put("city", cidade);
    }

    public String getCidade() {
        return getString("city");
    }


    public int getRating() {
        return getInt("rating");
    }

    public void setRating(int rating) {
        put("rating", rating);
    }

    public ParseFile getPhotoFile() {
        return getParseFile("photo");
    }

    public void setPhotoFile(ParseFile file) {
        put("photo", file);
    }

    public JSONArray getHashTags() {
        return getJSONArray("hashtag");
    }

    public void setHashTags(JSONArray array) {
        put("hashtag", array);
    }

    public void likeProduto(ParseUser user){

        Object[] obj = getList("likes").toArray();

        if(obj == null || obj.length != 0){
            Object[] tempArray = new Object[obj.length + 1];
            tempArray[tempArray.length - 1] = user.getObjectId();
            obj = tempArray;
            Log.d("Debug: ", "Não vem null, apenas vazio.");
        }else{
            obj = new Object[1];
            obj[0] = user.getObjectId();
        }


        String[] array = Arrays.copyOf(obj, obj.length, String[].class);
        System.out.println("Size of obj: " + obj.length);
        addAllUnique("likes", Arrays.asList(array));
        try {
            save();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public int getAmoutOfLikes(){
        List<Object> obj = getList("likes");
        int result = (obj == null)? 0 : obj.size();
        return result;
    }


}