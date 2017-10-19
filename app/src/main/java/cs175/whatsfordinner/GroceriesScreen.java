package cs175.whatsfordinner;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import static cs175.whatsfordinner.R.id.listView;
import static java.lang.Math.abs;

public class GroceriesScreen extends AppCompatActivity {
    protected DBHelper dbHelper;
    private GestureDetectorCompat mDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_groceries_screen);

        dbHelper = new DBHelper(this);
        List<String> ingredientsList = dbHelper.getAllIngredients();

        List<String> grocList = new ArrayList<String>();
        List<Integer> counts = new ArrayList<Integer>();
        for (int i = 0; i < ingredientsList.size(); i++) {
            int index = grocList.indexOf(ingredientsList.get(i));
            if (index >= 0) {
                counts.set(index, counts.get(index) + 1);
            } else {
                grocList.add(ingredientsList.get(i));
                counts.add(new Integer(1));
            }
        }

        mDetector = new GestureDetectorCompat(this, new MyGestureListener());

        ListView listView = (ListView) findViewById(R.id.groceries_list);
        listView.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                mDetector.onTouchEvent(event);
                return true;
            }
        });

        for (int i = 0; i < grocList.size(); i++) {
            grocList.set(i, grocList.get(i) + " - " + counts.get(i).toString());

        }


        ListAdapter<String> arrayAdapter = new ListAdapter<String>(this, android.R.layout.simple_list_item_1, grocList);
        listView.setAdapter(arrayAdapter);
        listView.setTextFilterEnabled(true);

    }

    public void addGroc(View v){
        RelativeLayout ParentRow = (RelativeLayout) v.getParent();
        TextView text = (TextView)ParentRow.getChildAt(0);
        Button addBut = (Button)ParentRow.getChildAt(1);
        String str = text.getText().toString();

        String[] parts = str.split(" - ");
        String ingredient = parts[0];
        String quantity = parts[1];
        Log.d("quantity ", quantity);
        //int quantityIncrease = Integer.parseInt(quantity);
        //Log.d("quantityIncrease ", quantityIncrease);
        //String update = ingredient + Integer.toString(quantityIncrease);
        //text.setText(update);

        ParentRow.refreshDrawableState();


    }

    class MyGestureListener extends GestureDetector.SimpleOnGestureListener {
        private static final int SWIPE_NONE = 0;
        private static final int SWIPE_LEFT = 1;
        private static final int SWIPE_RIGHT = 2;

        @Override
        public boolean onFling(MotionEvent ev1, MotionEvent ev2,
                               float velocityX, float velocityY) {
            int action = SWIPE_NONE;
            if (abs(ev2.getY() - ev1.getY()) < 50) {
                if ((ev1.getX() - ev2.getX()) > 300) {
                    action = SWIPE_LEFT;
                } else if ((ev2.getX() - ev1.getX()) > 300) {
                    action = SWIPE_RIGHT;
                }
            }

            ListView listView = (ListView) findViewById(R.id.groceries_list);
            int pos = listView.pointToPosition((int)ev1.getX(),(int)ev1.getY());
            View child = listView.getChildAt(pos);
            if(action == SWIPE_LEFT) {
                child.findViewById(R.id.addButton).setVisibility(View.VISIBLE);
                child.findViewById(R.id.minusButton).setVisibility(View.VISIBLE);
            }
            if(action == SWIPE_RIGHT) {
                child.findViewById(R.id.addButton).setVisibility(View.INVISIBLE);
                child.findViewById(R.id.minusButton).setVisibility(View.INVISIBLE);
            }
            return true;
        }
    }

    class ListAdapter<T> extends ArrayAdapter<T> {

        public ListAdapter(Context context, int textViewResourceId) {
            super(context, textViewResourceId);
        }

        public ListAdapter(Context context, int resource, List<T> items) {
            super(context, resource, items);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View v = convertView;

            if (v == null) {
                LayoutInflater vi;
                vi = LayoutInflater.from(getContext());
                v = vi.inflate(R.layout.button_list_row, null);
            }

            T p = getItem(position);

            if (p != null) {
                TextView textView = (TextView) v.findViewById(R.id.groc_text);

                if (textView != null) {
                    textView.setText(p.toString());
                }
            }
            return v;
        }
    }
}

