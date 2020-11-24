package com.group4.paperAnalyst.vo;

/**
 * Author: J.D. Liao
 * Date: 2020/11/24
 * Description:
 */
public class YearPaperCount {

    private Long year;
    private Long count;

    public YearPaperCount(Long year, Long count) {
        this.year = year;
        this.count = count;
    }

    public Long getYear() {
        return year;
    }

    public void setYear(Long year) {
        this.year = year;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }
}
