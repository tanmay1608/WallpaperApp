package com.example.wallpaper.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wallpaper.Interfaces.RecyclerViewIClickListner;
import com.example.wallpaper.Models.SuggestedModel;
import com.example.wallpaper.R;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class suggestedAdapter extends RecyclerView.Adapter<suggestedAdapter.SuggestedViewHolder> {

    ArrayList<SuggestedModel> suggestedModels;
    final private RecyclerViewIClickListner clickListner;
    Context context;

    public suggestedAdapter(ArrayList<SuggestedModel> suggestedModels, RecyclerViewIClickListner clickListner) {
        this.suggestedModels = suggestedModels;
        this.clickListner = clickListner;
       // this.clickListner = clickListner;
    }

    @NonNull
    @Override
    public SuggestedViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.suggested_item_layout,parent,false);
        final SuggestedViewHolder suggestedViewHolder=new SuggestedViewHolder(view);
        return suggestedViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull SuggestedViewHolder holder, int position) {
        SuggestedModel suggestedModel=suggestedModels.get(position);
        holder.image.setImageResource(suggestedModel.getImage());
        holder.title.setText(suggestedModel.getTitle());

    }

    @Override
    public int getItemCount() {
        return suggestedModels.size();
    }

    public class SuggestedViewHolder extends RecyclerView.ViewHolder {
        CircleImageView image;
        TextView title;
        public SuggestedViewHolder(@NonNull View itemView) {
            super(itemView);

            image=itemView.findViewById(R.id.suggested_image);
            title=itemView.findViewById(R.id.suggested_title);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    clickListner.onItemClick(getAdapterPosition());
                }
            });
        }
    }
}
