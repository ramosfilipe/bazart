package com.boleiros.bazart.modelo;

import android.util.Log;

import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseUser;

import org.json.JSONArray;

import java.util.Arrays;

/**
 * Created by Filipe on 06/07/14.
 */
@ParseClassName("Produto")
public class Produto extends ParseObject {

    public static final String LIKES = "likes";

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


    public void setAuthorStr(String authorStr) {
        put("authorStr", authorStr);
    }

    public String[] getArrayHashtags() {
        Object[] obj = getList("tags").toArray();
        String[] array = Arrays.copyOf(obj, obj.length, String[].class);
        return array;
    }

    public void setArrayHashtags(String[] arrayHashtags) {
        addAllUnique("tags", Arrays.asList(arrayHashtags));
    }

    public Boolean getVendido() {
        return getBoolean("isSold");
    }

    public void setVendido(Boolean bool) {
        put("isSold", bool);
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

    public void addLike() {
        put("numLikes", getLikes() + 1);
    }

    public void removeLike() {
        put("numLikes", getLikes() - 1);
    }

    public int getLikes() {
        return getInt("numLikes");
    }


    public void likeProduto(ParseUser user, boolean isLiked) {

        Object[] obj = getList(LIKES).toArray();
        int newSize = 0;
        for (int i = 0; i < obj.length; i++) {
            if (obj[i] != null) {
                newSize++;
            }
        }

        Object[] objNoNull = new Object[newSize];
        int cont = 0;

        for (int i = 0; i < obj.length; i++) {
            if (obj[i] != null) {
                objNoNull[cont] = obj[i];
                cont++;
            }

        }


        if (isLiked) {
            Object[] toDelete = new Object[1];
            for (int i = 0; i < obj.length; i++) {
                if (obj[i] != null && obj[i].toString().equals(user.getObjectId())) {
                    toDelete[0] = obj[i];
                }

            }
            removeLike();
            removeAll(LIKES, Arrays.asList(toDelete));

        } else {
            //obj != null ||
            if (obj.length != 0) {
                Object[] tempArray = new Object[obj.length + 1];
                tempArray[tempArray.length - 1] = user.getObjectId();
                obj = tempArray;
                Log.d("Debug: ", "Não vem null, apenas vazio.");
            } else {
                obj = new Object[1];
                obj[0] = user.getObjectId();
            }


            String[] array = Arrays.copyOf(obj, obj.length, String[].class);
            addLike();
            addAllUnique(LIKES, Arrays.asList(array));
        }


        try {
            save();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public int getAmoutOfLikes() {
//        List<Object> obj = getList(LIKES);
//        if (obj == null){
//            return 0;}
//        int result = 0;
//        for (int i = 0; i < obj.size(); i++) {
//            if (obj.get(i) != null){
//                result++;}
//        }
//
//
//        return result;
        return getLikes();
    }


    public boolean isLikedByUser(ParseUser user) {
        Object[] obj = getList(LIKES).toArray();
        boolean isLiked = false;
        for (int i = 0; i < obj.length; i++) {
            if (obj[i].toString().equals(user.getObjectId())) {
                isLiked = true;
                break;
            }
        }
        return isLiked;
    }

    public void initLikeArray() {
        String[] empty = new String[0];

        addAllUnique(LIKES, Arrays.asList(empty));
    }
}