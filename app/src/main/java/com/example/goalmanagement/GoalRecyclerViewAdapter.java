package com.example.goalmanagement;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class GoalRecyclerViewAdapter extends RecyclerView.Adapter<GoalRecyclerViewAdapter.ViewHolder> {

    private List<Goal> goalList;
    private ClickListener clickListener;

    public GoalRecyclerViewAdapter(ClickListener clickListener) {
        this.clickListener = clickListener;
        goalList = new ArrayList<>();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.goal_recyclerview_item_layout, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Goal goal = goalList.get(position);
        holder.txtName.setText(goal.g_name);
        holder.txtNo.setText("#" + String.valueOf(goal.goal_id));
        holder.txtDesc.setText(goal.g_description);
        holder.txtDu.setText(String.valueOf(goal.duration));
        holder.txtCategory.setText(goal.g_category);

    }

    @Override
    public int getItemCount() {
        return goalList.size();
    }


    public void updateGoalList(List<Goal> data) {
        goalList.clear();
        goalList.addAll(data);
        notifyDataSetChanged();
    }

    public void addRow(Goal data) {
        goalList.add(data);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView txtName;
        public TextView txtNo;
        public TextView txtDesc;
        public TextView txtDu;
        public TextView txtCategory;
        public CardView cardView;

        public ViewHolder(View view) {
            super(view);

            txtNo = view.findViewById(R.id.txtNo);
            txtName = view.findViewById(R.id.txtName);
            txtDesc = view.findViewById(R.id.txtDesc);
            txtDu = view.findViewById(R.id.txtDu);
            txtCategory = view.findViewById(R.id.txtCategory);
            cardView = view.findViewById(R.id.cardView);
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickListener.launchIntent(goalList.get(getAdapterPosition()).goal_id);
                }
            });
        }
    }

    public interface ClickListener {
        void launchIntent(int id);
    }
}
