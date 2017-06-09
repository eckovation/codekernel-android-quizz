package com.eckovation.course.quizz.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.eckovation.course.quizz.QuestionActivity;
import com.eckovation.course.quizz.R;
import com.eckovation.course.quizz.adapter.TopicsAdapter;
import com.eckovation.course.quizz.helper.FirebaseHelper;
import com.eckovation.course.quizz.models.TopicModel;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

/**
 * Created by codekernel on 05/06/17.
 */

public class AllTopicsFragment extends Fragment implements TopicsAdapter.TopicExpandedOptionsListener {


    RecyclerView mRecyclerView;
    ProgressBar loading;

    TopicsAdapter mAdapter;

    private DatabaseReference refs = FirebaseHelper.getTopicsRefs();
    private ChildEventListener childEventListener;

    public AllTopicsFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_topics_item_common, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mAdapter = new TopicsAdapter(getActivity(), this);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_topics);
        loading = (ProgressBar) view.findViewById(R.id.loading_topics);
        setupRecyclerView();
        listenForPopularTopicsFromServer();
    }

    private void listenForPopularTopicsFromServer() {
        childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if (mAdapter.getTopicModels().isEmpty()) {
                    loading.setVisibility(View.GONE);
                }
                TopicModel model = dataSnapshot.getValue(TopicModel.class);
                String key = dataSnapshot.getKey();
                mAdapter.getTopicModels().put(key, model);
                mAdapter.notifyItemInserted(mAdapter.getItemCount() + 1);

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                TopicModel model = dataSnapshot.getValue(TopicModel.class);
                String key = dataSnapshot.getKey();
                mAdapter.getTopicModels().put(key, model);
                mAdapter.notifyItemChanged(mAdapter.getTopicModels().indexOfKey(key));
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                String key = dataSnapshot.getKey();
                final int position = mAdapter.getTopicModels().indexOfKey(key);
                mAdapter.getTopicModels().remove(key);
                mAdapter.notifyItemRemoved(position);
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
//                TopicModel model = dataSnapshot.getValue(TopicModel.class);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        };
        refs.orderByKey().addChildEventListener(childEventListener);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (childEventListener != null) {
            refs.removeEventListener(childEventListener);
        }
    }

    /**
     * Setup the recycler view for the first time setting layouts and adapter
     */
    private void setupRecyclerView() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onPlayNowClick(int position, String key, TopicModel topic) {
        Intent intent = new Intent(getActivity(), QuestionActivity.class);
        intent.putExtra(QuestionActivity.ARG_TOPIC_KEY, key);
        getActivity().startActivity(intent);
    }


    @Override
    public void onChallengeClick(int position, TopicModel topic) {

    }

    @Override
    public void onRankingClick(int position, TopicModel topic) {

    }

    @Override
    public void onDiscussionClick(int position, TopicModel topic) {

    }
}
