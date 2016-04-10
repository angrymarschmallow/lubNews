package pl.angrymarschmallow.lubnews;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Mazek27 on 2016-04-10.
 */
public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
    private String[][] mDataset;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        //public TextView mTextView;
        public TextView titleArticle;
        public TextView timeArticle;
        public TextView url;
        public TextView categoryArticle;
        public TextView description;

        public MyViewHolder(View view) {
            super(view);
            titleArticle = (TextView) view.findViewById(R.id.titleArticle);
            timeArticle = (TextView) view.findViewById(R.id.timeArticle);
            url = (TextView) view.findViewById(R.id.url);
            categoryArticle = (TextView) view.findViewById(R.id.categoryArticle);
            description = (TextView) view.findViewById(R.id.description);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public MyAdapter(String[][] myDataset) {
        mDataset = myDataset;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        // create a new view
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_item, parent, false);
        return new MyViewHolder(itemView);
    }



    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.titleArticle.setText(mDataset[position][0]);
        holder.timeArticle.setText(mDataset[position][1]);
        holder.url.setText(mDataset[position][2]);
        holder.categoryArticle.setText(mDataset[position][3]);
        holder.description.setText(mDataset[position][4]);


    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.length;
    }
}
