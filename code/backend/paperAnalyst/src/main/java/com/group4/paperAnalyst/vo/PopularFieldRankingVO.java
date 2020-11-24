package com.group4.paperAnalyst.vo;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: J.D. Liao
 * Date: 2020/11/24
 * Description:
 */
public class PopularFieldRankingVO {

    private List<String> fields;

    private List<YearRankingVO> rankings;

    public PopularFieldRankingVO() {
        this.fields = new ArrayList<>();
        this.rankings = new ArrayList<>();
    }

    public List<String> getFields() {
        return fields;
    }

    public void setFields(List<String> fields) {
        this.fields = fields;
    }

    public List<YearRankingVO> getRankings() {
        return rankings;
    }

    public void setRankings(List<YearRankingVO> rankings) {
        this.rankings = rankings;
    }
}
