package com.example.goalmanagement;

import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.goalmanagement.R;

import java.util.ArrayList;
import java.util.List;

public class AchRecyclerViewAdapter extends RecyclerView.Adapter<AchRecyclerViewAdapter.ViewHolder> {

    private List<Ach> achList;
    private ClickListener clickListener;

    public AchRecyclerViewAdapter(ClickListener clickListener) {
        this.clickListener = clickListener;
        achList = new ArrayList<>();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ach_recyclerview_item_layout, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Ach ach = achList.get(position);
        holder.txtName.setText(ach.a_name);
        holder.txtDesc.setText(ach.a_description);
        holder.txtCategory.setText(ach.a_category);
        holder.txtDu.setText(String.valueOf(ach.duration));

        if(ach.a_category.equals("Exercise"))
        {
            Drawable drawable = holder.imageView.getResources().getDrawable(R.drawable.exerciseicon);
            holder.imageView.setImageDrawable(drawable);
        }
        else if(ach.a_category.equals("Health"))
        {
            Drawable drawable = holder.imageView.getResources().getDrawable(R.drawable.healthicon);
            holder.imageView.setImageDrawable(drawable);
        }
        else if(ach.a_category.equals("Medicine"))
        {
            Drawable drawable = holder.imageView.getResources().getDrawable(R.drawable.medicineicon);
            holder.imageView.setImageDrawable(drawable);
        }
        else
        {
            Drawable drawable = holder.imageView.getResources().getDrawable(R.drawable.studyicon);
            holder.imageView.setImageDrawable(drawable);
        }
    }

    @Override
    public int getItemCount() {
        return achList.size();
    }


    public void updateAchList(List<Ach> data) {
        achList.clear();
        achList.addAll(data);
        notifyDataSetChanged();
    }

    public void addRow(Ach data) {
        achList.add(data);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView txtName;
        public TextView txtDesc;
        public TextView txtCategory;
        public TextView txtDu;
        public CardView cardView;
        public ImageView imageView;

        public ViewHolder(View view) {
            super(view);

            txtName = view.findViewById(R.id.txtName);
            txtDesc = view.findViewById(R.id.txtDesc);
            txtCategory = view.findViewById(R.id.txtCategory);
            txtDu = view.findViewById(R.id.txtDu);
            cardView = view.findViewById(R.id.cardView);
            imageView = view.findViewById(R.id.imageView);
        }
    }

    public interface ClickListener {
        void launchIntent(int id);
    }
}
