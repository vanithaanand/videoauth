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
 * Created by snapooh on 23/9/15.
 */
public class PreviewAdapter extends RecyclerView.Adapter<PreviewAdapter.ViewHolder> {

    private final List<Media> result;
    private final Context context;
    private OnItemClickListener onItemClickListener;

    public PreviewAdapter(Context context,List<Media> result)
    {
        this.context=context;
        this.result=result;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_item, parent, false);
        return new ViewHolder(view);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.getmImageView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener != null) {
                    onItemClickListener.onClick(v, position);
                }
            }
        });
        Media media=result.get(position);
        Glide.with(context).load(media.getTpath()).into(holder.getmImageView());
    }

    @Override
    public int getItemCount() {
        return result.size();
    }

    public interface OnItemClickListener {
        void onClick(View v, int p);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final ImageView mImageView;

        public ViewHolder(View itemView) {
            super(itemView);
            mImageView = (ImageView) itemView.findViewById(R.id.my_image_preview);

        }

        public ImageView getmImageView() {
            return mImageView;
        }

        @Override
        public void onClick(View v) {

            if (onItemClickListener != null) {
                onItemClickListener.onClick(v, getPosition());
            }
        }
    }
}






