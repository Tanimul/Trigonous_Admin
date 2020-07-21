package com.example.trigonousadmin.Adapter;

import android.content.Context;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.trigonousadmin.Interface.ItemClickInterface;
import com.example.trigonousadmin.R;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewholder> {
    private Context context;
    private ArrayList<String> categoryname;
    private ItemClickInterface itemClickInterface;
    private long lastclicktime = 0;

    public CategoryAdapter(Context context, ArrayList<String> categoryname, ItemClickInterface itemClickInterface) {
        this.context = context;
        this.categoryname = categoryname;
        this.itemClickInterface = itemClickInterface;
    }

    @NonNull
    @Override
    public CategoryViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.categories_card, parent, false);
        return new CategoryAdapter.CategoryViewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewholder holder, final int position) {

        final String category_name = categoryname.get(position);
        holder.categoryname.setText(category_name);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SystemClock.elapsedRealtime() - lastclicktime < 1000) {
                    return;
                }
                lastclicktime = SystemClock.elapsedRealtime();
                itemClickInterface.OnItemsingleClick(category_name);
            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                itemClickInterface.OnItemLongClick(category_name);
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return categoryname.size();
    }

    public class CategoryViewholder extends RecyclerView.ViewHolder {
        public TextView categoryname;

        public CategoryViewholder(@NonNull View itemView) {
            super(itemView);
            categoryname = itemView.findViewById(R.id.card_category_name);
        }
    }

}
