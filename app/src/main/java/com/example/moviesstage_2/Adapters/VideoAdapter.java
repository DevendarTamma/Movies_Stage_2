package com.example.moviesstage_2.Adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.example.moviesstage_2.R;
import com.example.moviesstage_2.VideoModel;


public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.VideoInfo> {
    private VideoModel[] list;
    private Context context;
    public VideoAdapter(Context context, VideoModel[] list) {
        this.list = list;
        this.context=context;
    }


    @NonNull
    @Override
    public VideoAdapter.VideoInfo onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v= LayoutInflater.from(context).inflate(R.layout.one_video,viewGroup,false);
        return new VideoInfo(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VideoAdapter.VideoInfo videoInfo, int i) {
        videoInfo.videoTittle.setText(list[i].getName());
    }

    @Override
    public int getItemCount() {
        if(list!=null)
            return list.length;
        else return 0;
    }
    class VideoInfo extends  RecyclerView.ViewHolder{
        TextView videoTittle;
        public VideoInfo(@NonNull View itemView) {
            super(itemView);
            videoTittle=itemView.findViewById(R.id.video_tittle);
            videoTittle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    String key = list[pos].getKey();
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v="+key));
                    context.startActivity(intent);
                }
            });
        }
    }
}
