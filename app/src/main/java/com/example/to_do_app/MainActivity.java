package com.example.to_do_app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;

import com.example.to_do_app.Adapter.ToDoAdapter;
import com.example.to_do_app.Model.ToDoModel;
import com.example.to_do_app.Utils.DatabaseHandler;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity implements DialogCloseListener {

    private RecyclerView taskRecyclerView;
    private ToDoAdapter taskAdapter;

    private List<ToDoModel> tasklist;

    private DatabaseHandler db;
    private FloatingActionButton fab;

    private final static String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
       // getSupportActionBar().hide();

        db = new DatabaseHandler(this);
        db.openDatabase();

        tasklist= new ArrayList<>();

        taskRecyclerView = findViewById(R.id.tasksRecyclerView);
        taskRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        taskAdapter = new ToDoAdapter(db,this);
        taskRecyclerView.setAdapter(taskAdapter);

        /*//add dummy data to the view
        ToDoModel task = new ToDoModel();
        task.setTask("This is dummy task");
        task.setStatus(0);
        task.setId(1);

        tasklist.add(task);
        tasklist.add(task);
        tasklist.add(task);
        tasklist.add(task);
        tasklist.add(task);

        taskAdapter.setTask(tasklist);*/

        fab = findViewById(R.id.fab);

        //callitemTouchHelper class
        ItemTouchHelper itemTouchHelper = new
                ItemTouchHelper(new RecyclerItemTouchHelper(taskAdapter));
        itemTouchHelper.attachToRecyclerView(taskRecyclerView);



        tasklist = db.getAllTasks();
        Collections.reverse(tasklist);
        taskAdapter.setTask(tasklist);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddNewTask.newInstance().show(getSupportFragmentManager(),AddNewTask.TAG);
            }
        });




    }

    @Override
    public void handleDialogClose(DialogInterface dialog) {
        tasklist = db.getAllTasks();
        Collections.reverse(tasklist);
        taskAdapter.setTask(tasklist);
        taskAdapter.notifyDataSetChanged();
    }
}