package com.assigned.printart.Viewer;

        import android.view.View;
        import android.widget.ImageView;
        import android.widget.TextView;
        import androidx.annotation.NonNull;
        import androidx.recyclerview.widget.RecyclerView;
        import com.assigned.printart.R;
        import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class DisplayProductViewHolder extends RecyclerView.ViewHolder
{
    public static ImageView imgv;
    public static ImageView locl_buttons;
    public static TextView Pname, POPrice, PSPrice, Pdes, Seller, number, remove,discount;
    public static TextView b1,b2,b3;
    public DisplayProductViewHolder(@NonNull View itemView) {
        super(itemView);
        imgv=itemView.findViewById(R.id.images);
        locl_buttons=itemView.findViewById(R.id.locl_button);
        Pname=itemView.findViewById(R.id.name);
        POPrice=itemView.findViewById(R.id.productorigialPrice);
        PSPrice=itemView.findViewById(R.id.productofferPrice);
        Pdes=itemView.findViewById(R.id.descriptioN);
        Seller=itemView.findViewById(R.id.seller);
        remove = itemView.findViewById(R.id.trash);
        discount = itemView.findViewById(R.id.discount);
        b1=itemView.findViewById(R.id.b1);
        b2=itemView.findViewById(R.id.b2);
        b3=itemView.findViewById(R.id.b3);
    }
}
