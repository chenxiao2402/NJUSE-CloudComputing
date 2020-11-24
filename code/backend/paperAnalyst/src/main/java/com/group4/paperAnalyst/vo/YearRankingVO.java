package com.group4.paperAnalyst.vo;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: J.D. Liao
 * Date: 2020/11/24
 * Description:
 */
public class YearRankingVO {

    private String date;

    private List<FieldPaperCount> fields;

    public YearRankingVO(String date) {
        this.date = date;
        this.fields = new ArrayList<>();
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public List<FieldPaperCount> getFields() {
        return fields;
    }

    public void setFields(List<FieldPaperCount> fields) {
        this.fields = fields;
    }

    public static class FieldPaperCount {
        private String name;
        private Long paperNumber;

        public FieldPaperCount(String name, Long paperNumber) {
            this.name = name;
            this.paperNumber = paperNumber;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Long getPaperNumber() {
            return paperNumber;
        }

        public void setPaperNumber(Long paperNumber) {
            this.paperNumber = paperNumber;
        }
    }
}
