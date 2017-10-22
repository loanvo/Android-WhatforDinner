package cs175.whatsfordinner;

import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.EmptyStackException;
import java.util.List;

public class RecipeScreenActivity extends AppCompatActivity implements MyFragment.OnItemSelectedListener{

    protected DBHelper dbHelper;
    private List<String> mylist = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_screen);

        dbHelper = new DBHelper(this);
        mylist = dbHelper.getAllRecipeName();

        //get ingredients for chosen recipe


        MyFragment ListFrag = (MyFragment) getFragmentManager().findFragmentById(R.id.fragment1);
        DetailFragment detailFragment = (DetailFragment) getFragmentManager().findFragmentById(R.id.fragment2);

        if (ListFrag != null) {
            ListFrag.updateList(mylist);
        }
    }

    @Override
    public void OnRecipeSelected(String recipeName) {
        DetailFragment detail = (DetailFragment) getFragmentManager().findFragmentById(R.id.fragment2);
        Recipe recipeInfo = dbHelper.getRecipeByName(recipeName);

        // TODO:
        Uri defaultImage = Uri.parse("android.resource://cs175.whatsfordinner/"+R.drawable.default_image);
        recipeInfo.setImage(defaultImage);

        if(detail != null && detail.isInLayout()){
            // Landscape
            detail.updateDetail(recipeInfo);
        }else{
            // Portrait
            if(recipeInfo == null){
                throw new EmptyStackException();
            }else{
                String[] units = { "lb",  "pcs",  "oz",  "tsp",  "ml", "cup" };
                List<String> items = recipeInfo.getItems();
                String itemString = "";
                for(int i =0; i<items.size(); i++){
                    String[] parts = items.get(i).toString().split("~");
                    if((parts.length<3) || (parts[0].equals(""))) continue;
                    itemString += "* " + parts[0] +
                            " " + parts[1] +
                            " " + units[Integer.parseInt(parts[2])] +
                            "\n";
                }

                String direction = recipeInfo.getDirection();
                Uri image = recipeInfo.getImage();

                Intent intent = new Intent(this, Recipes_Screen.class);
                intent.putExtra("recipename", recipeName);
                intent.putExtra("ingredients", itemString);
                intent.putExtra("direction", direction);
                intent.putExtra("image", image.toString());
                //intent.putExtra("image", "");
                startActivity(intent);
            }
        }
    }
}
