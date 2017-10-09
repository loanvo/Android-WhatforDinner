package cs175.whatsfordinner;

import android.app.Activity;
import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;


import java.util.List;

/**
 * Created by loanvo on 10/7/17.
 */

public class MyFragment extends Fragment{
    private OnItemSelectedListener listener;
    ArrayAdapter<String > arrayAdapter;
    List<String> list;  //list of all recipe name
    List<String> wholeRecipe;    // recipe detail
    protected DBHelper dbHelper;
    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.list_fragment, container, false);
        ListView mylistView = (ListView) view.findViewById(R.id.listView);
        mylistView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String recipeName = arrayAdapter.getItem(i);
                listener.OnRecipeSelected(recipeName);
            }
        });

        //Handle long press
        mylistView.setOnLongClickListener(new View.OnLongClickListener() {
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
        return view;
    }

    public interface OnItemSelectedListener {
        public void OnRecipeSelected(String recipeName);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if(activity instanceof  OnItemSelectedListener){
            listener =(OnItemSelectedListener) activity;
        }else{
            throw new ClassCastException(activity.toString() + " must implement MyFragment.OnItemSelectedListener");
        }
    }

    public void updateList(List<String> mylist){
        list = mylist;
        ListView mylistView = (ListView) view.findViewById(R.id.listView);
        arrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, list);
        mylistView.setAdapter(arrayAdapter);
        mylistView.setTextFilterEnabled(true);
    }
}
