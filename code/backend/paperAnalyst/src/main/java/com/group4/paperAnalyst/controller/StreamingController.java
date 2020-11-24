package com.group4.paperAnalyst.controller;

import com.group4.paperAnalyst.dao.*;
import com.group4.paperAnalyst.pojo.SubjectPaperCount;
import com.group4.paperAnalyst.service.StreamService;
import com.group4.paperAnalyst.util.MapSortUtil;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.*;

import java.util.*;

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
    public List<Map<String, Object>> findPopularFields(@Param("year") Long year) {
        List<Map<String, Object>> res = new LinkedList<>();
        // year内的
        //按照论文数量从高到低排序
        List<Object[]> list_paper = paperCitationsDAO.getPapernumByYear(year);

        Map<String,Integer> mapForSort = new HashMap<>();
        for(Object[] o : list_paper){
            mapForSort.put(o[0].toString(),Integer.valueOf(o[1].toString()));
        }
        Map<String,Integer> mapSorted= MapSortUtil.sortByValueDesc(mapForSort);
        for(String subject:mapSorted.keySet()){
            Map<String,Object> sub_res = new HashMap<>();
            List<Object> list_author = authorCitationsDAO.getAuthornumByYearSubject(year,subject);
            int author_num = Integer.valueOf(list_author.get(0).toString());
            sub_res.put("field",subject);
            sub_res.put("authorNumber",author_num);
            sub_res.put(("paperNumber"),mapSorted.get(subject));
            res.add(sub_res);
        }
        return res;
    }

    @ApiOperation(value = "", notes = "'根据输⼊的年份数量和领域，返回近x年该领域每年的⽂章数量")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "year", value = "年份", paramType = "query", dataType = "Long"),
            @ApiImplicitParam(name = "field", value = "领域", paramType = "query", dataType = "String")})
    @RequestMapping(value = "/PaperNumbers", method = RequestMethod.POST)
    @ResponseBody
    public List<Map<String, Object>> findPaperNumbers(@Param("year") Long year, @Param("field") String field) {
        List<Map<String, Object>> res = new LinkedList<>();
        List<Object[]> list_paper = paperCitationsDAO.getPapernumByYearField(year, field);
        Map<Integer,Integer> mapForSort = new HashMap<>();
        for(Object[] o : list_paper){
            mapForSort.put(Integer.valueOf(o[0].toString()),Integer.valueOf(o[1].toString()));
        }
        Map<Integer,Integer> mapSorted= MapSortUtil.sortByKeyAsc(mapForSort);
        for (Integer sortedYear: mapSorted.keySet()) {
            Map<String, Object> sub_res = new HashMap<>();
            sub_res.put("year", sortedYear);
            sub_res.put("paperNumber",mapSorted.get(sortedYear));
            res.add(sub_res);
        }
        return res;
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
    public List<Map<String, Object>> findPopularAnnualField(@Param("year") Long year) {
        List<Map<String, Object>> res = new LinkedList<>();
        Calendar cal = Calendar.getInstance();
        int year_now = cal.get(Calendar.YEAR);
        for (int i = year_now; i > year_now - year; i--) {
            List<SubjectPaperCount> subjectPaperCounts = subjectPaperCountDAO.getPaperNumByYear(Long.valueOf(i));
            if (subjectPaperCounts.isEmpty()) {
                continue;
            }
            Map<String, Object> sub_res = new HashMap<>();
            sub_res.put("year", i);
            sub_res.put("field", subjectPaperCounts.get(0).getId().getSubject());
            sub_res.put("count", subjectPaperCounts.get(0).getPaperCount());
            res.add(sub_res);
        }
        return res;
    }

    @ApiOperation(value = "", notes = "根据输⼊的年份数量，返回近x年来每⼀年的热⻔领域（按照⽂章数）和该领域的⽂章数")
    @ApiImplicitParam(name = "year", value = "年份", paramType = "query", dataType = "Long")
    @RequestMapping(value = "/PopularFieldRanking", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> findPopularFieldRanking(@Param("year") Long year) {
        Map<String, Object> res = new HashMap<>();
        Set<String> fields = new HashSet<>();
        List<Map<String, Object>> rankings = new LinkedList<>();

        Calendar cal = Calendar.getInstance();
        int year_now = cal.get(Calendar.YEAR);

        for (long i = year_now-year+1; i <= year_now ; i++) {
            for (int j = 1; j <= 12; j++) {//月份
                List<SubjectPaperCount> subjectPaperCounts = subjectPaperCountDAO.getFieldTop10Bydate(Long.valueOf(i), Long.valueOf(j));
                if (subjectPaperCounts.isEmpty()) {
                    continue;
                }

                String date = String.valueOf(i) + "-" + String.valueOf(j);
                Map<String, Object> sub_res = new HashMap<>();
                sub_res.put("date", date);
                List<Map<String, Object>> subFields = new LinkedList<>();
                for (SubjectPaperCount subjectPaperCount : subjectPaperCounts) {
                    fields.add(subjectPaperCount.getId().getSubject());
                    Map<String, Object> subFieldsItem = new HashMap<>();
                    subFieldsItem.put("name", subjectPaperCount.getId().getSubject());
                    subFieldsItem.put("paperNumber", subjectPaperCount.getPaperCount());
                    subFields.add(subFieldsItem);
                }
                sub_res.put("fields", subFields);
                rankings.add(sub_res);
            }
        }
        res.put("fields", fields);
        res.put("rankings", rankings);
        return res;
    }

    @ApiOperation(value = "", notes = "返回数据库中每⼀年的论文总数，会轮询调用这个方法，为了'启动云计算'的需求")
    @RequestMapping(value = "/LoadedDataNumbers", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> findAllPopularFieldRanking(@Param("year") Long year) {
        Map<String, Object> res = new HashMap<>();
        Set<String> fields = new HashSet<>();
        List<Map<String, Object>> rankings = new LinkedList<>();

        Calendar cal = Calendar.getInstance();
        int year_now = cal.get(Calendar.YEAR);

        for (int i = year_now; i > year_now - year; i--) {
            for (int j = 1; j <= 12; j++) {//月份
                List<SubjectPaperCount> subjectPaperCounts = subjectPaperCountDAO.getFieldTop10Bydate(Long.valueOf(i), Long.valueOf(j));
                if (subjectPaperCounts.isEmpty()) {
                    continue;
                }
                String date = String.valueOf(i) + "-" + String.valueOf(j);
                Map<String, Object> sub_res = new HashMap<>();
                sub_res.put("date:", date);
                List<Map<String, Object>> subFields = new LinkedList<>();
                long count = 0;
                for (SubjectPaperCount subjectPaperCount : subjectPaperCounts) {
                    count += subjectPaperCount.getPaperCount();
                }
                sub_res.put("count:", count);
                rankings.add(sub_res);
            }
        }
        res.put("rankings", rankings);
        return res;
    }

    @ApiOperation(value = "", notes = "启动云计算")
    @RequestMapping(value = "/StartPaperCount", method = RequestMethod.POST)
    public String startStreamingService() {
        return streamService.startPaperCountStream();
    }
}
