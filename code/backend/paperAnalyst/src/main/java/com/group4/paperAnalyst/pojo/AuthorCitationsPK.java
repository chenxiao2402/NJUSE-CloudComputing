package com.group4.paperAnalyst.pojo;

import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;
@Embeddable
public class AuthorCitationsPK implements Serializable {

    private long year;

    private String author;

    private String subject;

    public long getYear() {
        return year;
    }

    public void setYear(long year) {
        this.year = year;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AuthorCitationsPK that = (AuthorCitationsPK) o;
        return  year == that.year &&
                author.equals(that.author) &&
                subject.equals(that.subject);
    }

    @Override
    public int hashCode() {
        return Objects.hash(year, author, subject);
    }
}
