package com.assigned.printart.Viewer;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.assigned.printart.R;

public class MyordersViewHolder extends RecyclerView.ViewHolder {
    public static ImageView imgis;
    public static TextView name, description, quantity, price, total;
    public MyordersViewHolder(@NonNull View itemView) {
        super(itemView);
        imgis = itemView.findViewById(R.id.img);
        name = itemView.findViewById(R.id.cartname);
        description = itemView.findViewById(R.id.cartdescription);
        quantity = itemView.findViewById(R.id.cartquantity);
        price = itemView.findViewById(R.id.cartprice);
        total = itemView.findViewById(R.id.sum);

    }
}
