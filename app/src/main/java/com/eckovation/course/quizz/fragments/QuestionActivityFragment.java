package com.eckovation.course.quizz.fragments;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.eckovation.course.quizz.QuestionActivity;
import com.eckovation.course.quizz.R;
import com.eckovation.course.quizz.models.QuestionModel;

/**
 * A placeholder fragment containing a simple view. By codekernel.
 */
public class QuestionActivityFragment extends Fragment {

    public static final String ARG_QUESTION_KEY = "arg_question_key";
    public static final String ARG_QUESTION_VALUE = "arg_question_value";

    private String mQuestionKey;
    private QuestionModel mQuestion;

    private TextView questiontext;
    private ImageView questionimage;
    private Button questionanswerbutton1;
    private Button questionanswerbutton2;
    private Button questionanswerbutton3;
    private Button questionanswerbutton4;

    public QuestionActivityFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param key      key of the question as in firebase realtime database.
     * @param question Question model as retrieved in base activity.
     * @return A new instance of fragment @{@link QuestionActivityFragment}.
     */
    public static QuestionActivityFragment newInstance(String key, QuestionModel question) {
        QuestionActivityFragment fragment = new QuestionActivityFragment();
        Bundle args = new Bundle();
        args.putString(ARG_QUESTION_KEY, key);
        args.putParcelable(ARG_QUESTION_VALUE, question);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mQuestionKey = getArguments().getString(ARG_QUESTION_KEY);
            mQuestion = getArguments().getParcelable(ARG_QUESTION_VALUE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_question, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.questionanswerbutton4 = (Button) view.findViewById(R.id.question_answer_button4);
        this.questionanswerbutton3 = (Button) view.findViewById(R.id.question_answer_button3);
        this.questionanswerbutton2 = (Button) view.findViewById(R.id.question_answer_button2);
        this.questionanswerbutton1 = (Button) view.findViewById(R.id.question_answer_button1);
        this.questionimage = (ImageView) view.findViewById(R.id.question_image);
        this.questiontext = (TextView) view.findViewById(R.id.question_text);

        questiontext.setText(mQuestion.getQuestion());

        questionanswerbutton1.setText(mQuestion.getOptions().get(0));
        questionanswerbutton2.setText(mQuestion.getOptions().get(1));
        questionanswerbutton3.setText(mQuestion.getOptions().get(2));
        questionanswerbutton4.setText(mQuestion.getOptions().get(3));

        questionanswerbutton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                disableAllButtons();
                questionanswerbutton1.setSelected(true);
                questionanswerbutton1.setTextColor(Color.WHITE);
                checkCorrectAnswer(questionanswerbutton1, 1);
            }
        });
        questionanswerbutton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                disableAllButtons();
                questionanswerbutton2.setSelected(true);
                questionanswerbutton2.setTextColor(Color.WHITE);
                checkCorrectAnswer(questionanswerbutton2, 2);
            }
        });
        questionanswerbutton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                disableAllButtons();
                questionanswerbutton3.setSelected(true);
                questionanswerbutton3.setTextColor(Color.WHITE);
                checkCorrectAnswer(questionanswerbutton3, 3);
            }
        });
        questionanswerbutton4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                disableAllButtons();
                questionanswerbutton4.setSelected(true);
                questionanswerbutton4.setTextColor(Color.WHITE);
                checkCorrectAnswer(questionanswerbutton4, 4);
            }
        });

    }

    private void checkCorrectAnswer(Button selectedButton, int answerPosition) {
        final QuestionActivity activity = (QuestionActivity) getActivity();
        activity.cancelCountDown();

        boolean isAnswerCorrect = mQuestion.getCorrect() == answerPosition;

        if (isAnswerCorrect) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                selectedButton.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.button_background_green_filled));
            } else {
                selectedButton.setBackgroundDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.button_background_green_filled));
            }
        } else {
            Button correctButton = getCorrectAnswerButton(mQuestion.getCorrect());
            if (correctButton == null) {
                return;
            }
            correctButton.setTextColor(Color.WHITE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                correctButton.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.button_background_green_filled));
            } else {
                correctButton.setBackgroundDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.button_background_green_filled));
            }
        }
        //Copied Variable to make it accessible inside runnable class
        final boolean finalIsAnswerCorrect = isAnswerCorrect;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                activity.moveToNextQuestion(mQuestionKey, finalIsAnswerCorrect);
            }
        }, 1500);
    }


    private void disableAllButtons() {
        questionanswerbutton1.setEnabled(false);
        questionanswerbutton2.setEnabled(false);
        questionanswerbutton3.setEnabled(false);
        questionanswerbutton4.setEnabled(false);
    }

    private Button getCorrectAnswerButton(int correct) {
        switch (correct) {
            case 1:
                return questionanswerbutton1;
            case 2:
                return questionanswerbutton2;
            case 3:
                return questionanswerbutton3;
            case 4:
                return questionanswerbutton4;
            default:
                return null;
        }
    }
}
