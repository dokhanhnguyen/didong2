package vn.dangyen.onthitracnghiem.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import vn.dangyen.onthitracnghiem.R;
import vn.dangyen.onthitracnghiem.common.Common;
import vn.dangyen.onthitracnghiem.model.HighScore;

public class ScoreAdapter extends RecyclerView.Adapter<ScoreAdapter.ViewHolder>{

    private Context mContext;
    private ArrayList<HighScore> mHighScores;

    public ScoreAdapter(Context context, ArrayList<HighScore> highScores) {
        this.mContext = context;
        this.mHighScores = highScores;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_score, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        final HighScore highScore = mHighScores.get(position);
        if (mHighScores.get(0).mRawName != null) {
            holder.tvItemSScore.setVisibility(View.VISIBLE);
            holder.tvItemSClassification.setVisibility(View.VISIBLE);
            holder.tvItemSDate.setText(String.format("%s\n%s",
                    Common.unixTimeToDate(highScore.mDate),
                    Common.unixTimeToTime(highScore.mDate)));
            holder.tvItemSScore.setText(String.valueOf(highScore.mScore));

            holder.layout_item_score.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Common.showDialogHighScore(mContext, mHighScores, position);
                }
            });
        } else {
            holder.tvItemSDate.setText("Chưa có dữ liệu");
            holder.tvItemSScore.setVisibility(View.GONE);
            holder.tvItemSClassification.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return mHighScores.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{

        TextView tvItemSDate, tvItemSScore, tvItemSClassification;
        LinearLayout layout_item_score;

        ViewHolder(View itemView) {
            super(itemView);
            tvItemSDate = itemView.findViewById(R.id.tvItemSDate);
            tvItemSScore = itemView.findViewById(R.id.tvItemSScore);
            tvItemSClassification = itemView.findViewById(R.id.tvItemSClassification);
            layout_item_score = itemView.findViewById(R.id.layout_item_score);
        }
    }
}
