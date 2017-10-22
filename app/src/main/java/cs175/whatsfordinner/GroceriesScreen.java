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
    private String[] units = { "lb",  "pcs",  "oz",  "tsp",  "ml", "cup" };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_groceries_screen);

        dbHelper = new DBHelper(this);
        List<String> ingredientsList = dbHelper.getAllIngredients();

        List<String> grocList = new ArrayList<String>();
        List<Float> counts = new ArrayList<Float>();
        List<String> unitList = new ArrayList<String>();
        for (int i = 0; i < ingredientsList.size(); i++) {
            String[] parts = ingredientsList.get(i).toString().split("~");
            if((parts.length<3) || (parts[0].equals(""))) continue;
            int index1 = grocList.indexOf(parts[0]);
            int index2 = unitList.indexOf(parts[2]);
            if ((index1 >= 0) && (index2 == index1)) {
                counts.set(index1, counts.get(index1) + Float.parseFloat(parts[1]));
            } else {
                grocList.add(parts[0]);
                counts.add(Float.parseFloat(parts[1]));
                unitList.add(parts[2]);
            }
        }
        for (int i = 0; i < grocList.size(); i++) {
            grocList.set(i, grocList.get(i) + "~" +
                            counts.get(i) + "~" +
                            unitList.get(i));
        }

        mDetector = new GestureDetectorCompat(this, new MyGestureListener());

        ListView listView = (ListView) findViewById(R.id.groceries_list);
        listView.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                mDetector.onTouchEvent(event);
                return true;
            }
        });

        ListAdapter<String> arrayAdapter = new ListAdapter<String>(this, android.R.layout.simple_list_item_1, grocList);
        listView.setAdapter(arrayAdapter);
        listView.setTextFilterEnabled(true);

    }

    public void addGroc(View v){
        RelativeLayout ParentRow = (RelativeLayout) v.getParent();
        TextView text = (TextView)ParentRow.getChildAt(0);

        String str = text.getText().toString();

        String[] names = str.split(":");
        String[] parts = names[1].split(" ");
        if(parts.length>=2) {
            Float quantity = Float.parseFloat(parts[1]) + 1;
            String update = names[0] + ": " + quantity.toString() + " " + parts[2];
            text.setText(update);
            ParentRow.refreshDrawableState();
        }
    }

    public void minusGroc(View v){
        RelativeLayout ParentRow = (RelativeLayout) v.getParent();
        TextView text = (TextView)ParentRow.getChildAt(0);

        String str = text.getText().toString();

        String[] names = str.split(":");
        String[] parts = names[1].split(" ");
        if(parts.length>=2) {
            Float quantity = Float.parseFloat(parts[1]) - 1;
            if(quantity<0) return;
            String update = names[0] + ": " + quantity.toString() + " " + parts[2];
            text.setText(update);
            ParentRow.refreshDrawableState();
        }
    }

    class MyGestureListener extends GestureDetector.SimpleOnGestureListener {
        private static final int SWIPE_NONE = 0;
        private static final int SWIPE_LEFT = 1;
        private static final int SWIPE_RIGHT = 2;
        private static final int SWIPE_UP = 3;
        private static final int SWIPE_DOWN = 4;

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
            } else if (abs(ev2.getX() - ev1.getX()) < 50) {
                if ((ev1.getY() - ev2.getY()) > 300) {
                    action = SWIPE_UP;
                } else if ((ev2.getY() - ev1.getY()) > 300) {
                    action = SWIPE_DOWN;
                }
            }
            ListView listView = (ListView) findViewById(R.id.groceries_list);
            int pos = listView.pointToPosition((int)ev1.getX(),(int)ev1.getY());
            Log.e("---pos---:",Integer.toString(pos));
            Log.e("---scrl--:",Integer.toString(listView.getScrollY()));
            Log.e("--vscrl--:",Integer.toString(listView.getVerticalScrollbarPosition()));
            Log.e("---childcount:",Integer.toString(listView.getChildCount()));
            View child = listView.getChildAt(pos);
            if (child != null) {
                if(action == SWIPE_LEFT) {
                    child.findViewById(R.id.addButton).setVisibility(View.VISIBLE);
                    child.findViewById(R.id.minusButton).setVisibility(View.VISIBLE);
                }
                if(action == SWIPE_RIGHT) {
                    child.findViewById(R.id.addButton).setVisibility(View.INVISIBLE);
                    child.findViewById(R.id.minusButton).setVisibility(View.INVISIBLE);
                }
            }
            if(action == SWIPE_UP) {
                listView.smoothScrollByOffset(3);
            }
            if(action == SWIPE_DOWN) {
                listView.smoothScrollByOffset(-3);
            }
            return super.onFling(ev1, ev2, velocityX, velocityY);
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
                    String[] parts = p.toString().split("~");
                    if((parts.length>=3) && (!parts[0].equals(""))) {
                        textView.setText(parts[0] + ": " + parts[1] +
                                         " " + units[Integer.parseInt(parts[2])]);
                    }
                }
            }
            return v;
        }
    }
}

