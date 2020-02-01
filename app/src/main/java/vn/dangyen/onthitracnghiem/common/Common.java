package vn.dangyen.onthitracnghiem.common;

import android.app.Dialog;
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.github.chrisbanes.photoview.PhotoView;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import vn.dangyen.onthitracnghiem.R;
import vn.dangyen.onthitracnghiem.model.HighScore;
import vn.dangyen.onthitracnghiem.model.HighScoreImport;

public class Common {
    public static int getId(String resourceName, Class<?> c) {
        try {
            Field idField = c.getDeclaredField(resourceName);
            return idField.getInt(idField);
        } catch (Exception e) {
            throw new RuntimeException("No resource ID found for: "
                    + resourceName + " / " + c, e);
        }
    }

    public static Bitmap getBitmapFromAssets(Context context, String fileName){
        AssetManager assetManager = context.getAssets();
        InputStream is = null;
        try {
            is = assetManager.open(fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return BitmapFactory.decodeStream(is);
    }

    public static String getChoiceText(int choice){
        String choiceText;
        if (choice == 1)
            choiceText = "A";
        else if (choice == 2)
            choiceText = "B";
        else if (choice == 3)
            choiceText = "C";
        else if (choice == 4)
            choiceText = "D";
        else
            choiceText = "-";
        return choiceText;
    }

    public static String unixTimeToDate(long unixTime){
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        Date date = new Date();
        date.setTime(unixTime);
        return dateFormat.format(date);
    }

    public static String unixTimeToTime(long unixTime){
        DateFormat dateFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
        Date date = new Date();
        date.setTime(unixTime);
        return dateFormat.format(date);
    }

    public static long timeToUnix(double timeInSeconds){
        return (long) (timeInSeconds * 1000);
    }

    public static void showDialogHighScore(Context context, ArrayList<HighScore> highScores, int position){
        final Dialog dialog = new Dialog(context);
        if (dialog.getWindow() != null) {
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
        dialog.setContentView(R.layout.dialog_high_score);
        TextView tvSDate = dialog.findViewById(R.id.tvSDate);
        tvSDate.setText(String.format("Ngày thi: %s (%s)",
                Common.unixTimeToDate(highScores.get(position).mDate),
                Common.unixTimeToTime(highScores.get(position).mDate)));
        TextView tvSExam = dialog.findViewById(R.id.tvSExam);
        tvSExam.setText(String.format(Locale.getDefault(), "Đề số: %d", highScores.get(position).mExamNum));
        TextView tvSTrue = dialog.findViewById(R.id.tvSTrue);
        tvSTrue.setText(String.format(Locale.getDefault(), "Đúng: %d", highScores.get(position).mCorrect));
        TextView tvSFalse = dialog.findViewById(R.id.tvSFalse);
        tvSFalse.setText(String.format(Locale.getDefault(), "Sai: %d",
                highScores.get(position).mSumQues - highScores.get(position).mCorrect - highScores.get(position).mUnAns));
        TextView tvSUnAns = dialog.findViewById(R.id.tvSUnAns);
        tvSUnAns.setText(String.format(Locale.getDefault(), "Chưa trả lời: %d", highScores.get(position).mUnAns));
        TextView tvSScore = dialog.findViewById(R.id.tvSScore);
        tvSScore.setText(String.valueOf(highScores.get(position).mScore));
        Button btnDLClose = dialog.findViewById(R.id.btnDLClose);
        btnDLClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });
        dialog.show();
    }

    public static void showDialogHighScoreImport(Context context, ArrayList<HighScoreImport> highScores, int position){
        final Dialog dialog = new Dialog(context);
        if (dialog.getWindow() != null) {
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
        dialog.setContentView(R.layout.dialog_high_score);
        TextView tvSDate = dialog.findViewById(R.id.tvSDate);
        tvSDate.setText(String.format("Ngày thi: %s (%s)",
                Common.unixTimeToDate(highScores.get(position).mDate),
                Common.unixTimeToTime(highScores.get(position).mDate)));
        TextView tvSExam = dialog.findViewById(R.id.tvSExam);
        tvSExam.setText(String.format(Locale.getDefault(), "Đề số: %d", highScores.get(position).mExamNum));
        TextView tvSTrue = dialog.findViewById(R.id.tvSTrue);
        tvSTrue.setText(String.format(Locale.getDefault(), "Đúng: %d", highScores.get(position).mCorrect));
        TextView tvSFalse = dialog.findViewById(R.id.tvSFalse);
        tvSFalse.setText(String.format(Locale.getDefault(), "Sai: %d",
                highScores.get(position).mSumQues - highScores.get(position).mCorrect - highScores.get(position).mUnAns));
        TextView tvSUnAns = dialog.findViewById(R.id.tvSUnAns);
        tvSUnAns.setText(String.format(Locale.getDefault(), "Chưa trả lời: %d", highScores.get(position).mUnAns));
        TextView tvSScore = dialog.findViewById(R.id.tvSScore);
        tvSScore.setText(String.valueOf(highScores.get(position).mScore));
        Button btnDLClose = dialog.findViewById(R.id.btnDLClose);
        btnDLClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });
        dialog.show();
    }

    public static void showImageDialog(Context context, String fileName){
        final Dialog dialog = new Dialog(context);
        if (dialog.getWindow() != null) {
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
        dialog.setContentView(R.layout.show_image);
        PhotoView imgQuesShow = dialog.findViewById(R.id.imgQuesShow);
        imgQuesShow.setImageBitmap(Common.getBitmapFromAssets(context, fileName));
        dialog.show();
    }
}
