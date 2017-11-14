package com.srp.rkim.adiona;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

/**
 * Created by 2018rkim on 11/9/2017.
 */
public class TrackeeAdapter extends
        RecyclerView.Adapter<TrackeeAdapter.ViewHolder> {

    private List<TrackeeModel> mTrackees;
    // Store the context for easy access
    private Context mContext;

    // Pass in the contact array into the constructor
    public TrackeeAdapter(Context context, List<TrackeeModel> trackees) {
        mTrackees = trackees;
        mContext = context;
    }

    // Easy access to the context object in the recyclerview
    private Context getContext() {
        return mContext;
    }

    @Override
    public TrackeeAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View trackeeView = inflater.inflate(R.layout.item_trackee, parent, false);

        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(trackeeView);
        return viewHolder;
    }

    // Involves populating data into the item through holder
    @Override
    public void onBindViewHolder(TrackeeAdapter.ViewHolder viewHolder, int position) {
        // Get the data model based on position
        TrackeeModel trackee = mTrackees.get(position);

        // Set item views based on your views and data model
        TextView nameView = viewHolder.nameTextView;
        nameView.setText(trackee.getTrackeeName());

        TextView syncView = viewHolder.syncTextView;
        syncView.setText(trackee.getTime().toString());

        Button eButton = viewHolder.editButton;
        Button dButton = viewHolder.deleteButton;
        //button.setText(trackee.isOnline() ? "Message" : "Offline");
        //button.setEnabled(trackee.isOnline());
    }

    // Returns the total count of items in the list
    @Override
    public int getItemCount() {
        return mTrackees.size();
    }

    // Provide a direct reference to each of the views within a data item
    // Used to cache the views within the item layout for fast access
    public class ViewHolder extends RecyclerView.ViewHolder {
        // Your holder should contain a member variable
        // for any view that will be set as you render a row
        public TextView nameTextView;
        public TextView syncTextView;
        public Button editButton;
        public Button deleteButton;

        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        public ViewHolder(View itemView) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);

            nameTextView = itemView.findViewById(R.id.trackee_name);
            syncTextView = itemView.findViewById(R.id.trackee_sync);
            editButton = itemView.findViewById(R.id.edit_trackee_button);
            deleteButton = itemView.findViewById(R.id.delete_trackee_button);
        }
    }
}

