package com.example.gevs.ui.user.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gevs.R;
import com.example.gevs.data.pojo.Vote;
import com.example.gevs.util.Constants;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class VoteAdapter extends RecyclerView.Adapter<VoteAdapter.VoteViewHolder> {

    List<Vote> voteList;

    public void setList(List<Vote> voteList) {
        this.voteList = voteList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public VoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.list_item_live_update, parent, false);
        return new VoteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VoteViewHolder holder, int position) {
        if (voteList != null) {
            Vote currentVote = voteList.get(position);
            holder.voterName.setText(currentVote.getName());
            holder.voterConstituency.setText(currentVote.getConstituency());

            DateFormat df = new SimpleDateFormat("hh:mm a", Locale.ENGLISH);
            String voteTime = df.format(currentVote.getVoteTime());
            holder.voteTime.setText(voteTime);

            if (currentVote.getParty().equals(Constants.PARTY_BLUE)) {
                holder.votedParty.setImageResource(R.drawable.blue);
            } else if (currentVote.getParty().equals(Constants.PARTY_RED)) {
                holder.votedParty.setImageResource(R.drawable.red);
            } else if (currentVote.getParty().equals(Constants.PARTY_YELLOW)) {
                holder.votedParty.setImageResource(R.drawable.yellow);
            } else {
                holder.votedParty.setImageResource(R.drawable.independent);
            }
        }
    }

    @Override
    public int getItemCount() {
        if (voteList != null) {
            return voteList.size();
        } else {
            return 0;
        }
    }

    class VoteViewHolder extends RecyclerView.ViewHolder {
        TextView voterName, voterConstituency, voteTime;
        ImageView votedParty;

        public VoteViewHolder(@NonNull View itemView) {
            super(itemView);
            voterName = itemView.findViewById(R.id.admin_voter_name);
            voterConstituency = itemView.findViewById(R.id.admin_voter_constituency);
            voteTime = itemView.findViewById(R.id.admin_voter_vote_time);
            votedParty = itemView.findViewById(R.id.admin_voter_party_imageView);
        }
    }

}
