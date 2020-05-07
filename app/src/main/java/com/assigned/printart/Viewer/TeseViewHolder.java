package com.assigned.printart.Viewer;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.assigned.printart.R;

public class TeseViewHolder extends RecyclerView.ViewHolder {
    public static TextView testtexview, deliverydate, cost, credit;

    public TeseViewHolder(@NonNull View itemView) {
        super(itemView);

        testtexview = itemView.findViewById(R.id.testtextview);
        deliverydate = itemView.findViewById(R.id.testDeliverydate);
        cost = itemView.findViewById(R.id.testCost);
        credit = itemView.findViewById(R.id.testCredit);
    }
}
