package cs175.whatsfordinner;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class Recipes_Screen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recipe_detail);

        TextView recipename = (TextView) findViewById(R.id.textView2);
        TextView ingredients = (TextView) findViewById(R.id.textView3);
        TextView direction = (TextView) findViewById(R.id.textView4);
        Intent intent = getIntent();

        String name;
        name = intent.getStringExtra("recipename");
        recipename.setText(name);

        String items;
        items = intent.getStringExtra("ingredients");
        ingredients.setText(items);

        String dir;
        dir = intent.getStringExtra("direction");
        direction.setText(dir);
    }

    public void getDataEntry(View view){
        finish();
    }

}
