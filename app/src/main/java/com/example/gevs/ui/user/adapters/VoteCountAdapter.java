package com.example.gevs.ui.user.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.gevs.R;
import com.example.gevs.data.pojo.DistrictVoteCount;

import java.util.HashMap;
import java.util.List;

public class VoteCountAdapter extends BaseExpandableListAdapter {

    private Context context;
    private List<String> expandableListTitle;
    private HashMap<String, List<DistrictVoteCount>> expandableListDetail;

    public VoteCountAdapter(Context context, List<String> expandableListTitle, HashMap<String, List<DistrictVoteCount>> expandableListDetail) {
        this.context = context;
        this.expandableListTitle = expandableListTitle;
        this.expandableListDetail = expandableListDetail;
    }

    @Override
    public int getGroupCount() {
        return expandableListTitle.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return expandableListDetail.get(expandableListTitle.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return expandableListTitle.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return expandableListDetail.get(expandableListTitle.get(groupPosition)).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        String listTitle = (String) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context.
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.list_item_group_constituency, null);
        }

        TextView constituency = convertView.findViewById(R.id.result_constituency);
        constituency.setText(listTitle);

        ExpandableListView eLV = (ExpandableListView) parent;
        eLV.expandGroup(groupPosition, true);

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        final DistrictVoteCount expandedDistrictVoteCount = (DistrictVoteCount) getChild(groupPosition, childPosition);
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.list_item_vote_count, null);
        }

        ImageView candidateImage = convertView.findViewById(R.id.candidate_imageView);
        TextView candidateName = convertView.findViewById(R.id.candidate_name);
        TextView candidateParty = convertView.findViewById(R.id.candidate_party);
        TextView candidateVoteCount = convertView.findViewById(R.id.vote_count);

        Glide.with(candidateImage.getContext()).load(expandedDistrictVoteCount.getPhoto()).into(candidateImage);
        candidateName.setText(expandedDistrictVoteCount.getName());
        candidateParty.setText(expandedDistrictVoteCount.getParty());
        candidateVoteCount.setText(String.valueOf(expandedDistrictVoteCount.getVote()));
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }

}
