package com.crocodile.quiz.adapter;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.crocodile.quiz.R;
import com.crocodile.quiz.model.Group;

import java.util.List;


public class GroupAdapter extends RecyclerView.Adapter<GroupAdapter.ViewHolder> {
    private String[] mDataset;

    private List<Group> groups;
    private int groupLayout;
    private Context context;

    public static class ViewHolder extends RecyclerView.ViewHolder{

        public RelativeLayout mRelativeLayout;
        public TextView mTextView;
        public RecyclerView mRecyclerView;
        public RecyclerView.Adapter mAdapter;
        public RecyclerView.LayoutManager mLayoutManager;

        public ViewHolder(View v) {
            super(v);
            mRelativeLayout = (RelativeLayout) v.findViewById(R.id.group_relative_layout);
            mTextView = (TextView) v.findViewById(R.id.group_text_view);
            mRecyclerView = v.findViewById(R.id.topics_recycler);

        }
    }

    public GroupAdapter(List<Group> groups, int groupLayout, Context context) {
        this.groups = groups;
        this.groupLayout = groupLayout;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent,
                                         int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(groupLayout, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Group group = groups.get(position);
        holder.mTextView.setText(group.getTitle());
        holder.mRecyclerView.setHasFixedSize(true);
        holder.mLayoutManager = new GridLayoutManager(context,group.getTopics().size());
        holder.mRecyclerView.setLayoutManager(holder.mLayoutManager);
        holder.mRecyclerView.setAdapter(new MenuAdapter(group.getTopics(), R.layout.menu_item, context));



    }


    @Override
    public int getItemCount() {
        return groups.size();
    }
}
