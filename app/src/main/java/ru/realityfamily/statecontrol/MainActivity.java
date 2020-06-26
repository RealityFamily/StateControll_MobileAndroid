package ru.realityfamily.statecontrol;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView buttonContainer;
    private MyAdapter adapter;
    private SwipeRefreshLayout srl;

    public String Device;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        buttonContainer = (RecyclerView) findViewById(R.id.ButtonContainer);
        buttonContainer.setLayoutManager(new LinearLayoutManager(this));
        adapter = new MyAdapter();
        buttonContainer.setAdapter(adapter);

        srl = findViewById(R.id.SwipeRefresh);
        srl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new TaskModel().RunAdapter(MainActivity.this, adapter, new ArrayList<String>(), "None");
                srl.setRefreshing(false);
            }
        });

        new TaskModel().RunAdapter(this, adapter, new ArrayList<String>(), "None");
    }
}
