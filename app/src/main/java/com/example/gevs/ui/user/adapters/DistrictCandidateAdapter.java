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
import com.example.gevs.data.pojo.Candidate;
import com.example.gevs.util.Constants;

import java.util.List;

public class DistrictCandidateAdapter extends RecyclerView.Adapter<DistrictCandidateAdapter.DistrictCandidateViewHolder> {

    final private ListItemClickListener mOnclickListener;
    List<Candidate> candidateList;

    public DistrictCandidateAdapter(ListItemClickListener mOnclickListener) {
        this.mOnclickListener = mOnclickListener;
    }

    public void setList(List<Candidate> candidateList) {
        this.candidateList = candidateList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public DistrictCandidateViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.list_item_voter_candidates, parent, false);
        return new DistrictCandidateViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DistrictCandidateViewHolder holder, int position) {
        if (candidateList != null) {
            Candidate candidate = candidateList.get(position);
            holder.candidateName.setText(candidate.getName());
            holder.candidateParty.setText(candidate.getParty());
            Glide.with(holder.candidatePhoto.getContext()).load(candidate.getPhoto()).into(holder.candidatePhoto);

            if (candidate.getParty().equals(Constants.PARTY_BLUE)) {
                holder.partyImage.setImageResource(R.drawable.blue);
            } else if (candidate.getParty().equals(Constants.PARTY_RED)) {
                holder.partyImage.setImageResource(R.drawable.red);
            } else if (candidate.getParty().equals(Constants.PARTY_YELLOW)) {
                holder.partyImage.setImageResource(R.drawable.yellow);
            } else {
                holder.partyImage.setImageResource(R.drawable.independent);
            }
        }
    }

    @Override
    public int getItemCount() {
        if (candidateList != null)
            return candidateList.size();
        else return 0;
    }

    public interface ListItemClickListener {
        void onListItemClick(String key);
    }

    class DistrictCandidateViewHolder extends RecyclerView.ViewHolder {
        TextView candidateName, candidateParty;
        ImageView candidatePhoto, partyImage;

        public DistrictCandidateViewHolder(@NonNull View itemView) {
            super(itemView);
            candidateName = itemView.findViewById(R.id.voter_candidate_name);
            candidateParty = itemView.findViewById(R.id.voter_candidate_constituency);
            candidatePhoto = itemView.findViewById(R.id.voter_candidate_imageView);
            partyImage = itemView.findViewById(R.id.voter_candidate_party_imageView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int index = getAdapterPosition();
                    mOnclickListener.onListItemClick(candidateList.get(index).getName());
                }
            });
        }
    }

}
