package cs175.whatsfordinner;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MealScreen extends AppCompatActivity {
    protected DBHelper dbHelper = new DBHelper(this);
    Recipe recipe = new Recipe();

    private List<Spinner> spinners;
    private List<String> meals;
    final int[] spin_ids = {
            R.id.break1,
            R.id.lunch1,
            R.id.dinner1,
            R.id.break2,
            R.id.lunch2,
            R.id.dinner2,
            R.id.break3,
            R.id.lunch3,
            R.id.dinner3,
            R.id.break4,
            R.id.lunch4,
            R.id.dinner4,
            R.id.break5,
            R.id.lunch5,
            R.id.dinner5,
            R.id.break6,
            R.id.lunch6,
            R.id.dinner6,
            R.id.break7,
            R.id.lunch7,
            R.id.dinner7,
    };

    private List<Long> recipeSelected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal_screen);

        recipeSelected = new ArrayList<Long>();

        meals = dbHelper.getAllRecipeName();
        meals.add(0,"Eating out");
        String mealString = "";

        for (int i=0; i < spin_ids.length; i++) {
            Spinner spin = (Spinner) findViewById(spin_ids[i]);
            spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {
                }

                public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                    if(pos > 0) {
                        recipeSelected.add(new Long(id));
                        meals.remove(pos);
                    }
                }
            });
        }

        updateSpinner();
    }

    private void updateSpinner() {
        ArrayAdapter<String> arrayAdapter;
        spinners = new ArrayList<Spinner>();
        for (int i=0; i < spin_ids.length; i++) {
            if(recipeSelected.contains(spin_ids[i])) continue;
            Spinner spin = (Spinner) findViewById(spin_ids[i]);
            arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, meals);
            arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spin.setAdapter(arrayAdapter);
        }
    }

    public class MealAdapter extends ArrayAdapter<String> {

        private Context context;
        private List<String> mealList;

        public MealAdapter(Context context, int textViewResourceId,
                           List<String> values) {
            super(context, textViewResourceId, values);
            this.context = context;
            this.mealList = values;
        }


        //View of Spinner on dropdown Popping

        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {
            mealList = dbHelper.getAllRecipeName();
            TextView view = new TextView(context);
            String mealString = "";

            for(int i =0; i<mealList.size(); i++){
                //mealString += "* " + mealList.get(i) + "\n";
                view.setTextColor(Color.BLACK);
                view.setText(mealString);
            }
            return view;
        }
    }
}
