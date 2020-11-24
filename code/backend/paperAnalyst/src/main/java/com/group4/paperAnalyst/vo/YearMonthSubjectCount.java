package com.group4.paperAnalyst.vo;

/**
 * Author: J.D. Liao
 * Date: 2020/11/24
 * Description:
 */
public class YearMonthSubjectCount {

    private Long year;

    private Long month;

    private String subject;

    private Long count;

    public YearMonthSubjectCount(Long year, Long month, String subject, Long count) {
        this.year = year;
        this.month = month;
        this.subject = subject;
        this.count = count;
    }

    public Long getMonth() {
        return month;
    }

    public void setMonth(Long month) {
        this.month = month;
    }

    public Long getYear() {
        return year;
    }

    public void setYear(Long year) {
        this.year = year;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }

    public YearMonthSubjectCount(Long year, String subject, Long count) {
        this.year = year;
        this.subject = subject;
        this.count = count;
    }


}
