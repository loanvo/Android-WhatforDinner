package cs175.whatsfordinner;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;

import static android.provider.AlarmClock.EXTRA_MESSAGE;

public class MainScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);
    }

    public void NewDish(View view){
        Intent intent = new Intent(this, NewDishScreen.class);
        //EditText editText = (EditText) findViewById(R.id.recipe_name);
        //String name = editText.getText().toString();
       // intent.putExtra(EXTRA_MESSAGE, name);
        startActivity(intent);
    }

    public void getMeal(View view){
        Intent intent = new Intent(this, MealScreen.class);
        startActivity(intent);
    }


}
