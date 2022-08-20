package com.eduar2tc.dinnerdecider;

import android.app.Activity;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import java.util.ArrayList;

public class ListAdapter extends ArrayAdapter<String> {
    private final Activity context;
    private final ArrayList<String> foodList;
    ListAdapter(Activity context, ArrayList<String> foodList) {
        super(context, R.layout.layout_list_food, foodList);
        this.context = context;
        this.foodList = foodList;
    }
    public View getView(int position, View view, ViewGroup parent){
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.layout_list_food, null, true);
        TextView foodName = rowView.findViewById(R.id.foodName);
        foodName.setText( this.foodList.get(position) );
        ImageButton imageButton = rowView.findViewById(R.id.deleteIcon);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(getContext(), R.style.dialog_style)
                        .setTitle( R.string.dialog_title )
                        .setMessage( R.string.dialog_message )
                        .setPositiveButton ( R.string.dialog_confirmation, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                View parentRow = (View) view.getParent();
                                ListView listView = (ListView) parentRow.getParent();
                                final int position = listView.getPositionForView(parentRow);
                                foodList.remove(position);
                                notifyDataSetChanged();
                            }
                        })
                        .setNegativeButton("No", null)
                        .show();
            }
        });
        return rowView;
    }



}
