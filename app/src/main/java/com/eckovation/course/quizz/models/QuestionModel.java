package com.eckovation.course.quizz.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by codekernel on 07/06/17.
 */

public class QuestionModel implements Parcelable {

    private String question;
    private int correct;
    private List<String> options;
    private String image;

    public QuestionModel() {
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public int getCorrect() {
        return correct;
    }

    public void setCorrect(int correct) {
        this.correct = correct;
    }

    public List<String> getOptions() {
        return options;
    }

    public void setOptions(List<String> options) {
        this.options = options;
    }

    @Override
    public String toString() {
        return "QuestionModel{" +
                "question='" + question + '\'' +
                ", correct=" + correct +
                ", options=" + options +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.question);
        dest.writeInt(this.correct);
        dest.writeStringList(this.options);
        dest.writeString(this.image);
    }

    protected QuestionModel(Parcel in) {
        this.question = in.readString();
        this.correct = in.readInt();
        this.options = in.createStringArrayList();
        this.image = in.readString();
    }

    public static final Creator<QuestionModel> CREATOR = new Creator<QuestionModel>() {
        public QuestionModel createFromParcel(Parcel source) {
            return new QuestionModel(source);
        }

        public QuestionModel[] newArray(int size) {
            return new QuestionModel[size];
        }
    };
}
