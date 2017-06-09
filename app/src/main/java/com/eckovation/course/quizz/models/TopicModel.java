package com.eckovation.course.quizz.models;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by codekernel on 06/06/17.
 */

@IgnoreExtraProperties
public class TopicModel {

    private String title;
    private String image;
    private boolean isPopular;

    private int resourceId;
    private boolean isExpanded = false;

    public TopicModel() {
    }

    public TopicModel(String title, boolean isPopular) {
        this.title = title;
        this.isPopular = isPopular;
    }

    public TopicModel(String title, int resourceId) {
        this.title = title;
        this.resourceId = resourceId;
    }

    public TopicModel(String title, String image, int resourceId) {
        this.title = title;
        this.image = image;
        this.resourceId = resourceId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Exclude
    public int getResourceId() {
        return resourceId;
    }

    public void setResourceId(int resourceId) {
        this.resourceId = resourceId;
    }

    @Exclude
    public boolean isExpanded() {
        return isExpanded;
    }

    public void toggleExpand() {
        isExpanded = !this.isExpanded;
    }

    public boolean isPopular() {
        return isPopular;
    }

    public void setPopular(boolean popular) {
        isPopular = popular;
    }

    @Override
    public String toString() {
        return "TopicModel{" +
                "title='" + title + '\'' +
                ", image='" + image + '\'' +
                ", isPopular=" + isPopular +
                '}';
    }
}
