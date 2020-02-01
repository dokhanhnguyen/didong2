package vn.dangyen.onthitracnghiem.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;

import cn.iwgang.countdownview.CountdownView;
import vn.dangyen.onthitracnghiem.R;
import vn.dangyen.onthitracnghiem.activity.ExamActivity;
import vn.dangyen.onthitracnghiem.adapter.SubjectAdapter;
import vn.dangyen.onthitracnghiem.common.Common;
import vn.dangyen.onthitracnghiem.helper.IOHelper;
import vn.dangyen.onthitracnghiem.model.Subject;

public class HomeFragment extends Fragment {

    public static final String EXTRA_RAW_NAME = "extraRawID";
    public static final String EXTRA_RAW_INFO = "extraRawINFO";
    public static final String EXTRA_STRING_ICON = "extraStringIcon";


    private ArrayList<Subject> mSubjects = new ArrayList<>();
    private  GridView gridSubject;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //đếm ngược thời gian
        long currentTime = System.currentTimeMillis();
        //cần convert sang timetoUnix
        long timeInMillis = Common.timeToUnix(1590796800);

        if (getActivity() != null) {
            CountdownView countdownView = getActivity().findViewById(R.id.countdownView);
            countdownView.start(timeInMillis - currentTime);
            gridSubject = getActivity().findViewById(R.id.gridSubject);
        }

        //class đọc dữ liệu từ file json
        IOHelper helper = new IOHelper(getActivity());
        try {
            //đọc vào mảng mSubjects
            mSubjects = helper.getSubject();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //danh sách môn học
        SubjectAdapter adapter = new SubjectAdapter(getActivity(), mSubjects);
        gridSubject.setAdapter(adapter);

        gridSubject.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //nếu mSrcExam.equals rỗng: chưa có dữ liệu
                if (mSubjects.get(position).mSrcExam.equals("")){
                    Toast.makeText(getActivity(), "Chưa có dữ liệu", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(getActivity(), ExamActivity.class);
                    intent.putExtra(EXTRA_RAW_NAME, mSubjects.get(position).mSrcExam);
                    intent.putExtra(EXTRA_STRING_ICON, mSubjects.get(position).mIcon);
                    intent.putExtra(EXTRA_RAW_INFO, mSubjects.get(position).mInfo);
                    startActivity(intent);
                }
            }
        });
    }
}
