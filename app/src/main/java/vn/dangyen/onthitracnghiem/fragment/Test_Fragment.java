package vn.dangyen.onthitracnghiem.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
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

public class Test_Fragment extends Fragment {

    public static final String ID_MA_LOP = "classID";
    public static final String ID_MA_DE = "testID";

    private ArrayList<Subject> mSubjects = new ArrayList<>();
    private GridView gridSubject;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_test, container, false);
        return view;
    }

}
