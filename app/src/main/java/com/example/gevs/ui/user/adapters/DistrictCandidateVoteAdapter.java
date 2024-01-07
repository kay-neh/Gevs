package com.example.gevs.ui.user.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.gevs.R;
import com.example.gevs.data.pojo.CandidateVote;

import java.util.List;

public class DistrictCandidateVoteAdapter extends RecyclerView.Adapter<DistrictCandidateVoteAdapter.DistrictCandidateVoteViewHolder> {

    final private ListItemClickListener mOnclickListener;
    List<CandidateVote> candidateList;

    public DistrictCandidateVoteAdapter(ListItemClickListener mOnclickListener) {
        this.mOnclickListener = mOnclickListener;
    }

    @NonNull
    @Override
    public DistrictCandidateVoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.list_item_vote_candidate1, parent, false);
        return new DistrictCandidateVoteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DistrictCandidateVoteViewHolder holder, int position) {
        if (candidateList != null) {
            CandidateVote candidate = candidateList.get(position);
            holder.candidateName.setText(candidate.getName());
            holder.candidateParty.setText(candidate.getParty());
            Glide.with(holder.candidatePhoto.getContext()).load(candidate.getPhoto()).into(holder.candidatePhoto);

            // set condition to enable/disable vote button
            if (candidate.getVoted()) {
                holder.voteButton.setEnabled(false);
                if (candidate.getPartyVoted().equals(candidate.getParty())) {
                    holder.voteButton.setVisibility(View.GONE);
                    holder.voteConfirmation.setVisibility(View.VISIBLE);
                } else {
                    holder.voteButton.setVisibility(View.VISIBLE);
                    holder.voteConfirmation.setVisibility(View.GONE);
                }
            } else {
                holder.voteButton.setEnabled(true);
            }

        }
    }

    @Override
    public int getItemCount() {
        if (candidateList != null)
            return candidateList.size();
        else return 0;
    }

    public void setList(List<CandidateVote> candidateList) {
        this.candidateList = candidateList;
        notifyDataSetChanged();
    }

    public interface ListItemClickListener {
        void onListItemClick(String key);
    }

    class DistrictCandidateVoteViewHolder extends RecyclerView.ViewHolder {
        TextView candidateName, candidateParty;
        ImageView candidatePhoto;
        Button voteButton;
        ConstraintLayout voteConfirmation;

        public DistrictCandidateVoteViewHolder(@NonNull View itemView) {
            super(itemView);
            candidateName = itemView.findViewById(R.id.voter_candidate_name);
            candidateParty = itemView.findViewById(R.id.voter_candidate_constituency);
            candidatePhoto = itemView.findViewById(R.id.voter_candidate_imageView);
            voteButton = itemView.findViewById(R.id.vote_button);
            voteConfirmation = itemView.findViewById(R.id.voted_confirmation_view);
            voteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int index = getAdapterPosition();
                    mOnclickListener.onListItemClick(candidateList.get(index).getName());
                }
            });
        }
    }

}
