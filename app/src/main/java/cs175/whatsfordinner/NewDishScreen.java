package cs175.whatsfordinner;

import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.IntegerRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.ContextMenu;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static android.provider.AlarmClock.EXTRA_MESSAGE;

public class NewDishScreen extends AppCompatActivity {

    protected DBHelper dbHelper;
    //private List<String> allIngredients = dbHelper.getAllIngredients();
    private List<Recipe> recipe;

    private String recipeName="";
    private List<String> ingredients=new ArrayList<String>();

    private String direct ="";

    private boolean  EditMode;
    private EditText name;

    private ListView itemList;

    private EditText direction;
    private ImageView image;
    Uri defaultImage = Uri.parse("android.resource://cs175.whatsfordinner/"+R.drawable.default_image);

    private Button submitBtn;
    private ListAdapter ingrAdapter;

    List<String> mylist = new ArrayList<String>();

    public static final int PICK_IMAGE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_dish_screen);

        dbHelper = new DBHelper(this);
       // List<String> ingredientsList = dbHelper.getAllIngredients();

        // If data is passed in, it is in edit mode
        Intent intent = getIntent();
        String recipename = intent.getStringExtra("recipename");

        //set up spinner for units


        // ArrayAdapter<String> unitAdapter= new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, units);
        //unitAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


        EditMode = false;
        if (recipename != null) {
            EditMode = true;

            // Fill in textboxes with current info
            name = (EditText) findViewById(R.id.recipe_name);
            name.setText(recipename);
            name.setEnabled(false);

            for(int i=0; i<10; i++) {
                ingredients.add(intent.getStringExtra("ingredients" + Integer.toString(i)));
            }

            direction = (EditText) findViewById(R.id.direction);
            String dir = intent.getStringExtra("direction");
            direction.setText(dir);

        } else { // not EditMode
            name = (EditText) findViewById(R.id.recipe_name);
            name.setEnabled(true);
            for(int i=0; i<10; i++){
                ingredients.add("");

            }
        }

        itemList = (ListView) findViewById(R.id.listView_item);

        ingrAdapter = new ListAdapter(this, android.R.layout.simple_list_item_1, ingredients);
        itemList.setAdapter(ingrAdapter);
        itemList.setTextFilterEnabled(true);

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

        name = (EditText) findViewById(R.id.recipe_name);

        if(EditMode == false){
            name.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View arg0, boolean arg1) {
                    List<String> nameList = dbHelper.getAllRecipeName();
                    name.setError(null);
                    submitBtn = (Button) findViewById(R.id.button2);
                    if(nameList.contains(name.getText().toString())){
                        name.setError("Recipe name already exists!");
                        submitBtn.setClickable(false);
                    }else if(name.getText().toString().isEmpty()){
                        name.setError("Please enter recipe name!");
                        submitBtn.setClickable(false);
                    } else{
                        name.setError(null);
                        submitBtn.setClickable(true);
                    }
                }
            });
        }
    }

    class ListAdapter extends ArrayAdapter<String> {
        private List<String> mData;
        private ArrayList<View> mView;

        public ListAdapter(Context context, int textViewResourceId) {
            super(context, textViewResourceId);
        }

        public ListAdapter(Context context, int resource, List<String> items) {
            super(context, resource, items);
            mData = new ArrayList<String>(items);
            mView = new ArrayList<View>();
            for(int i=0; i<10; i++) {
                mView.add(null);
            }
        }

        @Override
        public String getItem(int position) {
            itemList = (ListView) findViewById(R.id.listView_item);
            String p = "";

            return mData.get(position);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View v = convertView;

            if (v == null) {
                LayoutInflater vi;
                vi = LayoutInflater.from(getContext());
                v = vi.inflate(R.layout.item_row, null);
            }
            v.setTag(new Integer(position));

            String p = mData.get(position);
            if (p != null) {
                String[] parts = p.toString().split("~");

                AutoCompleteTextView textView = (AutoCompleteTextView) v.findViewById(R.id.itemView);
                if (textView != null) {
                    if (parts.length >= 1) {
                        textView.setText(parts[0]);
                    }
                }
                textView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View v, boolean hasFocus)  {
                        if(hasFocus) return;
                        updateRowData(v);
                    }
                });

                EditText quantityView = (EditText) v.findViewById(R.id.quantity_view);
                if (quantityView != null) {
                    if (parts.length >= 2) {
                        quantityView.setText(parts[1]);
                    } else {
                        quantityView.setText("1");
                    }
                }
                quantityView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View v, boolean hasFocus)  {
                        if(hasFocus) return;
                        updateRowData(v);
                    }
                });

                String[] units = { "lb",  "pcs",  "oz",  "tsp",  "ml", "cup" };
                Spinner spinner = (Spinner) v.findViewById(R.id.unit_spinner);
                if (spinner != null) {
                    ArrayAdapter<String> unitAdapter = new ArrayAdapter<String>(v.getContext(),
                            android.R.layout.simple_spinner_dropdown_item, units);
                    spinner.setAdapter(unitAdapter);
                    if (parts.length >= 3) {
                        spinner.setSelection(Integer.parseInt(parts[2]));
                    }
                }
                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {
                    }

                    public void onItemSelected(AdapterView<?> parent, View v, int pos, long id) {
                        //updateRowData(v);
                    }
                });
            }

            return v;
        }

        void updateRowData(View v) {
            LinearLayout row = (LinearLayout)v.getParent();
            AutoCompleteTextView textView = (AutoCompleteTextView) row.findViewById(R.id.itemView);
            EditText quantityView = (EditText) row.findViewById(R.id.quantity_view);
            Spinner spinner = (Spinner) row.findViewById(R.id.unit_spinner);
            Integer i = (Integer) row.getTag();
            mData.set(i, textView.getText() + "~" +
                    quantityView.getText() + "~" +
                    Integer.toString(spinner.getSelectedItemPosition()));
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK) {
            if(requestCode==PICK_IMAGE){
                /*try {
                    InputStream inputStream = getContentResolver().openInputStream(data.getData());
                    Drawable yourDrawable = Drawable.createFromStream(inputStream, data.getData().toString());
                    String a = yourDrawable.toString();
                    image.setImageDrawable(yourDrawable);
                } catch (FileNotFoundException e) {
                    //yourDrawable = getResources().getDrawable(R.drawable.default_image);
                }*/
                defaultImage = data.getData();
                image = (ImageView) findViewById(R.id.default_image);
                image.setImageURI(data.getData());
            }else{
                image.setImageURI(defaultImage);
            }

        }
    }

    public void saveRecipe(){
        name = (EditText) findViewById(R.id.recipe_name);

        direction = (EditText) findViewById(R.id.direction);
        image = (ImageView) findViewById(R.id.default_image);

        String n = name.getText().toString();
        List<String> itemList = new ArrayList<String>();

        ListView itemView = (ListView) findViewById(R.id.listView_item);
        for (int i=0; i<10; i++) {
            itemList.add(ingrAdapter.getItem(i));
        }

        String d = direction.getText().toString();
        Uri igm = Uri.parse(image.toString());
            if(!recipeExists(n)){
                Recipe recipe = new Recipe();
                recipe.setName(n);
                recipe.setItems(itemList);
                recipe.setDirection(d);

                //test
                //recipe.setImage(igm);

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
            }
    }

    public void submitRecipe(View view){
        saveRecipe();

        String[] units = { "lb",  "pcs",  "oz",  "tsp",  "ml", "cup" };
        List<String> items = ingredients;
        String itemString = "";
        for(int i =0; i<items.size(); i++){
            String[] parts = items.get(i).toString().split("~");
            if((parts.length<3) || (parts[0].equals(""))) continue;
            itemString += "* " + parts[0] +
                    " " + parts[1] +
                    " " + units[Integer.parseInt(parts[2])] +
                    "\n";
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
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        return super.onOptionsItemSelected(item);
    }

}
