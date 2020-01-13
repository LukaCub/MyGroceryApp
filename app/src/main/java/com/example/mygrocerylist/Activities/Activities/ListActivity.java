package com.example.mygrocerylist.Activities.Activities;

import android.app.AlertDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import com.example.mygrocerylist.Activities.Data.DatabaseHandler;
import com.example.mygrocerylist.Activities.Model.Grocery;
import com.example.mygrocerylist.Activities.UI.RecyclerViewAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.mygrocerylist.R;

import java.util.ArrayList;
import java.util.List;

public class ListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerViewAdapter recyclerViewAdapter;
    private List<Grocery> groceryList;
    private List<Grocery> listItems;
    private DatabaseHandler db;
    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    private EditText groceryItem;
    private EditText quantity;
    private Button saveButton;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        FloatingActionButton fab2 = findViewById(R.id.fab2);
//        fab2.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//            }
//        });

        FloatingActionButton fab2 = findViewById(R.id.fab2);
        fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();

                createPopupDialog();

            }
        });

        db = new DatabaseHandler(this);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerViewID);
        recyclerView .setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        groceryList = new ArrayList<>();
        listItems = new ArrayList<>();

        // Get items from databse
        groceryList = db.getAllGroceries();

        for (Grocery c: groceryList) {
            Grocery grocery = new Grocery();
            grocery.setName(c.getName());
            grocery.setQuantity("Qty: " + c.getQuantity());
            grocery.setId(c.getId());
            grocery.setDateItemAdded("Added on: " + c.getDateItemAdded());

            listItems.add(grocery);
        }

        recyclerViewAdapter = new RecyclerViewAdapter(this, listItems);
        recyclerView.setAdapter(recyclerViewAdapter);
        recyclerViewAdapter.notifyDataSetChanged();

    }

    private void createPopupDialog() {

        dialogBuilder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.popup, null);

        groceryItem = (EditText) view.findViewById(R.id.groceryItem);
        quantity = (EditText) view.findViewById(R.id.groceryQty);
        saveButton = (Button) view.findViewById(R.id.saveButton);

        dialogBuilder.setView(view);
        dialog = dialogBuilder.create();
        dialog.show();

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Todo: Save to db
                //Todo: Go to next screen

                if (groceryItem.getText().toString().isEmpty()
                        && !quantity.getText().toString().isEmpty()) {
                    saveGroceryToDB(v);
                }
                saveGroceryToDB(v);
            }
        });

    }

    private void saveGroceryToDB(View v) {

        Grocery grocery = new Grocery();

        String newGrocery = groceryItem.getText().toString();
        String newGroceryQuantity = quantity.getText().toString();

        grocery.setName(newGrocery);
        grocery.setQuantity(newGroceryQuantity);

        // Save to DB
        db.addGrocery(grocery);


        Snackbar.make(v, "Item Saved!", Snackbar.LENGTH_LONG).show();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                dialog.dismiss();
            }
        }, 1000);

    }
}
