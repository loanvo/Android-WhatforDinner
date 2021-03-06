package cs175.whatsfordinner;

import android.app.Fragment;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.provider.CalendarContract;
import android.support.annotation.IntegerRes;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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
        if (ListFrag != null) {
            ListFrag.updateList(mylist);
        }
    }

    @Override
    public void OnRecipeSelected(View view,String recipeName, int count) {

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

            //add selected recipes to meal plan
            dbHelper.insertSelectedCount(recipeName);

            if(recipeInfo == null){
                throw new EmptyStackException();
            }else{
                view.setBackgroundColor(Color.LTGRAY);
                ((TextView)view).setText(recipeName + ("               (added " +count+ " times)"));
            }
        }
    }
}
