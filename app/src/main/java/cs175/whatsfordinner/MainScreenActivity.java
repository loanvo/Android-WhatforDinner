package cs175.whatsfordinner;

import android.content.Intent;
import android.os.Handler;
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

    //Handle long press


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);

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
