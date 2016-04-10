package pl.angrymarschmallow.lubnews;

import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Mazek27 on 2016-04-10.
 */
public class MyArticleAdapter extends RecyclerView.Adapter<MyArticleAdapter.MyViewHolder>{
    public ArrayList<ArrayList<String>> mDataset;

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        //public TextView mTextView;
        public CardView element;
        public TextView titleArticle;
        public TextView timeArticle;
        public TextView categoryArticle;
        public TextView description;



        public MyViewHolder(View view, final ArrayList<ArrayList<String>> mDataset) {
            super(view);
            element = (CardView) view.findViewById(R.id.card_view);
            titleArticle = (TextView) view.findViewById(R.id.titleArticle);
            timeArticle = (TextView) view.findViewById(R.id.timeArticle);
            categoryArticle = (TextView) view.findViewById(R.id.categoryArticle);
            description = (TextView) view.findViewById(R.id.description);

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

    public MyArticleAdapter(ArrayList<ArrayList<String>> myDataset) {
        this.mDataset = myDataset;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_item, parent, false);
        return new MyViewHolder(itemView, mDataset);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        ArrayList<ArrayList<String>> mData = mDataset;

        holder.titleArticle.setText(mData.get(position).get(4));
        holder.timeArticle.setText(
                mData.get(position).get(1) + "-" +
                mData.get(position).get(2) + "-" +
                mData.get(position).get(3));
        holder.categoryArticle.setText(mData.get(position).get(6));
        holder.description.setText(mData.get(position).get(5));

    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
