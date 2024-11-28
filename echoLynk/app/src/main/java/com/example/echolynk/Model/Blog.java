package com.example.echolynk.Model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.google.firebase.Timestamp;

public class Blog implements Parcelable {

    private String id;
    private String status;
    private String title;
    private String author;
    private String image;
    private String description;
    private Timestamp timestamp;

    public Blog(String id, String title, String author, String description, Timestamp timestamp,String image) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.description = description;
        this.timestamp = timestamp;
        this.image=image;
    }

    public Blog() {
    }

    public Blog(String title, String author, String image) {
        this.title = title;
        this.author = author;
        this.image = image;
    }

    public Blog(String title, String author, String image, String description) {
        this.title = title;
        this.author = author;
        this.image = image;
        this.description = description;
    }

    public Blog(String title, String image) {
        this.title = title;
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDescription() {return description;}

    public void setDescription(String description) {this.description = description;}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }
    @Override
    public String toString() {
        return "Blog{" +
                "id='" + id + '\'' +
                ", status='" + status + '\'' +
                ", title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", image=" + image +
                ", description='" + description + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public Blog(Parcel in) {
        id = in.readString();
        title = in.readString();
        author = in.readString();
        image=in.readString();
        description = in.readString();
        timestamp = in.readParcelable(Timestamp.class.getClassLoader());
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(title);
        parcel.writeString(author);
        parcel.writeString(image);
        parcel.writeString(description);
        parcel.writeParcelable(timestamp, i);
    }

    public static final Creator<Blog> CREATOR = new Creator<Blog>() {
        @Override
        public Blog createFromParcel(Parcel in) {
            return new Blog(in);
        }

        @Override
        public Blog[] newArray(int size) {
            return new Blog[size];
        }
    };
}

