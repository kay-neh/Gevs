package com.example.gevs.ui.user.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gevs.R;
import com.example.gevs.data.pojo.Voter;

import java.util.List;

public class VoterAdapter extends RecyclerView.Adapter<VoterAdapter.VoterViewHolder> {

    List<Voter> voterList;

    @NonNull
    @Override
    public VoterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.list_item_voters, parent, false);
        return new VoterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VoterViewHolder holder, int position) {
        if (voterList != null) {
            Voter currentVoter = voterList.get(position);
            holder.voterName.setText(currentVoter.getFullName());
            holder.voterConstituency.setText(currentVoter.getConstituency());
        }
    }

    @Override
    public int getItemCount() {
        if (voterList != null) {
            return voterList.size();
        } else {
            return 0;
        }
    }

    public void setList(List<Voter> voterList) {
        this.voterList = voterList;
        notifyDataSetChanged();
    }

    class VoterViewHolder extends RecyclerView.ViewHolder {

        TextView voterName, voterConstituency;

        public VoterViewHolder(@NonNull View itemView) {
            super(itemView);
            voterName = itemView.findViewById(R.id.voter_name_textview);
            voterConstituency = itemView.findViewById(R.id.voter_constituency_textview);

        }
    }

}
