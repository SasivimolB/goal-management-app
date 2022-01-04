package com.example.goalmanagement;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class CheckinggRecyclerViewAdapter extends RecyclerView.Adapter<CheckinggRecyclerViewAdapter.ViewHolder> {

    private List<Checkingg> checkList;
    private ClickListener clickListener;

    public CheckinggRecyclerViewAdapter(ClickListener clickListener) {
        this.clickListener = clickListener;
        checkList = new ArrayList<>();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.check_recyclerview_item_layout, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Checkingg check = checkList.get(position);
        holder.txtName.setText(check.c_name);
        holder.txtDesc.setText(check.c_description);
        holder.txtCategory.setText(check.c_category);

    }

    @Override
    public int getItemCount() {
        return checkList.size();
    }


    public void updateCheckList(List<Checkingg> data) {
        checkList.clear();
        checkList.addAll(data);
        notifyDataSetChanged();
    }

    public void addRow(Checkingg data) {
        checkList.add(data);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView txtName;
        public TextView txtDesc;
        public TextView txtCategory;
        public CardView cardView;

        public ViewHolder(View view) {
            super(view);

            txtName = view.findViewById(R.id.txtName);
            txtDesc = view.findViewById(R.id.txtDesc);
            txtCategory = view.findViewById(R.id.txtCategory);
            cardView = view.findViewById(R.id.cardView);
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickListener.launchIntent(checkList.get(getAdapterPosition()).check_id);
                }
            });
        }
    }

    public interface ClickListener {
        void launchIntent(int id);
    }
}
