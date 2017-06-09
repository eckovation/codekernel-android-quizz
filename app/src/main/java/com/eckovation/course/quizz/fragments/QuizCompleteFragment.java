package com.eckovation.course.quizz.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.eckovation.course.quizz.R;

/**
 * Created by codekernel on 08/06/17.
 */

public class QuizCompleteFragment extends Fragment {

    public static final String ARG_MARKS_OBTAINED = "arg_marks_obtained";
    public static final String ARG_MARKS_TOTAL = "arg_marks_total";

    private int scored;

    public QuizCompleteFragment() {
    }

    public static QuizCompleteFragment newInstance(int marks, int total) {
        QuizCompleteFragment fragment = new QuizCompleteFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_MARKS_OBTAINED, marks);
        args.putInt(ARG_MARKS_TOTAL, total);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            scored = getArguments().getInt(ARG_MARKS_OBTAINED);
//            total = getArguments().getInt(ARG_MARKS_TOTAL);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.quiz_completed_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TextView scoredView = (TextView) view.findViewById(R.id.text_marks_scored);
        scoredView.setText("" + scored);

        AppCompatButton goToHome = (AppCompatButton) view.findViewById(R.id.go_to_home_button);
        goToHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });
    }

}
