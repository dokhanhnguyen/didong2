package vn.dangyen.onthitracnghiem.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Locale;

import io.github.kexanie.library.MathView;
import vn.dangyen.onthitracnghiem.R;
import vn.dangyen.onthitracnghiem.activity.QuizActivity;
import vn.dangyen.onthitracnghiem.common.Common;
import vn.dangyen.onthitracnghiem.model.Question;

public class ViewQuestionMathAdapter extends PagerAdapter {

    private ArrayList<Question> mQuestions;
    private LayoutInflater inflater;
    private Context mContext;

    public ViewQuestionMathAdapter(Context context, ArrayList<Question> questions){
        this.mQuestions = questions;
        this.mContext = context;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return mQuestions.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, final int position) {
        View view = inflater.inflate(R.layout.view_question_math, container, false);
        container.addView(view);

        final TextView tvQMCount = view.findViewById(R.id.tvQMCount);
        tvQMCount.setText(String.format(Locale.getDefault(), "Câu %d/%d", position + 1, mQuestions.size()));

        final LinearLayout layoutQMImg = view.findViewById(R.id.layoutQMImg);
        final ImageView imgQMQues = view.findViewById(R.id.imgQMQues);
        if (mQuestions.get(position).mImg == null){
            layoutQMImg.setVisibility(View.GONE);
        } else {
            layoutQMImg.setVisibility(View.VISIBLE);
            imgQMQues.setImageBitmap(Common.getBitmapFromAssets(mContext, mQuestions.get(position).mImg));
            imgQMQues.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Common.showImageDialog(mContext, mQuestions.get(position).mImg);
                }
            });
        }

        final String question, ansA, ansB, ansC, ansD;
        question = mQuestions.get(position).mQues;
        ansA = mQuestions.get(position).mAnsA;
        ansB = mQuestions.get(position).mAnsB;
        ansC = mQuestions.get(position).mAnsC;
        ansD = mQuestions.get(position).mAnsD;

        final MathView viewQM = view.findViewById(R.id.viewQM);
        viewQM.config(
                "MathJax.Hub.Config({\n"+
                        "  CommonHTML: { linebreaks: { automatic: true } },\n"+
                        "  \"HTML-CSS\": { linebreaks: { automatic: true } },\n"+
                        "         SVG: { linebreaks: { automatic: true } }\n"+
                        "});");
        viewQM.setText(String.format("%s\n<br/><br/>%s\n<br/><br/>%s\n<br/><br/>%s\n<br/><br/>%s", question, ansA, ansB, ansC, ansD));

        final TextView tvA, tvB, tvC, tvD, tvQMStatus;
        tvA = view.findViewById(R.id.tvA);
        tvB = view.findViewById(R.id.tvB);
        tvC = view.findViewById(R.id.tvC);
        tvD = view.findViewById(R.id.tvD);
        tvQMStatus = view.findViewById(R.id.tvQMStatus);

        if (mQuestions.get(position).mChoice == 1)
            tvA.setBackgroundResource(R.drawable.tv_circle_choice);
        else if (mQuestions.get(position).mChoice == 2)
            tvB.setBackgroundResource(R.drawable.tv_circle_choice);
        else if (mQuestions.get(position).mChoice == 3)
            tvC.setBackgroundResource(R.drawable.tv_circle_choice);
        else if (mQuestions.get(position).mChoice == 4)
            tvD.setBackgroundResource(R.drawable.tv_circle_choice);
        else {
            tvA.setBackgroundResource(R.drawable.tv_circle_normal);
            tvB.setBackgroundResource(R.drawable.tv_circle_normal);
            tvC.setBackgroundResource(R.drawable.tv_circle_normal);
            tvD.setBackgroundResource(R.drawable.tv_circle_normal);
        }

        if (!QuizActivity.checkResult) {
            tvA.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mQuestions.get(position).mChoice = 1;
                    tvA.setBackgroundResource(R.drawable.tv_circle_choice);
                    tvB.setBackgroundResource(R.drawable.tv_circle_normal);
                    tvC.setBackgroundResource(R.drawable.tv_circle_normal);
                    tvD.setBackgroundResource(R.drawable.tv_circle_normal);
                }
            });

            tvB.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mQuestions.get(position).mChoice = 2;
                    tvB.setBackgroundResource(R.drawable.tv_circle_choice);
                    tvA.setBackgroundResource(R.drawable.tv_circle_normal);
                    tvC.setBackgroundResource(R.drawable.tv_circle_normal);
                    tvD.setBackgroundResource(R.drawable.tv_circle_normal);
                }
            });

            tvC.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mQuestions.get(position).mChoice = 3;
                    tvC.setBackgroundResource(R.drawable.tv_circle_choice);
                    tvA.setBackgroundResource(R.drawable.tv_circle_normal);
                    tvB.setBackgroundResource(R.drawable.tv_circle_normal);
                    tvD.setBackgroundResource(R.drawable.tv_circle_normal);
                }
            });


            tvD.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mQuestions.get(position).mChoice = 4;
                    tvD.setBackgroundResource(R.drawable.tv_circle_choice);
                    tvA.setBackgroundResource(R.drawable.tv_circle_normal);
                    tvB.setBackgroundResource(R.drawable.tv_circle_normal);
                    tvC.setBackgroundResource(R.drawable.tv_circle_normal);
                }
            });
        } else {
            tvQMStatus.setVisibility(View.VISIBLE);

            if (mQuestions.get(position).mChoice == 0){
                tvQMStatus.setText("Chưa trả lời");
            } else if (mQuestions.get(position).mChoice == mQuestions.get(position).mAnsTrue){
                tvQMStatus.setText("Đúng");
            } else {
                tvQMStatus.setText("Sai");
            }

            if (mQuestions.get(position).mAnsTrue == 1)
                tvA.setBackgroundResource(R.drawable.tv_circle_true);
            else if (mQuestions.get(position).mAnsTrue == 2)
                tvB.setBackgroundResource(R.drawable.tv_circle_true);
            else if (mQuestions.get(position).mAnsTrue == 3)
                tvC.setBackgroundResource(R.drawable.tv_circle_true);
            else if (mQuestions.get(position).mAnsTrue == 4)
                tvD.setBackgroundResource(R.drawable.tv_circle_true);
        }

        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        View view = (View) object;
        container.removeView(view);
    }
}
