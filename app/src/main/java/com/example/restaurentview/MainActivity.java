package com.example.restaurentview;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.SwitchCompat;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private ArrayList<SetDish> arr;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
String emailchild;
    private ListView listView;
    private CustomAdapter customAdapter;
    private Map map;
    private ProgressBar progressBar;
    private LinearLayout nameLL;
    private LinearLayout vegOnlyLL;
    private TextView sorry;
    private SwipeRefreshLayout swipeRefreshLayout;
    private SwitchCompat switchbt;
    static int veg =0;
    private SearchView searchView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        switchbt = findViewById(R.id.switchButton);
        swipeRefreshLayout = findViewById ( R.id.swiperefreshlayout );
        emailchild = getIntent().getStringExtra("emailchild");
        FirebaseStorage storage = FirebaseStorage.getInstance();
        arr = new ArrayList<>();
        progressBar = findViewById(R.id.progressBar);
        firebaseDatabase = FirebaseDatabase.getInstance();
ArrayList<SetDish> setDishArrayList = new ArrayList<>();

        databaseReference = firebaseDatabase.getReference().child("Dishes").child(emailchild);
        customAdapter = new CustomAdapter(this,R.layout.dish,arr);
        listView = findViewById(R.id.parents);
        listView.setAdapter(customAdapter);
        nameLL=findViewById(R.id.nameLL);
        vegOnlyLL=findViewById(R.id.vegOnlyLL);
        vegOnlyLL.setVisibility(View.INVISIBLE);
        nameLL.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);
        sorry = findViewById(R.id.sorry);
        searchView = findViewById(R.id.search);

        switchbt.setOnClickListener(v -> {
            ArrayList<SetDish> textToCheckList=new ArrayList<>();

            if (switchbt.isChecked()){
                for (SetDish sd: arr) {
                    if(sd.getVeg().equals("true")){
                        textToCheckList.add(sd);
                    }
                }
            }
            else{
                textToCheckList.addAll(arr) ;
            }
            CustomAdapter customadapter = new CustomAdapter(MainActivity.this,R.layout.dish,textToCheckList);
            listView.setAdapter(customadapter);
            customadapter.notifyDataSetChanged();

        });

searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        ArrayList<SetDish> textToCheckList=new ArrayList<>();
        String textToCheck=s.toString();
        if(s.length()!=0){
            //code for making First letter of string CAPITAL
            textToCheck = textToCheck.toUpperCase();
            //code for filling second list from backup list based on text to search here in this case, it is "textToCheck"
            for (SetDish sd: arr) {
                if(sd.getDishName().toUpperCase().contains(textToCheck)){
                    textToCheckList.add(sd);
                }
            }
        }else{
            textToCheckList.addAll(arr) ;
        }
// Setting new list to adapter and notifying it
        CustomAdapter customadapter = new CustomAdapter(MainActivity.this,R.layout.dish,textToCheckList);
        listView.setAdapter(customadapter);
        customadapter.notifyDataSetChanged();
        return false;
    }
});

        swipeRefreshLayout.setOnRefreshListener(() -> {

            finish();
            overridePendingTransition(0,0);
            startActivity(getIntent());
            overridePendingTransition(0,0);
            swipeRefreshLayout.setRefreshing(false);

        });



        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                        map = (Map)dataSnapshot.getValue();
                            arr.add(new SetDish(map.get("discount").toString(),map.get("dishAbout").toString(),map.get("dishImage").toString(),map.get("dishName").toString(),map.get("newDish").toString(),map.get("price").toString(),map.get("recommended").toString(),map.get("veg").toString()));



                        progressBar.setVisibility(View.INVISIBLE);
                        vegOnlyLL.setVisibility(View.VISIBLE);
                        nameLL.setVisibility(View.VISIBLE);
                    }
//                    for (int i=0;i<arr.size();i++){
//                        SetDish filterDish = new SetDish(arr.get(i).getDiscount(),arr.get(i).getDishAbout(),arr.get(i).getDishImage(),arr.get(i).getDishName(),arr.get(i).getNewDish(),arr.get(i).getPrice(),arr.get(i).getRecommended(),arr.get(i).getVeg());
//                        Toast.makeText(MainActivity.this, "filterDish.getDishName()-->"+filterDish.getDishName(), Toast.LENGTH_SHORT).show();
//                    }

                    customAdapter.notifyDataSetChanged();

                }
                else{
                    sorry.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.dotmenu, menu);
        return true;
    }

    public void sign_out_menu(MenuItem item) {
        FirebaseAuth.getInstance().signOut();
        Intent intToMain = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intToMain);
    }

    public void Add_in_menu(MenuItem item) {
        Intent intToMain = new Intent(MainActivity.this, EditDish.class);
        intToMain.putExtra("emailchild",emailchild);

        startActivity(intToMain);
    }

}
