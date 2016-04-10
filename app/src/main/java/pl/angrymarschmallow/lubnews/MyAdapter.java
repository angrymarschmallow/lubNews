package pl.angrymarschmallow.lubnews;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Mazek27 on 2016-04-10.
 */
public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder>{
    public ArrayList<ArrayList<String>> mDataset;


    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        //public TextView mTextView;
        public LinearLayout element;
        public ImageView hashtag;
        public TextView hashtagText;


        public MyViewHolder(View view, final ArrayList<ArrayList<String>> mDataset) {
            super(view);
            element = (LinearLayout) view.findViewById(R.id.element);
            hashtag = (ImageView) view.findViewById(R.id.hashtag);
            hashtagText = (TextView) view.findViewById(R.id.hashtagText);

            element.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(), ArticleActivity.class);
                    intent.putExtra("tag", mDataset.get(getLayoutPosition()).get(0)); //getLayoutPosition()
                    v.getContext().startActivity(intent);
                }
            });
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public MyAdapter(ArrayList<ArrayList<String>> myDataset) {
        this.mDataset = myDataset;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        // create a new view
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_item, parent, false);
        return new MyViewHolder(itemView, mDataset);
    }



    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        ArrayList<ArrayList<String>> mData = mDataset;

        holder.hashtag.setImageResource(R.drawable.hashtag);
        holder.hashtagText.setText(mData.get(position).get(4));


    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        Log.d("wielkosc " , mDataset.size() + "");
        return mDataset.size();
    }
}
