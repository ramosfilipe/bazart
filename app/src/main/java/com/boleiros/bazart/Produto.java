package com.boleiros.bazart;
import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;

import org.json.JSONArray;

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
        put("price",price);
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
    public JSONArray getHashTags(){
        return getJSONArray("hashtag");
    }
    public void setHashTags(JSONArray array){
        put("hashtag",array);
    }

}