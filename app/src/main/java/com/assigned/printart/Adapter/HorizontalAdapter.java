package com.assigned.printart.Adapter;

import android.media.Image;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.assigned.printart.MainActivity;
import com.assigned.printart.Model.HorizontalScroller;
import com.assigned.printart.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.squareup.picasso.Picasso;

public class HorizontalAdapter extends FirebaseRecyclerAdapter<HorizontalScroller, HorizontalAdapter.Horizontalviewholder> {
    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public HorizontalAdapter(@NonNull FirebaseRecyclerOptions<HorizontalScroller> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull Horizontalviewholder holder, int position, @NonNull HorizontalScroller model) {


        if (holder.count == 1) {
            String x = model.getImage();
            Picasso.get().load(x).into(holder.imageView);
            holder.count = holder.count + 1;
        } else {
            String x = model.getImage();
            Picasso.get().load(x).into(holder.imageView1);
        }
    }

    @NonNull
    @Override
    public Horizontalviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.horizontalrecyclerdesign, parent, false);
        return new Horizontalviewholder(view);
    }

    static class Horizontalviewholder extends RecyclerView.ViewHolder {
        ImageView imageView, imageView1;
        public static int count = 1;

        public Horizontalviewholder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imgviewis);
            imageView1 = itemView.findViewById(R.id.imgviewiss);
        }
    }
}
