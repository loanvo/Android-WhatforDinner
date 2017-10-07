package cs175.whatsfordinner;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static android.provider.AlarmClock.EXTRA_MESSAGE;

public class NewDishScreen extends AppCompatActivity {

    protected DBHelper dbHelper;
    private List<Recipe> recipe;

    private MyAdapter adapter;

    private String recipeName="";
    private String ingredients="";
    private String direct ="";
    private String wholeRecipe="";

    private EditText name;
    private EditText item;
    private EditText direction;
    private ImageView image;
    Uri defaultImage = Uri.parse("android.resource://cs175.whatsfordinner.res.drawable.default_image");

    List<String> mylist = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_dish_screen);

        dbHelper = new DBHelper(this);
    }

    /*@Override
    protected void onResume() {
        super.onResume();

        recipe = dbHelper.getRecipe();
        adapter = new MyAdapter(this, R.layout.activity_recipes__screen, recipe);
        ListView listRecipes = (ListView) findViewById(R.id.listview);
    }*/
    public void saveRecipe(){
        name = (EditText) findViewById(R.id.recipe_name);
        item = (EditText) findViewById(R.id.item1);
        direction = (EditText) findViewById(R.id.direction);
        image = (ImageView) findViewById(R.id.default_image);

        String n = name.getText().toString();
        String i = item.getText().toString();
        String d = direction.getText().toString();

        if(n.isEmpty()){
            Toast.makeText(getApplicationContext(), "a recipe name must be entered", Toast.LENGTH_SHORT).show();
        }else{
            if(!recipeExists(n)){
                Recipe recipe = new Recipe();
                recipe.setName(n);
                recipe.setItems(i);
                recipe.setDirection(d);
                recipe.setImage(defaultImage);
                dbHelper.createRecipe(recipe);

                recipeName = n;
                ingredients = i;
                direct = d;

                recipe.setName("");
                recipe.setItems("");
                recipe.setDirection("");

             //   adapter.add(recipe);

                //adapter.notifyDataSetChanged();

            }else{
                Toast.makeText(getApplicationContext(), "This recipe already exists", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void submitRecipe(View view){
        saveRecipe();
        Intent launhcRecipe = new Intent(this, Recipes_Screen.class);
        launhcRecipe.putExtra("recipename", recipeName);
        launhcRecipe.putExtra("ingredients", ingredients);
        launhcRecipe.putExtra("direction", direct);
        startActivity(launhcRecipe);
    }
    public List<String> addName(String n){
        n = name.getText().toString();
        if(!recipeExists(n))
            mylist.add(n);
        return mylist;
    }

    public List<String> getList(){
        return mylist;
    }
    public boolean recipeExists(String name){
        mylist = dbHelper.getAllRecipeName();
        int recipeCount = mylist.size();
        for(int i =0; i<recipeCount; i++){
            if(name.compareToIgnoreCase(mylist.get(i))==0)
                return true;
        }
        return false;
    }
    private class MyAdapter extends ArrayAdapter<Recipe>{
        Context context;
        List<Recipe> recipeList = new ArrayList<Recipe>();

        public MyAdapter(Context c, int rId, List<Recipe> objects){
            super(c, rId, objects);
            recipeList = objects;
            context = c;
        }

        @Override
        public View getView(int position, View view, ViewGroup parent) {
            if(view == null){
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = inflater.inflate(R.layout.recipe_detail, parent, false);

            };
            Recipe currentRecipe = recipe.get(position);
            return view;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

       // if(id == R.id.action_settings){
        //    return true;
        //}

        return super.onOptionsItemSelected(item);
    }
}
