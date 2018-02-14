package com.crocodile.quiz.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.crocodile.quiz.R;
import com.crocodile.quiz.activity.QuestionActivity;
import com.crocodile.quiz.helper.DownloadHelper;
import com.crocodile.quiz.model.Topic;

import java.io.InputStream;
import java.util.List;

import static android.app.PendingIntent.getActivity;


public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.ViewHolder> {
    private String[] mDataset;

    private List<Topic> topics;
    private int topicLayout;
    private Context context;

    public static class ViewHolder extends RecyclerView.ViewHolder  implements DownloadHelper.OnImageDownloadListener{

        public RelativeLayout mRelativeLayout;
        public TextView mTextView;
        public ImageView mImageView;

        public ViewHolder(View v) {
            super(v);
            mRelativeLayout = (RelativeLayout) v.findViewById(R.id.relativelayout);
            mImageView = (ImageView) v.findViewById(R.id.imageview);
            mTextView = (TextView) v.findViewById(R.id.textview);

        }

        @Override
        public void onImageDownloaded(Bitmap image) {
            mImageView.setImageBitmap(image);
        }
    }

    public MenuAdapter(List<Topic> topics, int topicLayout, Context context) {
        this.topics = topics;
        this.topicLayout = topicLayout;
        this.context = context;
    }

    @Override
    public MenuAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                     int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(topicLayout, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Topic topic = topics.get(position);
        holder.mTextView.setText(topic.getTitle());


        holder.mRelativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), QuestionActivity.class);
                intent.putExtra("name",topic.getTitle());
                intent.putExtra("id", topic.get_id());
                v.getContext().startActivity(intent);
            }
        });

        DownloadHelper.downloadImage(topic.getImageUrl(),holder);
    }


    @Override
    public int getItemCount() {
        return topics.size();
    }
}
