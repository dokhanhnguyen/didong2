package vn.dangyen.onthitracnghiem.adapter;

        import android.content.Context;
        import android.support.annotation.NonNull;
        import android.support.annotation.Nullable;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.ArrayAdapter;
        import android.widget.TextView;

        import java.util.ArrayList;

        import vn.dangyen.onthitracnghiem.R;
        import vn.dangyen.onthitracnghiem.activity.QuizActivity;
        import vn.dangyen.onthitracnghiem.common.Common;
        import vn.dangyen.onthitracnghiem.model.Question;

public class ListQuestionAdapter extends ArrayAdapter<Question> {

    public ListQuestionAdapter(@NonNull Context context, ArrayList<Question> arrQuestion) {
        super(context, 0, arrQuestion);
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null){
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.item_question, parent, false);
        }

        TextView tvItemQuesNum = convertView.findViewById(R.id.tvItemQuesNum);
        TextView tvItemQuesChoice = convertView.findViewById(R.id.tvItemQuesChoice);
        TextView tvItemQuesTrue = convertView.findViewById(R.id.tvItemQuesTrue);

        final Question question = getItem(position);

        if (question != null) {

            tvItemQuesNum.setText(String.valueOf(position+1));
            tvItemQuesChoice.setText(Common.getChoiceText(question.mChoice));

            if (QuizActivity.checkResult){
                tvItemQuesTrue.setVisibility(View.VISIBLE);
                tvItemQuesTrue.setText(Common.getChoiceText(question.mAnsTrue));
                tvItemQuesChoice.setBackgroundResource(R.drawable.tv_circle_true);
                tvItemQuesTrue.setBackgroundResource(R.drawable.tv_circle_choice);
                if (question.mChoice == question.mAnsTrue){
                    tvItemQuesChoice.setBackgroundResource(R.drawable.tv_circle_choice);
                }
            }
        }

        return convertView;
    }
}
