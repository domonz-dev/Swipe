package com.domonz.swipe;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.io.File;
import java.util.ArrayList;

public class AdapterProductImages extends RecyclerView.Adapter<AdapterProductImages.ViewHolder> {

    Context context;
    public ArrayList<File> files;

    public AdapterProductImages(Context context, ArrayList<File> files) {
        this.context = context;
        this.files = files;
    }

    @NonNull
    @Override
    public AdapterProductImages.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_selected_images, parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterProductImages.ViewHolder holder, @SuppressLint("RecyclerView") int position) {

        Glide.with(context).load(Uri.fromFile(files.get(position))).thumbnail(0.5f).diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.image);

        holder.btnClose.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onClick(View view) {
                files.remove(position);
                notifyDataSetChanged();
            }
        });

    }

    @Override
    public int getItemCount() {
        return files.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageButton btnClose;
        ImageView image;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            btnClose = itemView.findViewById(R.id.btnClose);
            image = itemView.findViewById(R.id.productImage);

        }
    }
}
