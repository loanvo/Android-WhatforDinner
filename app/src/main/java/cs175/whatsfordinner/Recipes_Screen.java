package cs175.whatsfordinner;

import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class Recipes_Screen extends AppCompatActivity {

    List<String> wholeRecipe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recipe_detail);

        TextView recipename = (TextView) findViewById(R.id.textView2);
        TextView ingredients = (TextView) findViewById(R.id.textView3);
        TextView direction = (TextView) findViewById(R.id.textView4);
        ImageView imageView = (ImageView) findViewById(R.id.imageView);
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

        String igm;
        igm = intent.getStringExtra("image");
        imageView.setImageURI(Uri.parse(igm));

    }

    public void getDataEntry(View view){
        finish();
    }

}

