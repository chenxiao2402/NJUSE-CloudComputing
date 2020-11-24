package com.group4.paperAnalyst.util;

import com.group4.paperAnalyst.pojo.SubjectPaperCount;

import java.time.LocalDate;
import java.util.*;

public class MonthFieldsAccumulator {
    private final Map<YearMonthIndex, List<SubjectPaperCount>> monthFields = new HashMap<>(13);

    public void accumulate(Map.Entry<Long, List<SubjectPaperCount>> currMonthFields, Long year) {
        List<SubjectPaperCount> paperCounts = currMonthFields.getValue();
        Long currMonth = currMonthFields.getKey();
        YearMonthIndex currYearMonth = new YearMonthIndex(year, currMonth);

        if (monthFields.size() == 0) {
            this.monthFields.put(currYearMonth, currMonthFields.getValue());

        } else {
            List<SubjectPaperCount> prev = this.monthFields.get(currYearMonth.previousMonth());
            List<SubjectPaperCount> curr = new ArrayList<>();
            // accumulate the same subjects
            for (SubjectPaperCount prevPaperCount : prev) {
                Optional<SubjectPaperCount> currPaperCountOptional = paperCounts.stream()
                        .filter(p -> p.getId().getSubject().equals(prevPaperCount.getId().getSubject()))
                        .findFirst();

                if (!currPaperCountOptional.isPresent()) {
                    continue;
                }

                SubjectPaperCount currPaperCount = currPaperCountOptional.get();

                SubjectPaperCount newPaperCount = prevPaperCount.copy();
                newPaperCount.setPaperCount(newPaperCount.getPaperCount() + currPaperCount.getPaperCount());
                newPaperCount.setAuthorCount(newPaperCount.getAuthorCount() + currPaperCount.getAuthorCount());
                newPaperCount.getId().setMonth(currMonthFields.getKey());
                curr.add(newPaperCount);
            }

            // find the subjects that are not contained in previous list
            paperCounts.stream()
                    .filter(p -> !paperCountsContainSubject(prev, p))
                    .forEach(curr::add);

            curr.sort(Comparator.comparing(SubjectPaperCount::getPaperCount).reversed());

            monthFields.put(currYearMonth, curr);
        }
    }

    public Map<YearMonthIndex, List<SubjectPaperCount>> getMonthFields() {
        return monthFields;
    }

    private boolean paperCountsContainSubject(List<SubjectPaperCount> paperCounts, SubjectPaperCount target) {
        return paperCounts.stream()
                .anyMatch(p -> p.getId().getSubject().equals(target.getId().getSubject()));
    }

    public static class YearMonthIndex implements Comparable<YearMonthIndex> {
        private final Long year;
        private final Long month;

        public YearMonthIndex(Long year, Long month) {
            this.year = year;
            this.month = month;
        }

        public Long getYear() {
            return year;
        }

        public Long getMonth() {
            return month;
        }

        public YearMonthIndex previousMonth() {
            if (month == 1) {
                return new YearMonthIndex(year - 1, 12L);
            }
            return new YearMonthIndex(year, month - 1);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            YearMonthIndex that = (YearMonthIndex) o;
            return Objects.equals(year, that.year) &&
                    Objects.equals(month, that.month);
        }

        @Override
        public int hashCode() {
            return Objects.hash(year, month);
        }

        @Override
        public int compareTo(YearMonthIndex o) {
            LocalDate dateC1 = LocalDate.of(this.year.intValue(), this.month.intValue(), 1);
            LocalDate dateC2 = LocalDate.of(o.year.intValue(), o.month.intValue(), 1);
            return dateC1.compareTo(dateC2);
        }
    }
}
