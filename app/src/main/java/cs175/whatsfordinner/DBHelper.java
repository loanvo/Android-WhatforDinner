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
import java.util.List;

/**
 * Created by loanvo on 10/5/17.
 */
public class DBHelper extends SQLiteOpenHelper{
    private static final int DATABASE_VERSION = 2;
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

    /*public void createRecipe(Recipe recipe){
        SQLiteDatabase db = getWritableDatabase();

        String insert = "INSERT or replace INTO " + TABLE_NAME + "("
                + KEY_NAME + ", " + KEY_ITEMS + ", " + KEY_DIRECTION + ", "
                + KEY_IMAGEURI + ") "
                + "VALUES( '" + recipe.getName()
                + "','" + recipe.getItems() + "', '" + recipe.getDirection()
                + "', '" + recipe.getImage() + "')";
        db.execSQL(insert);
        db.close();
    }

    public Recipe getRecipe(int id){
        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.query(TABLE_NAME, new String[]{
                KEY_ID, KEY_NAME, KEY_ITEMS, KEY_DIRECTION, KEY_IMAGEURI},
                KEY_ID + "=?", new String[]{String.valueOf(id)}, null, null, null, null);
        if(cursor != null){
            cursor.moveToFirst();
        }

        Recipe recipe = new Recipe(Integer.parseInt(
                cursor.getString(0)),
                cursor.getString(1),
                cursor.getString(2),
                cursor.getString(3),
                Uri.parse(cursor.getString(4)));
        db.close();
        cursor.close();
        return recipe;
    }

    public void deleteRecipe(Recipe recipe){
        SQLiteDatabase db = getWritableDatabase();

        db.delete(TABLE_NAME, KEY_ID + "=?", new String[]{String.valueOf(recipe.getID())});
        db.close();
    }

    public int getRecipeCount(){
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
        int count = cursor.getCount();
        db.close();
        cursor.close();

        return count;
    }
    public int updateRecipe(Recipe recipe){
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, recipe.getName());
        values.put(KEY_ITEMS, recipe.getItems());
        values.put(KEY_DIRECTION, recipe.getDirection());
        values.put(KEY_IMAGEURI, recipe.getImage().toString());

        int rowAffected =db.update(
                TABLE_NAME,
                values,
                KEY_ID + "=?", new String[]{String.valueOf(recipe.getID())}
        );
        db.close();
        return rowAffected;
    }

    public List<Recipe> getAllRecipes(){
        List<Recipe> allRecipes = new ArrayList<Recipe>();

        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor =db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
        if(cursor.moveToFirst()){
            do{
                allRecipes.add(new Recipe(Integer.parseInt(
                        cursor.getString(0)),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3),Uri.parse(cursor.getString(4))));

            }
            while (cursor.moveToNext());

        }
        cursor.close();
        db.close();
        return allRecipes;
    }

    */

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
    public List<Recipe> getRecipe(){
        List<Recipe> recipesList = new ArrayList<Recipe>();

        String selectQuery = "SELECT * FROM " + TABLE_NAME;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if(cursor.moveToFirst()){
            do {
                Recipe recipe = new Recipe();
                recipe.setId (cursor.getInt(0));
                recipe.setItems(cursor.getString(2).toString());
                recipe.setDirection(cursor.getString(3));
                recipe.setImage(Uri.parse(cursor.getString(4)));
            } while(cursor.moveToNext());
        }
        return recipesList;
    }

    public Recipe getRecipeByName(String name){
        Recipe recipeInfo = new Recipe();

        String selectQuery = "SELECT * FROM " + TABLE_NAME + " WHERE " + KEY_NAME + " = " + "'"+ name +"'";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if(cursor.moveToFirst()){
            recipeInfo.setName (cursor.getString(1));
            recipeInfo.setItems (cursor.getString(2).toString());
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
        values.put(KEY_ITEMS, recipe.getItems());
        values.put(KEY_DIRECTION, recipe.getDirection());
        values.put(KEY_IMAGEURI, recipe.getImage().toString());

        db.update(TABLE_NAME, values, KEY_ID + "=?", new String[]{String.valueOf(recipe.getID())});

        db.close();
    }
}

