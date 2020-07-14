package ru.realityfamily.statecontrol.Fragments;

import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import ru.realityfamily.statecontrol.MyAdapter;
import ru.realityfamily.statecontrol.R;
import ru.realityfamily.statecontrol.TaskModel;

public class LauncherFragment extends Fragment {

    private RecyclerView buttonContainer;
    private MyAdapter adapter;
    private SwipeRefreshLayout srl;
    private Button close;

    private TaskModel taskModel;

    private Handler gameMessage;
    private Handler deviceMessage;
    private Handler disconnectMessage;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.fragment_launher, null);

        taskModel = new TaskModel();

        close = (Button) v.findViewById(R.id.Close);
        // getting RecyclerView from window
        buttonContainer = (RecyclerView) v.findViewById(R.id.ButtonContainer);
        //configuration RecyclerView
        buttonContainer.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new MyAdapter();
        buttonContainer.setAdapter(adapter);

        //initialize handlers
        gameMessage = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                ((TextView) v.findViewById(R.id.App)).setText("Приложение: " + ((String) msg.obj));
            }
        };
        deviceMessage = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                ((TextView) v.findViewById(R.id.Device)).setText("Устройство: " + ((String) msg.obj));
            }
        };
        disconnectMessage = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                ((Button) v.findViewById(R.id.Close)).setVisibility(msg.what);
            }
        };

        // getting SwipeRefresh from window
        srl = v.findViewById(R.id.SwipeRefresh);
        srl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (taskModel.Status.equals("States")) {
                    //taskModel.GetStates(getContext(), adapter, deviceMessage, disconnectMessage, taskModel.Device);
                }
                else if (taskModel.Status.equals("Games")) {
                    //taskModel.GetLaunchersGames(getContext(), adapter, gameMessage, deviceMessage, disconnectMessage, taskModel.Game);
                }
                else if (taskModel.Status.equals("Devices")) {
                    //taskModel.GetLaunchers(getContext(), adapter, gameMessage, deviceMessage);
                }
                srl.setRefreshing(false);
            }
        });

        // Run method of getting info from server and placing it to RecyclerView
        //taskModel.GetGames(getContext(), adapter, gameMessage, deviceMessage);

        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (taskModel.Status.equals("States")) {
                    //Toast.makeText(getContext(), "Чтобы выйти из приложения закройте его.", Toast.LENGTH_LONG).show();
                }
                else if (taskModel.Status.equals("Games")) {
                    //taskModel.GetLaunchers(getContext(), adapter, gameMessage, deviceMessage);
                }
                else if (taskModel.Status.equals("Devices")) {
                    //NavHostFragment.findNavController(LauncherFragment.this).popBackStack();
                } else {
                    NavHostFragment.findNavController(LauncherFragment.this).popBackStack();
                }
            }
        });

        close.setVisibility(View.GONE);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                taskModel.CloseApp(getContext());
                taskModel.GetDevices(getContext(), adapter, gameMessage, deviceMessage, taskModel.Game);
            }
        });

        return v;
    }
}