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
import android.text.TextUtils;
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
    private List<String> ingredients=new ArrayList<String>();
    private String direct ="";

    private boolean  EditMode;
    private EditText name;
    private EditText item1;
    private EditText item2;
    private EditText item3;
    private EditText item4;
    private EditText item5;
    private EditText item6;
    private EditText item7;
    private EditText item8;
    private EditText item9;
    private EditText item10;

    private EditText direction;
    private ImageView image;
    Uri defaultImage = Uri.parse("android.resource://cs175.whatsfordinner.res.drawable.default_image");

    List<String> mylist = new ArrayList<String>();

    public static final int PICK_IMAGE = 1;
    Boolean newEntry = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_dish_screen);

        dbHelper = new DBHelper(this);

        // If data is passed in, it is in edit mode
        Intent intent = getIntent();
        String recipename = intent.getStringExtra("recipename");
        EditMode = false;
        if (!recipename.isEmpty()) {
            EditMode = true;
            // Fill in textboxes with current info
            name = (EditText) findViewById(R.id.recipe_name);
            name.setText(recipename);

            item1 = (EditText) findViewById(R.id.item1);
            String item = intent.getStringExtra("ingredients1");
            item1.setText(item);
            item2 = (EditText) findViewById(R.id.item2);
            item = intent.getStringExtra("ingredients2");
            item2.setText(item);
            item3 = (EditText) findViewById(R.id.item3);
            item = intent.getStringExtra("ingredients3");
            item3.setText(item);
            item4 = (EditText) findViewById(R.id.item4);
            item = intent.getStringExtra("ingredients4");
            item4.setText(item);
            item5 = (EditText) findViewById(R.id.item5);
            item = intent.getStringExtra("ingredients5");
            item5.setText(item);
            item6 = (EditText) findViewById(R.id.item6);
            item = intent.getStringExtra("ingredients6");
            item6.setText(item);
            item7 = (EditText) findViewById(R.id.item7);
            item = intent.getStringExtra("ingredients7");
            item7.setText(item);
            item8 = (EditText) findViewById(R.id.item8);
            item = intent.getStringExtra("ingredients8");
            item8.setText(item);
            item9 = (EditText) findViewById(R.id.item9);
            item = intent.getStringExtra("ingredients9");
            item9.setText(item);
            item10 = (EditText) findViewById(R.id.item10);
            item = intent.getStringExtra("ingredients10");
            item10.setText(item);

            direction = (EditText) findViewById(R.id.direction);
            String dir = intent.getStringExtra("direction");
            direction.setText(dir);
        }

        //get Image for the new recipe
        Button imageButton=(Button)findViewById(R.id.button);
        imageButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
            }
        });
        RelativeLayout myLayout = (RelativeLayout) findViewById( R.id.mylayout );

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK) {
            if(requestCode==PICK_IMAGE){
                defaultImage = data.getData();
                image = (ImageView) findViewById(R.id.default_image);
                image.setImageURI(data.getData());
                Log.d("NewDish", defaultImage.toString());
            }else{
                image.setImageURI(defaultImage);
            }

        }
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
        item1 = (EditText) findViewById(R.id.item1);
        item2 = (EditText) findViewById(R.id.item2);
        item3 = (EditText) findViewById(R.id.item3);
        item4 = (EditText) findViewById(R.id.item4);
        item5 = (EditText) findViewById(R.id.item5);
        item6 = (EditText) findViewById(R.id.item6);
        item7 = (EditText) findViewById(R.id.item7);
        item8 = (EditText) findViewById(R.id.item8);
        item9 = (EditText) findViewById(R.id.item9);
        item10 = (EditText) findViewById(R.id.item10);
        direction = (EditText) findViewById(R.id.direction);
        image = (ImageView) findViewById(R.id.default_image);



        String n = name.getText().toString();
        List<String> itemList = new ArrayList<String>();
        itemList.add(item1.getText().toString());
        itemList.add(item2.getText().toString());
        itemList.add(item3.getText().toString());
        itemList.add(item4.getText().toString());
        itemList.add(item5.getText().toString());
        itemList.add(item6.getText().toString());
        itemList.add(item7.getText().toString());
        itemList.add(item8.getText().toString());
        itemList.add(item9.getText().toString());
        itemList.add(item10.getText().toString());
        String d = direction.getText().toString();
        Uri igm = Uri.parse(image.toString());
        if(n.isEmpty()){
            Toast.makeText(getApplicationContext(), "a recipe name must be entered", Toast.LENGTH_SHORT).show();
        }else{
            if(!recipeExists(n)){
                Recipe recipe = new Recipe();
                recipe.setName(n);
                recipe.setItems(itemList);
                recipe.setDirection(d);
                recipe.setImage(defaultImage);

                if(EditMode) {
                    dbHelper.removeRecipe(n);
                }
                dbHelper.createRecipe(recipe);

                recipeName = n;
                ingredients = itemList;
                direct = d;

                recipe.setName("");
                recipe.setItems(new ArrayList<String>());
                recipe.setDirection("");
            }else{
                Toast.makeText(getApplicationContext(), "This recipe already exists", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void submitRecipe(View view){
        saveRecipe();

        String itemString = "";
        for(int i =0; i<ingredients.size(); i++){
            if (ingredients.get(i).isEmpty()) continue;
            itemString += "* " + ingredients.get(i) + "\n";
        }

        Intent launhcRecipe = new Intent(this, Recipes_Screen.class);
        launhcRecipe.putExtra("recipename", recipeName);
        launhcRecipe.putExtra("ingredients", itemString);
        launhcRecipe.putExtra("direction", direct);
        launhcRecipe.putExtra("image", defaultImage.toString());
        startActivity(launhcRecipe);
    }
    public List<String> addName(String n){
        n = name.getText().toString();
        if(!recipeExists(n))
            mylist.add(n);

        return mylist;
    }

    public boolean recipeExists(String name){
        if (EditMode) return false;
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
        return super.onOptionsItemSelected(item);
    }
}
