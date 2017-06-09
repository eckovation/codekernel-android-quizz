package com.eckovation.course.quizz.adapter;

import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v4.util.SimpleArrayMap;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.eckovation.course.quizz.R;
import com.eckovation.course.quizz.models.TopicModel;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by codekernel on 06/06/17.
 */

public class TopicsAdapter extends RecyclerView.Adapter<TopicsAdapter.TopicViewHolder> {

    private SimpleArrayMap<String, TopicModel> topicModels = new SimpleArrayMap<>();
    private List<GradientDrawable> gradients = new ArrayList<>();
    private TopicExpandedOptionsListener listener;

    public TopicsAdapter(Context context, TopicExpandedOptionsListener listener) {
        this.listener = listener;
        fillGradientList(context);
    }

    @Override
    public TopicViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.topic_item_view, parent, false);
        return new TopicViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TopicViewHolder holder, int position, List<Object> payloads) {
        super.onBindViewHolder(holder, position, payloads);
        if (!payloads.isEmpty() && payloads.get(0) instanceof Boolean) {
            final TopicModel topic = topicModels.get(topicModels.keyAt(position));
            if (!topic.isExpanded()) {
                holder.expandedView.setVisibility(View.GONE);
            } else {
                holder.expandedView.setScaleX(0);
                holder.expandedView.setScaleY(0);
                holder.expandedView.setVisibility(View.VISIBLE);
            }
        } else {
            onBindViewHolder(holder, position);
        }
    }

    @Override
    public void onBindViewHolder(TopicViewHolder holder, int position) {
        final TopicModel topic = topicModels.get(topicModels.keyAt(position));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            holder.baseGradientLayout.setBackground(gradients.get(position % 4));
        } else {
            holder.baseGradientLayout.setBackgroundDrawable(gradients.get(position % 4));
        }
        holder.title.setText(topic.getTitle());
        if (topic.getResourceId() != 0) {
            holder.image.setImageResource(topic.getResourceId());
        } else {
            holder.image.setImageResource(R.drawable.ic_menu);
        }
        if (!topic.isExpanded()) {
            holder.expandedView.setVisibility(View.GONE);
        } else {
            holder.expandedView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return topicModels == null ? 0 : topicModels.size();
    }

    public SimpleArrayMap<String, TopicModel> getTopicModels() {
        return topicModels;
    }

    private void fillGradientList(Context context) {
        gradients.add(getTempGradientDrawable(ContextCompat.getColor(context, R.color.gradient_1_start), ContextCompat.getColor(context, R.color.gradient_1_end)));
        gradients.add(getTempGradientDrawable(ContextCompat.getColor(context, R.color.gradient_2_start), ContextCompat.getColor(context, R.color.gradient_2_end)));
        gradients.add(getTempGradientDrawable(ContextCompat.getColor(context, R.color.gradient_3_start), ContextCompat.getColor(context, R.color.gradient_3_end)));
        gradients.add(getTempGradientDrawable(ContextCompat.getColor(context, R.color.gradient_4_start), ContextCompat.getColor(context, R.color.gradient_4_end)));
    }

    private GradientDrawable getTempGradientDrawable(int startColor, int endColor) {
        GradientDrawable drawable = new GradientDrawable(GradientDrawable.Orientation.BR_TL, new int[]{startColor, endColor});
        drawable.setDither(true);
        drawable.setGradientCenter(drawable.getIntrinsicWidth() / 8, drawable.getIntrinsicHeight() / 2);
        drawable.setCornerRadius(20);
        drawable.setShape(GradientDrawable.RECTANGLE);
        drawable.setGradientType(GradientDrawable.LINEAR_GRADIENT);
        drawable.setUseLevel(true);
        return drawable;
    }

    public interface TopicExpandedOptionsListener {

        void onPlayNowClick(int position, String key, TopicModel topic);

        void onChallengeClick(int position, TopicModel topic);

        void onRankingClick(int position, TopicModel topic);

        void onDiscussionClick(int position, TopicModel topic);
    }

    class TopicViewHolder extends RecyclerView.ViewHolder {

        RelativeLayout baseGradientLayout;
        TextView title;
        ImageView image;
        CardView expandedView;

        public TopicViewHolder(View itemView) {
            super(itemView);
            baseGradientLayout = (RelativeLayout) itemView.findViewById(R.id.layout_topic_item);
            title = (TextView) itemView.findViewById(R.id.textView_topic_item);
            image = (ImageView) itemView.findViewById(R.id.imageView_topic_item);
            expandedView = (CardView) itemView.findViewById(R.id.expanded_view_topics);

            baseGradientLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (getAdapterPosition() == -1) {
                        //Just in case the view is recycled
                        return;
                    }
                    final TopicModel topicModel = topicModels.get(topicModels.keyAt(getAdapterPosition()));
                    if (topicModel.isExpanded()) {
                        shrinkView(baseGradientLayout);
                    } else if (!topicModel.isExpanded()) {
                        expandView(baseGradientLayout, expandedView);
                    }
                    topicModel.toggleExpand();
                    notifyItemChanged(getAdapterPosition(), true);
                }
            });

            Button playNowButton = (Button) expandedView.findViewById(R.id.image_button_play_now);
            Button challengeButton = (Button) expandedView.findViewById(R.id.image_button_challenge);
            Button rankingButton = (Button) expandedView.findViewById(R.id.image_button_ranking);
            Button discussionButton = (Button) expandedView.findViewById(R.id.image_button_discussion);

            playNowButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        final TopicModel topicModel = topicModels.get(topicModels.keyAt(getAdapterPosition()));
                        listener.onPlayNowClick(getAdapterPosition(), topicModels.keyAt(getAdapterPosition()), topicModel);
                    }
                }
            });
            challengeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        final TopicModel topicModel = topicModels.get(topicModels.keyAt(getAdapterPosition()));
                        listener.onChallengeClick(getAdapterPosition(), topicModel);
                    }
                }
            });
            rankingButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        final TopicModel topicModel = topicModels.get(topicModels.keyAt(getAdapterPosition()));
                        listener.onRankingClick(getAdapterPosition(), topicModel);
                    }
                }
            });
            discussionButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        final TopicModel topicModel = topicModels.get(topicModels.keyAt(getAdapterPosition()));
                        listener.onDiscussionClick(getAdapterPosition(), topicModel);
                    }
                }
            });

        }

        @TargetApi(Build.VERSION_CODES.HONEYCOMB)
        private void expandView(final RelativeLayout baseGradientLayout, final CardView expandedView) {
            ValueAnimator valueAnimator = ValueAnimator.ofInt(baseGradientLayout.getPaddingTop(), baseGradientLayout.getPaddingTop() / 4);
            valueAnimator.setDuration(300);
            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    int padding = (int) animation.getAnimatedValue();
                    baseGradientLayout.setPadding(baseGradientLayout.getPaddingLeft(), padding, baseGradientLayout.getPaddingRight(), padding);
                    ViewCompat.setScaleY(expandedView, animation.getAnimatedFraction());
                    ViewCompat.setScaleX(expandedView, animation.getAnimatedFraction());
                }
            });
            valueAnimator.start();
        }

        @TargetApi(Build.VERSION_CODES.HONEYCOMB)
        private void shrinkView(final RelativeLayout baseGradientLayout) {
            ValueAnimator valueAnimator = ValueAnimator.ofInt(baseGradientLayout.getPaddingTop(), baseGradientLayout.getPaddingTop() * 4);
            valueAnimator.setDuration(300);
            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    int padding = (int) animation.getAnimatedValue();
                    baseGradientLayout.setPadding(baseGradientLayout.getPaddingLeft(), padding, baseGradientLayout.getPaddingRight(), padding);
                }
            });
            valueAnimator.start();
        }
    }
}
