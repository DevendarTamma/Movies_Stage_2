package com.example.moviesstage_2.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import  com.example.moviesstage_2.R;
import com.example.moviesstage_2.ReviewModel;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewHolder> {
    private ReviewModel[] list;

    public ReviewAdapter(ReviewModel[] list, Context context) {
        this.list = list;
        this.context = context;
    }

    private Context context;
    @NonNull
    @Override
    public ReviewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v= LayoutInflater.from(context).inflate(R.layout.review_item,viewGroup,false);
        return new ReviewHolder(v);
    }
    @Override
    public void onBindViewHolder(@NonNull ReviewHolder reviewHolder, int i) {
        reviewHolder.author.setText(list[i].getAuthor());
        reviewHolder.comment.setText(list[i].getComment());

    }

    @Override
    public int getItemCount() {
        if(list!=null)
            return list.length;
        else return 0;
    }

    public class ReviewHolder extends RecyclerView.ViewHolder {
        TextView author,comment;
        public ReviewHolder(@NonNull View itemView) {
            super(itemView);
            author=itemView.findViewById(R.id.author_view);
            comment=itemView.findViewById(R.id.comment_view);

        }
    }
}
