package com.example.trigonousadmin.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.trigonousadmin.Interface.ItemClickInterface;
import com.example.trigonousadmin.Interface.OrderItemClikInterface;
import com.example.trigonousadmin.Model.Admin;
import com.example.trigonousadmin.Model.Order;
import com.example.trigonousadmin.Model.Product;
import com.example.trigonousadmin.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class OrderListAdapter extends RecyclerView.Adapter<OrderListAdapter.OrderViewHolder> {
    private static final String TAG = "Product_List_Adapter";
    private ArrayList<Order> orderLists = new ArrayList<>();
    Context context;
    private OrderItemClikInterface itemClickInterface;

    public OrderListAdapter(ArrayList<Order> orderLists, Context context, OrderItemClikInterface itemClickInterface) {
        this.orderLists = orderLists;
        this.context = context;
        this.itemClickInterface = itemClickInterface;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.ordercard, parent, false);
        return new OrderListAdapter.OrderViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        final Order order = orderLists.get(position);

        holder.productcode.setText("Product Code: " + order.getProductcode());
        holder.category.setText("Category: " + order.getProductcategory());
        holder.quantity.setText("Quantity: " + order.getProductquantity());
        holder.productcost.setText("Product Cost: " + order.getProductcost());
        holder.deliverycost.setText("Delivery Cost: " + order.getDeliverycost());
        holder.totalcost.setText("Total Cost: " + order.getTotalcost());
        holder.totalpaid.setText("Total Paid: " + order.getTotalpaid());
        holder.totaldue.setText("Total Due: " + order.getTotaldue());
        holder.customername.setText("Customer Name: " +order.getCustomername());
        holder.phoneno.setText("Phone Number: " +order.getCustomerphone());
        holder.area.setText("Area: " +order.getCustomerarea());
        holder.date.setText("Date: " +order.getCal());
        holder.request.setText("Request: " +order.getRequest());

        if(!order.isCompletedelivery()){
            holder.layout.setBackgroundResource(R.color.colorred);
        }else {
            holder.layout.setBackgroundResource(R.color.colorgreen);
        }

        publishername(holder.adminname, order.getAdminid());

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                itemClickInterface.OnorderLongClick(order.getCal(),order.getKey(),order.isCompletedelivery());
                return true;
            }
        });

    }

    @Override
    public int getItemCount() {
        return orderLists.size();
    }

    public class OrderViewHolder extends RecyclerView.ViewHolder {
        private TextView productcode, category, quantity, productcost, deliverycost, totalcost, totalpaid, totaldue,
                customername, phoneno, area, date, request, adminname;
        private ConstraintLayout layout;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            productcode = itemView.findViewById(R.id.ordercodeET);
            category = itemView.findViewById(R.id.productcategory);
            quantity = itemView.findViewById(R.id.orderquantityET);
            productcost = itemView.findViewById(R.id.ordercostET);
            deliverycost = itemView.findViewById(R.id.deliverycostET);
            totalcost = itemView.findViewById(R.id.ordertotalcostET);
            totalpaid = itemView.findViewById(R.id.totalpaidET);
            totaldue = itemView.findViewById(R.id.totaldueET);
            customername = itemView.findViewById(R.id.customername);
            phoneno = itemView.findViewById(R.id.customerphoneno);
            area = itemView.findViewById(R.id.customerarea);
            date = itemView.findViewById(R.id.orderdate);
            request = itemView.findViewById(R.id.orderrequestET);
            adminname = itemView.findViewById(R.id.publishbyadmin);
            layout=itemView.findViewById(R.id.layout);
        }
    }

    private void publishername(final TextView adminname, String adminid) {

        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Admin").child(adminid);
        databaseReference.keepSynced(true);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Admin admin = dataSnapshot.getValue(Admin.class);
                adminname.setText("Order Received by ... " + admin.getFullname());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
