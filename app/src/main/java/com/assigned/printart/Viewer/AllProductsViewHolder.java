package com.assigned.printart.Viewer;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.assigned.printart.R;

public class AllProductsViewHolder extends RecyclerView.ViewHolder {
    public static ImageView imageis;
    public static TextView textis;

    public AllProductsViewHolder(@NonNull View itemView) {
        super(itemView);
        imageis = itemView.findViewById(R.id.imageis);
        textis = itemView.findViewById(R.id.textis);

    }
}
