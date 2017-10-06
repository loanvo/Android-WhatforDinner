package cs175.whatsfordinner;

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
import android.view.ContextMenu;
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
    EditText editText;
    Button imageButton;
    RelativeLayout myLayout;

    DBHelper dbHelper;
    ArrayAdapter<Recipe> arrayAdapter;

    List<Recipe> RecipeArrayList = new ArrayList<Recipe>();

    Button saveRecipe;
    ImageView inputImageId;
    EditText inputRecipeName;
    EditText inputItems;
    EditText inputDirection;
    Drawable noRecipeImage;
    Uri defaultImage = Uri.parse("android.resource://cs175.whatsfordinner.res.drawable.default_image");

    Boolean newEntry = true;

    ListView recipesListView;
    ImageView listViewPhoto;
    TextView listViewName;
    TextView listViewItems;
    TextView listViewDirection;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_dish_screen);
        imageButton=(Button)findViewById(R.id.button);

        imageButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Uri uri = Uri.parse("http://www.google.com"); // missing 'http://' will cause crashed
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);

            }


        });
        myLayout = (RelativeLayout) findViewById( R.id.mylayout );

        dbHelper = new DBHelper(getApplicationContext());
        saveRecipe = (Button) findViewById(R.id.button2);
        inputRecipeName = (EditText) findViewById(R.id.recipe_name);
        inputItems = (EditText) findViewById(R.id.item1);
        inputDirection = (EditText) findViewById(R.id.direction);
        inputImageId = (ImageView) findViewById(R.id.default_image);
        noRecipeImage = inputImageId.getDrawable();
        recipesListView = (ListView) findViewById(R.id.listview);
        inputRecipeName.addTextChangedListener(new TextWatcher(){
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                saveRecipe.setEnabled(String.valueOf(inputRecipeName.getText()).trim().length()>0);

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        //inputImageId.setOnClickListener(getPhotoFromGallery);
        saveRecipe.setOnClickListener(recordRecipe);

        if(dbHelper.getRecipeCount() != 0)
            RecipeArrayList.addAll(dbHelper.getAllRecipes());
        populateList();

    }

    public final View.OnClickListener recordRecipe = new View.OnClickListener(){
        public void onClick(View view){
            Recipe recipe = new Recipe(dbHelper.getRecipeCount(),
                    String.valueOf(inputRecipeName.getText().toString()),
                    String.valueOf(inputItems.getText().toString()),
                    String.valueOf(inputDirection.getText().toString()), defaultImage);
            if(!recipeExists(recipe)) {
                dbHelper.createRecipe(recipe);
                RecipeArrayList.add(recipe);
                arrayAdapter.notifyDataSetChanged();
                Toast.makeText(getApplicationContext(), inputRecipeName.getText().toString() +
                " has been added. ",
                        Toast.LENGTH_SHORT).show();
                newEntry =true;
                onResume();
                return;
            }
            Toast.makeText(getApplicationContext(), String.valueOf(inputRecipeName.getText()) + "" +
                    " has already been added. " + "Use another name. ", Toast.LENGTH_LONG).show();
        }

    };
    public void createRecipeMenu(ContextMenu menu, View view, ContextMenu.ContextMenuInfo menuInfo){
        super.onCreateContextMenu(menu, view, menuInfo);

        menu.setHeaderIcon(R.drawable.recipes);
        menu.setHeaderTitle("Recipes");
        menu.add(Menu.NONE, 1, Menu.NONE, "Delete Recipe");

    }

    public boolean recipeExists(Recipe recipe){
        String first = recipe.getName();
        int recipeCount = RecipeArrayList.size();
        for(int i =0; i<recipeCount; i++){
            if(first.compareToIgnoreCase(RecipeArrayList.get(i).getName())==0)
                return true;
        }
        return false;
    }

    public void populateList(){
        arrayAdapter = new RecipeListAdapter();
        recipesListView.setAdapter(arrayAdapter);
    }

    private class RecipeListAdapter extends ArrayAdapter<Recipe>{
        public RecipeListAdapter(){
            super(getApplicationContext(), R.layout.activity_recipes__screen, RecipeArrayList);
        }

        @NonNull
        @Override
        public View getView(int position, View view, ViewGroup parent) {
            if(view == null)
                view = getLayoutInflater().inflate(R.layout.activity_recipes__screen, parent, false);
            Recipe currentRecipe = RecipeArrayList.get(position);
            listViewName = (TextView) view.findViewById(R.id.recipe_name);
            listViewItems = (TextView) view.findViewById(R.id.item1);
            listViewDirection = (TextView) view.findViewById(R.id.direction);
            listViewPhoto = (ImageView) view.findViewById(R.id.default_image);

            listViewName.setText(currentRecipe.getName());
            listViewItems.setText(currentRecipe.getItems());
            listViewDirection.setText(currentRecipe.getDirection());
            listViewPhoto.setImageURI(currentRecipe.getImage());

            return view;
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        if(newEntry){
            inputRecipeName.setText("");
            inputItems.setText("");
            inputDirection.setText("");
            inputImageId.setImageDrawable(noRecipeImage);
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
        /*if(id==R.id.action_settings){
            return true;
        }*/
        return super.onOptionsItemSelected(item);
    }

    /*public void saveRecipe(View view) {
        /*ArrayList<EditText> textList = new ArrayList<EditText>();
        for( int i = 0; i < myLayout.getChildCount(); i++ ) {
            if (myLayout.getChildAt(i) instanceof EditText)
                textList.add((EditText) myLayout.getChildAt(i));
        }
        Intent intent = new Intent(this, Recipes_Screen.class);
        EditText editText = (EditText) findViewById(R.id.recipe_name);
        String message = editText.getText().toString();
        intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);
    }*/


}
