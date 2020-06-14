package com.assigned.printart.Adapter;

import android.media.Image;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.assigned.printart.MainActivity;
import com.assigned.printart.Model.HorizontalScroller;
import com.assigned.printart.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.squareup.picasso.Picasso;

public class YourDesignsPage extends FirebaseRecyclerAdapter<HorizontalScroller, YourDesignsPage.Horizontalviewholder> {
    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */

    public YourDesignsPage(@NonNull FirebaseRecyclerOptions<HorizontalScroller> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull Horizontalviewholder holder, int position, @NonNull HorizontalScroller model) {

            String x = model.getBlank();
            Picasso.get().load(x).into(holder.imageView);

   }

    @NonNull
    @Override
    public Horizontalviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.yourdesignlayout, parent, false);
        return new Horizontalviewholder(view);
    }

    static class Horizontalviewholder extends RecyclerView.ViewHolder {
        ImageView imageView;


        public Horizontalviewholder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageview);

        }
    }
}
