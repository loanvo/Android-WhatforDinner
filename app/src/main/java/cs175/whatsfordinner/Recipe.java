package cs175.whatsfordinner;

import android.net.Uri;

import java.util.List;

/**
 * Created by loanvo on 10/3/17.
 */

public class Recipe {
    private int _id;
    private String name;
    private List<String> ingredients;
    private String direction;
    private Uri image;
    private int selected;

    public Recipe(){

    }
   /* public Recipe(int id, String n, String ingre, String dir, Uri imageUri){
        _id = id;
        name = n;
        ingredients = ingre;
        direction = dir;
        image = imageUri;
    }*/
    public int getID(){
        return _id;
    }

    public String getName(){
        return name;
    }

    public List<String> getItems(){
        return ingredients;
    }

    public String getDirection(){
        return direction;
    }

    public Uri getImage(){
        return image;
    }
    public int getSelectedRecipe(){
        return selected;
    }
    public void setId(int id){
        _id = id;
    }

    public void setName(String n){
        name = n;
    }

    public void setImage(Uri igm){
        image = igm;
    }

    public void setItems(List<String> i){
        ingredients = i;
    }

    public void setDirection(String d){
        direction = d;
    }
    public void setSelectedRecipe(int s){
        selected=s;
    }

}
