package com.eckovation.course.quizz;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v4.util.SimpleArrayMap;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.eckovation.course.quizz.fragments.QuestionActivityFragment;
import com.eckovation.course.quizz.fragments.QuizCompleteFragment;
import com.eckovation.course.quizz.helper.FirebaseHelper;
import com.eckovation.course.quizz.models.QuestionModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

public class QuestionActivity extends AppCompatActivity {

    public static final String ARG_TOPIC_KEY = "arg_topic_key";
    private static final String TAG = QuestionActivity.class.getCanonicalName();
    Handler timeHandler = new Handler();
    private SimpleArrayMap<String, QuestionModel> mQuestionsMap = new SimpleArrayMap<>();
    private String topicKey;
    private DatabaseReference refs;
    private TextView countdownTextView;
    private TextView countdownStaticTextView;
    private TextView questionNoTextView;
    private Runnable delayedRunnable;

    private CountDownTimer countDownTimer = new CountDownTimer(30000, 1000) {

        public void onTick(long millisUntilFinished) {
            countdownTextView.setText(String.valueOf(millisUntilFinished / 1000));
        }

        public void onFinish() {
            countdownTextView.setText("0");
        }
    };
    private ValueEventListener valueEventListener;
    private int noOfAnswerCorrectMarked = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        countdownTextView = (TextView) findViewById(R.id.countdown_timer_question);
        countdownStaticTextView = (TextView) findViewById(R.id.countdown_timer_static_text);
        questionNoTextView = (TextView) findViewById(R.id.question_number_text);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        if (savedInstanceState == null) {
            topicKey = getIntent().getStringExtra(ARG_TOPIC_KEY);
        } else {
            topicKey = savedInstanceState.getString(ARG_TOPIC_KEY);
        }
        if (topicKey != null) {
            refs = FirebaseHelper.getQuestionRefs(topicKey);
            getQuestionsFromServer();
        }

        delayedRunnable = new Runnable() {
            @Override
            public void run() {
                countDownTimer.start();
            }
        };
    }

    private void getQuestionsFromServer() {
        valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    QuestionModel question = data.getValue(QuestionModel.class);
                    String key = data.getKey();
                    mQuestionsMap.put(key, question);
                }
                if (!mQuestionsMap.isEmpty()) {
                    final String key0 = mQuestionsMap.keyAt(0);

                    final QuestionActivityFragment fragment = QuestionActivityFragment.newInstance(key0, mQuestionsMap.get(key0));
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragment_content_question, fragment).commit();
                    startCountDown();
                    questionNoTextView.setText(String.format(getString(R.string.question_no), 1, mQuestionsMap.size()));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        refs.addValueEventListener(valueEventListener);
    }

    public void startCountDown() {
        timeHandler.postDelayed(delayedRunnable, 500);
    }

    public void cancelCountDown() {
        countDownTimer.cancel();
    }

    void resetCountDown() {
        countDownTimer.cancel();
        timeHandler.postDelayed(delayedRunnable, 500);
    }

    public void moveToNextQuestion(String previousKey, boolean isAnswerCorrect) {
        if (isAnswerCorrect) {
            noOfAnswerCorrectMarked++;
        }
        int prevPosition = mQuestionsMap.indexOfKey(previousKey);
        int newPosition = prevPosition + 1;
        if (newPosition < mQuestionsMap.size()) {
            questionNoTextView.setText(String.format(getString(R.string.question_no), newPosition + 1, mQuestionsMap.size()));
            final String key0 = mQuestionsMap.keyAt(newPosition);
            final QuestionActivityFragment fragment = QuestionActivityFragment.newInstance(key0, mQuestionsMap.get(key0));
            getSupportFragmentManager().beginTransaction()
                    .setCustomAnimations(R.anim.push_left_in, R.anim.push_left_out, R.anim.push_left_in, R.anim.push_left_out)
                    .replace(R.id.fragment_content_question, fragment).commit();
            resetCountDown();
        } else {
            final QuizCompleteFragment fragment = QuizCompleteFragment.newInstance(noOfAnswerCorrectMarked, mQuestionsMap.size());
            getSupportFragmentManager().beginTransaction()
                    .setCustomAnimations(R.anim.push_left_in, R.anim.push_left_out, R.anim.push_left_in, R.anim.push_left_out)
                    .replace(R.id.fragment_content_question, fragment).commit();
            countdownTextView.setVisibility(View.GONE);
            countdownStaticTextView.setVisibility(View.GONE);
            questionNoTextView.setVisibility(View.GONE);
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (valueEventListener != null) {
            refs.removeEventListener(valueEventListener);
        }
        countDownTimer.cancel();
        timeHandler.removeCallbacks(delayedRunnable);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(ARG_TOPIC_KEY, topicKey);
    }
}
