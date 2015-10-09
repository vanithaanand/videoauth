package com.snapooh.videoauthentication;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.List;

/**
 * Created by snapooh on 2/9/15.
 */
public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder> {
    private final Context context;
    private final List<byte[]> mDataSet;
    private  OnItemClickListener mItemClickListener;
    public CustomAdapter(Context ctx,List<byte[]> mDataSet){
        this.context=ctx;
        this.mDataSet=mDataSet;

    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
       View view= LayoutInflater.from(context).inflate(R.layout.row_item,parent,false);

        return new ViewHolder(view);
    }

    public void setmItemClickListener(OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        Glide.with(context).load(mDataSet.get(position)).into(holder.item);
       // holder.item.setImageBitmap(mDataSet[position]);
    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final ImageView item;

        public ViewHolder(View itemView) {
            super(itemView);
            item=(ImageView)itemView.findViewById(R.id.item);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {


                mItemClickListener.onItemClick(v, getPosition()); //OnItemClickListener mItemClickListener;


        }
    }
    public interface OnItemClickListener {
         void onItemClick(View view , int position);
    }

}
