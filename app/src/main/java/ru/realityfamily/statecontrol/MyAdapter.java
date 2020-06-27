package ru.realityfamily.statecontrol;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
    private List<String> mDataset = new ArrayList<>();
    private View.OnClickListener mListener;

    static class MyViewHolder extends RecyclerView.ViewHolder{

        private Button btn;
        public MyViewHolder(View v) {
            super(v);
            // Finding button on layout
            btn = (Button) v.findViewById(R.id.Content);
        }

        public void bind(String text, View.OnClickListener listener) {
            // Setting received title and ClickListener to View
            btn.setText(text);
            btn.setOnClickListener(listener);
        }

        public static MyViewHolder inflate(ViewGroup parent) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
            return new MyViewHolder(view);
        }
    }

    public void setItems(List<String> Dataset, View.OnClickListener Listener) {
        // Setting to adapter array of titles and ClickListener for buttons
        mDataset = Dataset;
        mListener = Listener;
        // Reload RecyclerView
        notifyDataSetChanged();
    }

    // Create new views (invoked by the layout manager)
    @Override
    public MyAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return MyViewHolder.inflate(parent);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.bind(mDataset.get(position), mListener);

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}
