package com.universidad.bluenet.opencvagain;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Pc on 7/12/2018.
 */

public class AdapterRecognation extends RecyclerView.Adapter<AdapterRecognation.myViewHolder> {
    Context mContext;
    List<itemPersonal> mData;

    public AdapterRecognation(Context mContext, List<itemPersonal> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View v = inflater.inflate(R.layout.card_item, parent, false);
        return new myViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull myViewHolder holder, int position) {
        /*//Drawable v = mContext.getResources().getDrawable(mData.get(position).getBackground());
        Glide.with(mContext).load(mData.get(position).getBackground()).into(holder.background_img);
        //holder.background_img.setBackgroundResource(mData.get(position).getBackground());
        //holder.background_img.setImageResource(mData.get(position).getBackground());
        Glide.with(mContext).load(mData.get(position).getProfilePhoto()).into(holder.profile_photo);
        //holder.profile_photo.setImageResource(mData.get(position).getProfilePhoto());
        holder.tx_title.setText(mData.get(position).getProfile_name());
        holder.tx_nbFollowers.setText(mData.get(position).getNbFollowers() + " Followers");*/
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class myViewHolder extends RecyclerView.ViewHolder{
        ImageView profile_photo;
        TextView tx_title;
        public myViewHolder(View itemView) {
            super(itemView);
            /*profile_photo = itemView.findViewById(R.id.profile_img);
            background_img = itemView.findViewById(R.id.card_background);
            tx_title = itemView.findViewById(R.id.card_title);
            tx_nbFollowers = itemView.findViewById(R.id.card_nb_follower);*/
        }
    }
}
