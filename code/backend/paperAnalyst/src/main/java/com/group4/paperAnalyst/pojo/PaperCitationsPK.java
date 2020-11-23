package com.group4.paperAnalyst.pojo;

import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class PaperCitationsPK implements Serializable {

    private String title;

    private String subject;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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
        PaperCitationsPK that = (PaperCitationsPK) o;
        return title.equals(that.title) &&
                subject.equals(that.subject);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, subject);
    }
}
