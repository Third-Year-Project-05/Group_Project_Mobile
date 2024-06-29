package com.example.echolynk.Model;

public class Blog {
    private String title;
    private String author;
    private int image;
    private String description;


    public Blog() {
    }

    public Blog(String title, String author, int image) {
        this.title = title;
        this.author = author;
        this.image = image;
    }

    public Blog(String title, String author, int image, String description) {
        this.title = title;
        this.author = author;
        this.image = image;
        this.description = description;
    }

    public Blog(String title, int image) {
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

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getDescription() {return description;}

    public void setDescription(String description) {this.description = description;}

    @Override
    public String toString() {
        return "Blog{" +
                "title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", image=" + image +
                '}';
    }
}

