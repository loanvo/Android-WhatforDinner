package cs175.whatsfordinner;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class GroceriesScreen extends AppCompatActivity {
    protected DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_groceries_screen);

        dbHelper = new DBHelper(this);
        List<String> ingredientsList = dbHelper.getAllIngredients();

        List<String> grocList = new ArrayList<String>();
        List<Integer> counts = new ArrayList<Integer>();
        for (int i=0; i < ingredientsList.size(); i++) {
            int index = grocList.indexOf(ingredientsList.get(i));
            if (index >= 0) {
                counts.set(index,counts.get(index)+1);
            } else {
                grocList.add(ingredientsList.get(i));
                counts.add(new Integer(1));
            }
        }

        for (int i=0; i < grocList.size(); i++) {
            grocList.set(i, grocList.get(i) + " - " + counts.get(i).toString());
        }

        ListView listView = (ListView) findViewById(R.id.groceries_list);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, grocList);
        listView.setAdapter(arrayAdapter);
        listView.setTextFilterEnabled(true);

    }

}

