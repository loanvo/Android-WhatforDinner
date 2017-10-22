package cs175.whatsfordinner;

import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by loanvo on 10/7/17.
 */

public class DetailFragment extends Fragment {
    View view;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.recipe_detail_fragment, container, false);
        return view;
    }

    public void updateDetail(Recipe recipe){
        TextView recipename = (TextView) view.findViewById(R.id.textView2);
        TextView ingredients = (TextView) view.findViewById(R.id.textView3);
        TextView direction = (TextView) view.findViewById(R.id.textView4);
        ImageView imageView = (ImageView) view.findViewById(R.id.imageView);

        String[] units = { "lb",  "pcs",  "oz",  "tsp",  "ml", "cup" };

        String name = recipe.getName();
        recipename.setText(name);

        List<String> items = recipe.getItems();
        String itemString = "";
        for(int i =0; i<items.size(); i++){
            String[] parts = items.get(i).toString().split("~");
            if((parts.length<3) || (parts[0].equals(""))) continue;
            itemString += "* " + parts[0] +
                           " " + parts[1] +
                           " " + units[Integer.parseInt(parts[2])] +
                           "\n";
        }
        ingredients.setText(itemString);

        String dir = recipe.getDirection();
        direction.setText(dir);

        Uri igm = recipe.getImage();
        //imageView.setImageURI(igm);
    }
}
