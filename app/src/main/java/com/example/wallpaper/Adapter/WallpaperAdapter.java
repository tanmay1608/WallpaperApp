package com.example.wallpaper.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.wallpaper.FullScreenWallpaper;
import com.example.wallpaper.Models.WallpaperModel;
import com.example.wallpaper.R;

import java.util.List;

public class WallpaperAdapter  extends RecyclerView.Adapter<WallpaperViewHolder> {
    private Context context;
    private List<WallpaperModel> wallpaperModelsLists;

    public WallpaperAdapter(Context context, List<WallpaperModel> wallpaperModelsLists) {
        this.context = context;
        this.wallpaperModelsLists = wallpaperModelsLists;
    }

    @NonNull
    @Override
    public WallpaperViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.wallpaper_items,parent,false);

        return new WallpaperViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WallpaperViewHolder holders, int position) {
        final WallpaperViewHolder holder=holders;
        final WallpaperModel model=wallpaperModelsLists.get(position);

        Glide.with(context).load(model.getMediumUrl()).into(holder.imageView);
        holder.photographerName.setText(model.getPhotographerName());
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context, FullScreenWallpaper.class);
                intent.putExtra("originalUrl",model.getOriginalUrl());
                context.startActivity(intent);

            }
        });

    }

    @Override
    public int getItemCount() {
        return wallpaperModelsLists.size();
    }
}
class WallpaperViewHolder extends RecyclerView.ViewHolder{
    ImageView imageView;
    TextView photographerName;

    public WallpaperViewHolder(@NonNull View itemView) {
        super(itemView);
        imageView=itemView.findViewById(R.id.image_view_item);
        photographerName=itemView.findViewById(R.id.Photographer_Name);
    }
}
