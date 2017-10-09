package cs175.whatsfordinner;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.util.Log;

import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by loanvo on 10/5/17.
 */
public class DBHelper extends SQLiteOpenHelper{
    private static final int DATABASE_VERSION = 3;
    private static final String DATABASE_NAME = "Recipes";
    private static final String TABLE_NAME = "new_Recipes";

    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_ITEMS = "items";
    private static final String KEY_DIRECTION = "direction";
    private static final String KEY_IMAGEURI = "imageUri";

    private int recipeCount;

    public DBHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String querry = ("CREATE TABLE " + TABLE_NAME + "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                KEY_NAME + " TEXT," + KEY_ITEMS + " TEXT," + KEY_DIRECTION + " TEXT,"
                    + KEY_IMAGEURI + " TEXT)");
        Log.d("DBHelper-onCreate", querry);
        db.execSQL (querry);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int version1, int version2) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    //save new recipe to database
    public void createRecipe(Recipe recipe){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(KEY_NAME, recipe.getName());
        values.put(KEY_ITEMS, recipe.getItems().toString());
        values.put(KEY_DIRECTION, recipe.getDirection());
        values.put(KEY_IMAGEURI, recipe.getImage().toString());

        db.insert(TABLE_NAME, null, values);
        recipeCount ++;

        db.close();
    }

    //get all name of recipe in database
    public List<String> getAllRecipeName(){
        List<String> recipesNameList = new ArrayList<String>();

        String selectQuery = "SELECT " + KEY_NAME + " FROM " + TABLE_NAME;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if(cursor.moveToFirst()){
            do {
                Recipe recipe = new Recipe();
                recipe.setName (cursor.getString(0));
                recipesNameList.add(recipe.getName());
            } while(cursor.moveToNext());
        }
        return recipesNameList;
    }

    //get all ingredients from all recipes in database
    public List<String> getAllIngredients(){
        List<String> Ingredients = new ArrayList<String>();

        String selectQuery = "SELECT DISTINCT " + KEY_ITEMS + " FROM " + TABLE_NAME;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if(cursor.moveToFirst()){
            do {
                String itemString = cursor.getString(0);
                List<String> items = Arrays.asList(itemString.substring(1, itemString.length() - 1).replaceAll("\\s", "").split(","));
                Ingredients.addAll(items);
            } while(cursor.moveToNext());
        }
        return Ingredients;
    }

    // get recipe detail
    public List<Recipe> getRecipe(){
        List<Recipe> recipesList = new ArrayList<Recipe>();

        String selectQuery = "SELECT * FROM " + TABLE_NAME;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if(cursor.moveToFirst()){
            do {
                Recipe recipe = new Recipe();
                recipe.setId (cursor.getInt(0));
                String itemString = cursor.getString(2);
                List<String> items = Arrays.asList(itemString.substring(1, itemString.length() - 1).replaceAll("\\s", "").split(","));
                recipe.setItems (items);
                recipe.setDirection(cursor.getString(3));
                recipe.setImage(Uri.parse(cursor.getString(4)));
            } while(cursor.moveToNext());
        }
        return recipesList;
    }

    public void removeRecipe (String name) {
        String selectQuery = "DELETE FROM " + TABLE_NAME + " WHERE " + KEY_NAME + " = " + "'" + name + "'";
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(selectQuery);
    }

    public Recipe getRecipeByName(String name){
        Recipe recipeInfo = new Recipe();

        String selectQuery = "SELECT * FROM " + TABLE_NAME + " WHERE " + KEY_NAME + " = " + "'"+ name +"'";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if(cursor.moveToFirst()){
            recipeInfo.setName (cursor.getString(1));
            String itemString = cursor.getString(2);
            List<String> items = Arrays.asList(itemString.substring(1, itemString.length() - 1).replaceAll("\\s", "").split(","));
            recipeInfo.setItems (items);
            recipeInfo.setDirection (cursor.getString(3));
            recipeInfo.setImage(Uri.parse(cursor.getString(4)));
        }else{
            return null;
        }
        return recipeInfo;
    }

    public List<String> getIngredients(){
        List<String> ingredient_list = new ArrayList<String>();
        String selectQuery = "SELECT " + KEY_ITEMS + " FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if(cursor.moveToFirst()){
            do{
                ingredient_list.add(cursor.getString(2));
            }while(cursor.moveToNext());
        }else{
            return null;
        }
        return ingredient_list;
    }


    public void deleteAll(List<Recipe> list){
        list.clear();

        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, null, new String[]{});
        db.close();
    }

    public void updateRecipe(Recipe recipe){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(KEY_NAME, recipe.getName());
        //values.put(KEY_ITEMS, recipe.getItems());
        values.put(KEY_DIRECTION, recipe.getDirection());
        values.put(KEY_IMAGEURI, recipe.getImage().toString());

        db.update(TABLE_NAME, values, KEY_ID + "=?", new String[]{String.valueOf(recipe.getID())});

        db.close();
    }
}

