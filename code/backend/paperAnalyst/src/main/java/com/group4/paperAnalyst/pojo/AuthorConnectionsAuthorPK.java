package com.group4.paperAnalyst.pojo;

import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class AuthorConnectionsAuthorPK implements Serializable {
    private long startYear;

    private String subject;

    private long authorId;

    public long getStartYear() {
        return startYear;
    }

    public void setStartYear(long startYear) {
        this.startYear = startYear;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public long getAuthorId() {
        return authorId;
    }

    public void setAuthorId(long authorId) {
        this.authorId = authorId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AuthorConnectionsAuthorPK that = (AuthorConnectionsAuthorPK) o;
        return startYear == that.startYear &&
                authorId == that.authorId &&
                subject.equals(that.subject);
    }

    @Override
    public int hashCode() {
        return Objects.hash(startYear, subject, authorId);
    }
}
