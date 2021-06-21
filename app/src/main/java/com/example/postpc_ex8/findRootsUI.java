package com.example.postpc_ex8;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

class findRootsViewHolder extends RecyclerView.ViewHolder {
    TextView suffix;
    TextView preffix;
    FloatingActionButton cancelButton;
    FloatingActionButton deleteButton;
    ProgressBar progressBar;
    ConstraintLayout constraintLayout;
    public findRootsViewHolder(View view) {
        super(view);
        preffix = view.findViewById(R.id.prefix);
        suffix = view.findViewById(R.id.suffix);
        cancelButton = view.findViewById(R.id.cancel_button);
        deleteButton = view.findViewById(R.id.delete_button);
        progressBar = view.findViewById(R.id.progressBar);
        constraintLayout = view.findViewById(R.id.constraintLayout);
    }
}

class findRootsAdapter extends RecyclerView.Adapter<findRootsViewHolder> {


    private findRootsHolder _findRootsArrayList;
    public findRootsAdapter(findRootsHolder holder)
    {
        _findRootsArrayList = holder;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void setRootsFinders(List<RootsFinder> finders) {
        if (!_findRootsArrayList.getCurrentFinders().isEmpty())
            _findRootsArrayList.clear();
        _findRootsArrayList.addAll(finders);
//        Collections.sort(_findRootsArrayList.itemsList, new ToDoItemsCompartor());
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return _findRootsArrayList.size();
    }

    public @NotNull
    findRootsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View view = LayoutInflater.from(context)
                .inflate(R.layout.row_find_roots, parent, false);
        return new findRootsViewHolder(view);
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    public void onBindViewHolder(findRootsViewHolder holder, int position) {
        RootsFinder finder = _findRootsArrayList.getCurrentFinders().get(position);
        holder.preffix.setText(finder.getPreffix());
        holder.suffix.setText(finder.getSuffix());
        holder.progressBar.setProgress(finder.getProgress()); //todo: set the appropriate number
        // if in progress disable deleteButton and enable cancelButton
        if (finder.getProgress()<100)
        {
            holder.cancelButton.setVisibility(View.VISIBLE);
            holder.deleteButton.setVisibility(View.INVISIBLE);
        }
        else{
            holder.cancelButton.setVisibility(View.INVISIBLE);
            holder.deleteButton.setVisibility(View.VISIBLE);
        }

        holder.cancelButton.setOnClickListener(v->{
            _findRootsArrayList.deleteFinder(finder);
//            Collections.sort(_todoItemArrayList.itemsList, new ToDoItemsCompartor());
            notifyDataSetChanged();
        });


        holder.deleteButton.setOnClickListener(v->{
            _findRootsArrayList.deleteFinder(finder);
//            Collections.sort(_todoItemArrayList.itemsList, new ToDoItemsCompartor());
            notifyDataSetChanged();
        });


    }

}
