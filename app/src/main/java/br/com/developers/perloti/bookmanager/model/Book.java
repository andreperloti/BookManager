package br.com.developers.perloti.bookmanager.model;


import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

/**
 * Created by perloti on 17/06/18.
 */

public class Book extends RealmObject {

    @PrimaryKey
    private String id;
    @Required
    private String title;
    private String author;
    private String description;
    private String status;
    private String thubmailLarge;
    private String thubmail;

    public static class Builder {

        // requerido
        private final String id;
        private final String title;

        // opcional
        private String author;
        private String description;
        private String status;
        private String thubmailLarge;
        private String thubmail;

        public Builder(String id, String title) {
            this.id = id;
            this.title = title;
        }

        public Builder author(String author) {
            this.author = author;
            return this;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Builder status(String status) {
            this.status = status;
            return this;
        }

        public Builder thubmailLarge(String thubmailLarge) {
            this.thubmailLarge = thubmailLarge;
            return this;
        }

        public Builder thubmail(String thubmail) {
            this.thubmail = thubmail;
            return this;
        }

        public Book build() {
            return new Book(this);
        }
    }

    private Book(Builder builder) {
        id = builder.id;
        title = builder.title;
        author = builder.author;
        description = builder.description;
        status = builder.status;
        thubmailLarge = builder.thubmailLarge;
        thubmail = builder.thubmail;
    }

    public Book() {
    }

    @Override
    public String toString() {
        return "Book{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", description='" + description + '\'' +
                ", status='" + status + '\'' +
                ", thubmailLarge='" + thubmailLarge + '\'' +
                ", thubmail='" + thubmail + '\'' +
                '}';
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getThubmailLarge() {
        return thubmailLarge;
    }

    public void setThubmailLarge(String thubmailLarge) {
        this.thubmailLarge = thubmailLarge;
    }

    public String getThubmail() {
        return thubmail;
    }

    public void setThubmail(String thubmail) {
        this.thubmail = thubmail;
    }


}
