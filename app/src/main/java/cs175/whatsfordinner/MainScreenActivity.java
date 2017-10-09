package cs175.whatsfordinner;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

import static android.provider.AlarmClock.EXTRA_MESSAGE;



public class MainScreenActivity extends AppCompatActivity {
    private List<String> list;
    protected DBHelper dbHelper;


    private ImageView image;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);

        image = (ImageView) findViewById(R.id.imageView);
        //Handle long press
        image.setOnLongClickListener(new View.OnLongClickListener() {
            public boolean onLongClick(View view) {
                AlertDialog alertDialog = new AlertDialog.Builder(view.getContext()).create();
                alertDialog.setTitle("");
                alertDialog.setMessage("Author:   Loan Vo" + "\n" + "Version:   1.0" + "\n"
                                        + "Link for help:   https://developer.android.com/develop/index.html"
                                + "\n\n" + "Copyright © Loan Vo, 2017");
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                alertDialog.show();

                return true;
            }
        });

    }

    public void NewDish(View view){
        Intent intent = new Intent(this, NewDishScreen.class);
        startActivity(intent);
    }

    public void getRecipeList(View view){
        Intent intent = new Intent(this, RecipeScreenActivity.class);
        startActivity(intent);
    }

    public void getIngredients(View view){
        Intent intent = new Intent(this, GroceriesScreen.class);
        startActivity(intent);
    }

    public void getMeal(View view){
        Intent intent = new Intent(this, MealScreen.class);
        startActivity(intent);
    }
}
