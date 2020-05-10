package com.assigned.printart.Viewer;

import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.assigned.printart.R;

public class CategoryViewHolder extends RecyclerView.ViewHolder {

    public static RecyclerView category_recyclerView;
    public static RecyclerView.LayoutManager manager;
    public static TextView CategoryName;
    public static RelativeLayout relativeLayout;
    public static ImageView catimgv;

    public CategoryViewHolder(@NonNull View itemView) {
        super(itemView);
        manager = new LinearLayoutManager(itemView.getContext(), LinearLayoutManager.HORIZONTAL, false);
        CategoryName = itemView.findViewById(R.id.category_name);
        category_recyclerView = itemView.findViewById(R.id.recyclerView);
        category_recyclerView.setLayoutManager(manager);
        relativeLayout = itemView.findViewById(R.id.firstone);
        catimgv = itemView.findViewById(R.id.catimg);
    }
}
