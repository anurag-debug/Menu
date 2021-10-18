package com.example.restaurentview;

import android.content.Context;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class CustomAdapter extends ArrayAdapter<SetDish> implements Filterable {
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;

ArrayList<SetDish> arrayList;


    public CustomAdapter(@NonNull Context context, int resource, ArrayList<SetDish> arrayList) {
        super(context, resource, arrayList);
        this.arrayList = arrayList;

    }


    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.dish, parent, false);
        }
        SetDish currentDish = getItem(position);
        TextView name = (TextView) listItemView.findViewById(R.id.dishName);
        name.setInputType(InputType.TYPE_TEXT_FLAG_CAP_WORDS);

        name.setText(currentDish.getDishName());

        TextView discount = (TextView) listItemView.findViewById(R.id.discount);
        TextView price = (TextView) listItemView.findViewById(R.id.price);
        price.setText(currentDish.getPrice());
        if (currentDish.getDiscount().equals("")){
        price.setBackground(null);}
        else{
            discount.setText(currentDish.getDiscount());
            discount.setVisibility(View.VISIBLE);
        }


        TextView desc = (TextView) listItemView.findViewById(R.id.desc);
        desc.setText(currentDish.getDishAbout());



        ImageView imageView = listItemView.findViewById(R.id.imageView);

        Glide.with(imageView.getContext())
                .load(currentDish.getDishImage())
                .into(imageView);


        TextView recommended = (TextView) listItemView.findViewById(R.id.recommended);
        if(currentDish.getRecommended().equals("true")){
            recommended.setVisibility(View.VISIBLE);
        }

        TextView newDish = (TextView) listItemView.findViewById(R.id.newLaunch);
        if(currentDish.getNewDish().equals("true")){
            newDish.setVisibility(View.VISIBLE);
        }

ImageView image = (ImageView) listItemView.findViewById(R.id.vegLogo);
        ImageView image2 = (ImageView) listItemView.findViewById(R.id.nonvegLogo);
        if (currentDish.getVeg().equals("true")){
            image.setVisibility(View.VISIBLE);
            image2.setVisibility(View.INVISIBLE);
        }
else{
            image.setVisibility(View.INVISIBLE);
    image2.setVisibility(View.VISIBLE);
        }
return listItemView;
    }

}
