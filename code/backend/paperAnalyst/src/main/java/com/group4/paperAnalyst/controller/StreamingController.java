package com.group4.paperAnalyst.controller;

import com.group4.paperAnalyst.dao.*;
import com.group4.paperAnalyst.pojo.SubjectPaperCount;
import com.group4.paperAnalyst.service.StreamService;
import com.group4.paperAnalyst.util.MonthFieldsAccumulator;
import com.group4.paperAnalyst.vo.*;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@CrossOrigin
public class StreamingController {
    @Autowired
    AuthorCitationsDAO authorCitationsDAO;

    @Autowired
    PaperCitationsDAO paperCitationsDAO;

    @Autowired
    SubjectPaperCountDAO subjectPaperCountDAO;

    @Autowired
    AuthorConnectionsAuthorDAO authorConnectionsAuthorDAO;

    @Autowired
    RelativeSubjectsDAO relativeSubjectsDAO;

    @Autowired
    CollaborationDAO collaborationDAO;

    @Autowired
    StreamService streamService;

    @ApiOperation(value = "", notes = "根据输⼊的年份数量，返回近x年各个领域的⽂章数和作者数，选择⽂章数量的前20名")
    @ApiImplicitParam(name = "year", value = "年份", paramType = "query", dataType = "Long")
    @RequestMapping(value = "/PopularFields", method = RequestMethod.POST)
    @ResponseBody
    public List<PopularFieldsVO> findPopularFields(@Param("year") Long year) {
        return subjectPaperCountDAO.getPopularFieldsInfoAfterYear(year)
                .stream()
                .sorted(Comparator.comparing(PopularFieldsVO::getPaperNumber).reversed())
                .limit(20)
                .collect(Collectors.toList());
    }

    @ApiOperation(value = "", notes = "'根据输⼊的年份数量和领域，返回近x年该领域每年的⽂章数量")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "year", value = "年份", paramType = "query", dataType = "Long"),
            @ApiImplicitParam(name = "field", value = "领域", paramType = "query", dataType = "String")})
    @RequestMapping(value = "/PaperNumbers", method = RequestMethod.POST)
    @ResponseBody
    public List<YearSubjectPaperCountVO> findPaperNumbers(@Param("year") Long year, @Param("field") String field) {
        return subjectPaperCountDAO.getSubjectYearTrend(year, field);
    }

    @ApiOperation(value = "", notes = "根据输⼊的年份数量和领域，返回近x年该领域⾼引⽤⽂章前10名")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "year", value = "年份", paramType = "query", dataType = "Long"),
            @ApiImplicitParam(name = "field", value = "领域", paramType = "query", dataType = "String")})
    @RequestMapping(value = "/PopularPapers", method = RequestMethod.POST)
    @ResponseBody
    public List<Map<String, Object>> findPopularPapers(@Param("year") Long year, @Param("field") String field) {
        List<Map<String, Object>> res = new LinkedList<>();
        List<Object[]> list_paper = paperCitationsDAO.getPapersByYearField(year, field);
        for (Object[] o : list_paper) {
            Map<String, Object> sub_res = new HashMap<>();
            sub_res.put("paper", o[0].toString());
            sub_res.put("citation", Integer.valueOf(o[1].toString()));
            res.add(sub_res);
        }
        return res;
    }

    @ApiOperation(value = "", notes = "'根据输⼊的年份数量和领域，返回近x年该领域⾼引⽤作者前10名")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "year", value = "年份", paramType = "query", dataType = "Long"),
            @ApiImplicitParam(name = "field", value = "领域", paramType = "query", dataType = "String")})
    @RequestMapping(value = "/PopularAuthors", method = RequestMethod.POST)
    @ResponseBody
    public List<Map<String, Object>> findPopularAuthors(@Param("year") Long year, @Param("field") String field) {
        List<Map<String, Object>> res = new LinkedList<>();
        List<Object[]> list_paper = authorCitationsDAO.getAuthorByYearField(year, field);
        for (Object[] o : list_paper) {
            Map<String, Object> sub_res = new HashMap<>();
            sub_res.put("author", o[0].toString());
            sub_res.put("citation", Integer.valueOf(o[1].toString()));
            res.add(sub_res);
        }
        return res;
    }

    @ApiOperation(value = "", notes = "''根据输⼊的年份数量，返回近x年来每⼀年的热⻔领域（按照⽂章数）和该领域的⽂章\n" +
            "数")
    @ApiImplicitParam(name = "year", value = "年份", paramType = "query", dataType = "Long")
    @RequestMapping(value = "/PopularAnnualField", method = RequestMethod.POST)
    @ResponseBody
    public List<YearSubjectPaperCountVO> findPopularAnnualField(@Param("year") Long year) {
        List<YearSubjectPaperCountVO> yearSubjectPaperCountVOS = subjectPaperCountDAO.getYearSubjectPaperCount(year);
        Map<Long, List<YearSubjectPaperCountVO>> fieldsByYear = yearSubjectPaperCountVOS.stream()
                .collect(Collectors.groupingBy(YearSubjectPaperCountVO::getYear));

        return fieldsByYear.entrySet().stream().sorted(Map.Entry.comparingByKey()).map(entry ->
            entry.getValue().stream().max(Comparator.comparing(YearSubjectPaperCountVO::getCount))
                    .orElseThrow(NoSuchElementException::new)
        ).collect(Collectors.toList());
    }

    @ApiOperation(value = "", notes = "根据输⼊的年份数量，返回近x年来每⼀年的热⻔领域（按照⽂章数）和该领域的⽂章数")
    @ApiImplicitParam(name = "year", value = "年份", paramType = "query", dataType = "Long")
    @RequestMapping(value = "/PopularFieldRanking", method = RequestMethod.POST)
    public PopularFieldRankingVO findPopularFieldRanking(@Param("year") Long year) {

        int yearNow = getCurrentYear();

        PopularFieldRankingVO result = new PopularFieldRankingVO();
        Set<String> appearedFields = new HashSet<>();
        MonthFieldsAccumulator accumulator = new MonthFieldsAccumulator();
        for (long i = year; i <= yearNow; i++) {
            List<SubjectPaperCount> yearRankings =
                    subjectPaperCountDAO.getPopularFieldRankingByYear(i);
            Map<Long, List<SubjectPaperCount>> monthFields = yearRankings.stream()
                    .collect(Collectors.groupingBy(SubjectPaperCount::getMonth));

            long currentYear = i;
            monthFields.entrySet().stream()
                    .sorted(Map.Entry.comparingByKey())
                    .forEach(e -> accumulator.accumulate(e, currentYear));
        }

        accumulator.getMonthFields().entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .forEach(entry -> {
                    String date = dateString(entry.getKey().getYear(), entry.getKey().getMonth());
                    YearRankingVO yearRankingVO = new YearRankingVO(date);

                    List<YearRankingVO.FieldPaperCount> fields = entry.getValue().stream()
                            .limit(10)
                            .map(s -> {
                                appearedFields.add(s.getId().getSubject());
                                return new YearRankingVO.FieldPaperCount(s.getId().getSubject(), s.getPaperCount());
                            })
                            .collect(Collectors.toList());
                    yearRankingVO.setFields(fields);
                    result.getRankings().add(yearRankingVO);
                });
        result.setFields(new ArrayList<>(appearedFields));

        return result;
    }

    @RequestMapping("/YearPaperCount")
    public List<YearPaperCount> getYearPaperCount() {
        return subjectPaperCountDAO.getYearPaperCount();
    }

    @RequestMapping(value = "/StartPaperCount")
    public String startStreamingService() {
        return streamService.startPaperCountStream();
    }

    private String dateString(Long year, Long month) {
        if (month < 10) {
            return year.toString() + "-0" + month.toString();
        } else {
            return year.toString() + "-" + month.toString();
        }
    }

    private int getCurrentYear() {
        Calendar cal = Calendar.getInstance();
        return cal.get(Calendar.YEAR);
    }
}
