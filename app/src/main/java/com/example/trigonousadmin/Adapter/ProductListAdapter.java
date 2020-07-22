package com.example.trigonousadmin.Adapter;

import android.content.Context;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.trigonousadmin.Interface.ItemClickInterface;
import com.example.trigonousadmin.Model.Product;
import com.example.trigonousadmin.R;
import com.example.trigonousadmin.Ui.ProductList;

import java.util.ArrayList;

public class ProductListAdapter extends RecyclerView.Adapter<ProductListAdapter.ProductListholder> {
    private static final String TAG = "Product_List_Adapter";
    private ArrayList<Product> productLists = new ArrayList<>();
    Context context;
    private long lastclicktime = 0;
    private ItemClickInterface itemClickInterface;

    public ProductListAdapter(ArrayList<Product> productLists, Context context, ItemClickInterface itemClickInterface) {
        this.productLists = productLists;
        this.context = context;
        this.itemClickInterface = itemClickInterface;
    }

    @NonNull
    @Override
    public ProductListholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.productcard, parent, false);
        return new ProductListAdapter.ProductListholder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductListholder holder, int position) {
        final Product product = productLists.get(position);

        holder.product_name.setText(product.getProductname());
        holder.product_code.setText(product.getProductcode());
        Glide.with(context).load(product.getProducturl())
                .placeholder(R.drawable.ic_image).into(holder.productimage);


        if (product.getProductavailable() == 0) {
            holder.product_code.setTextColor(context.getResources().getColor(R.color.colorred));
            holder.product_name.setTextColor(context.getResources().getColor(R.color.colorred));
        } else {
            holder.product_code.setTextColor(context.getResources().getColor(R.color.colorblack));
            holder.product_name.setTextColor(context.getResources().getColor(R.color.colorblack));
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SystemClock.elapsedRealtime() - lastclicktime < 1000) {
                    return;
                }
                lastclicktime = SystemClock.elapsedRealtime();
                itemClickInterface.OnItemsingleClick(product.getProductcode());
            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                itemClickInterface.OnItemLongClick(product.getProductcode());
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return productLists.size();
    }


    public class ProductListholder extends RecyclerView.ViewHolder {
        private TextView product_name, product_code;
        private ImageView productimage;

        public ProductListholder(@NonNull View itemView) {
            super(itemView);

            product_code = itemView.findViewById(R.id.card_productcode);
            product_name = itemView.findViewById(R.id.card_producttitle);
            productimage = itemView.findViewById(R.id.card_product_photo);
        }

    }
}
