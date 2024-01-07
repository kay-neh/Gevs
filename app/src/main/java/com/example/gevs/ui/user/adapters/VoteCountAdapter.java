package com.example.gevs.ui.user.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.gevs.R;
import com.example.gevs.data.pojo.DistrictVoteCount;
import com.example.gevs.data.pojo.VoteCount;

import java.util.List;

public class VoteCountAdapter extends RecyclerView.Adapter<VoteCountAdapter.VoteCountViewHolder> {

    List<DistrictVoteCount> voteCountList;

    public void setList(List<DistrictVoteCount> voteCountList) {
        this.voteCountList = voteCountList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public VoteCountViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(R.layout.list_item_vote_count, parent, false);
        return new VoteCountViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VoteCountViewHolder holder, int position) {
        if (voteCountList != null) {
            DistrictVoteCount voteCount = voteCountList.get(position);
            holder.candidateName.setText(voteCount.getName());
            holder.candidateParty.setText(voteCount.getParty());
            holder.candidateVoteCount.setText(String.valueOf(voteCount.getVote()));
            Glide.with(holder.candidateImage.getContext()).load(voteCount.getPhoto()).into(holder.candidateImage);
        }
    }

    @Override
    public int getItemCount() {
        if (voteCountList != null)
            return voteCountList.size();
        else return 0;
    }

    class VoteCountViewHolder extends RecyclerView.ViewHolder {
        TextView candidateName, candidateParty, candidateVoteCount;
        ImageView candidateImage;

        public VoteCountViewHolder(@NonNull View itemView) {
            super(itemView);
            candidateName = itemView.findViewById(R.id.candidate_name);
            candidateParty = itemView.findViewById(R.id.candidate_party);
            candidateVoteCount = itemView.findViewById(R.id.vote_count);
            candidateImage = itemView.findViewById(R.id.candidate_imageView);
        }
    }

}
