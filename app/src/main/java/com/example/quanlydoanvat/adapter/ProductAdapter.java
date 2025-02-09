package com.example.quanlydoanvat.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quanlydoanvat.R;
import com.example.quanlydoanvat.model.Product;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    private List<Product> productList;
    private OnProductClickListener listener;

    public interface OnProductClickListener {
        void onProductClick(Product product);
    }

    public ProductAdapter(List<Product> productList, OnProductClickListener listener) {
        this.productList = productList;
        this.listener = listener;
    }

    public void updateProductList(List<Product> newList) {
        productList = newList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_product, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product product = productList.get(position);
        holder.tvProductName.setText(product.getTenSP());
        holder.tvProductCategory.setText(product.getLoaiSP());
        holder.tvProductPrice.setText(product.getGiaSP() + " VNĐ");
        holder.tvProductQuantity.setText("SL: " + product.getSoLuong());
        holder.tvManufactureDate.setText("NSX: " + product.getNgaySX());
        holder.tvExpiryDate.setText("HSD: " + product.getHanSD());

        holder.itemView.setOnClickListener(v -> listener.onProductClick(product));
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    static class ProductViewHolder extends RecyclerView.ViewHolder {
        TextView tvProductName, tvProductCategory, tvProductPrice, tvProductQuantity, tvManufactureDate, tvExpiryDate;

        ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            tvProductName = itemView.findViewById(R.id.tvProductName);
            tvProductCategory = itemView.findViewById(R.id.tvProductCategory);
            tvProductPrice = itemView.findViewById(R.id.tvProductPrice);
            tvProductQuantity = itemView.findViewById(R.id.tvProductQuantity);
            tvManufactureDate = itemView.findViewById(R.id.tvManufactureDate);
            tvExpiryDate = itemView.findViewById(R.id.tvExpiryDate);
        }
    }
}
