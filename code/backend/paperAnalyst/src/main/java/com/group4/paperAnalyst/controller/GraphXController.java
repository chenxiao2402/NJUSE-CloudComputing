package com.group4.paperAnalyst.controller;

import com.group4.paperAnalyst.dao.*;
import com.group4.paperAnalyst.pojo.*;
import com.group4.paperAnalyst.util.MapSortUtil;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@CrossOrigin
public class GraphXController {
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
    SubjectCrossoverRankDAO subjectCrossoverRankDAO;

    @ApiOperation(value = "",notes="根据输⼊的年份数量，返回近x年来各个领域的⽂章数和作者数，选择pagerank算法\n" +
            "（graphx部分）得到的前20名）")
    @ApiImplicitParam(name = "year", value = "年份", paramType = "query", dataType = "Long")
    @RequestMapping(value = "/IntersectionOfFields",method = RequestMethod.POST)
    @ResponseBody
    public List<Map<String,Object>> findIntersectionOfFields(@Param("year") Long year){
        List<Map<String,Object>> res = new LinkedList<>();
        //
        List<SubjectCrossoverRank> list_cross = subjectCrossoverRankDAO.getRelatedFields(year);
        for(SubjectCrossoverRank subjectCrossoverRank:list_cross){
            Map<String,Object> sub_res = new HashMap();
            sub_res.put("field",subjectCrossoverRank.getId().getSubject());
            List<Object[]> subjectPaperCounts = subjectPaperCountDAO.getCountByYearSubject(year,subjectCrossoverRank.getId().getSubject());
            sub_res.put("paperNumber",subjectPaperCounts.get(0)[1]);
            sub_res.put("authorNumber",subjectPaperCounts.get(0)[2]);
            res.add(sub_res);
        }
        return res;
    }

    @ApiOperation(value = "",notes="根据输⼊的年份数量和领域，返回近x年和该领域交叉最多的领域前10名（交叉：⼀篇⽂\n" +
            "章同属于两个领域")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "year", value = "年份", paramType = "query", dataType = "Long"),
            @ApiImplicitParam(name = "field", value = "领域", paramType = "query", dataType = "String")})
    @RequestMapping(value = "/RelatedFields",method = RequestMethod.POST)
    @ResponseBody
    public List<Map<String,String>> findRelatedFields(@Param("year") Long year,@Param("field")String field){
        //获得了很多
        List<Map<String,String>> res = new LinkedList<>();
        List<RelativeSubjects> res1= relativeSubjectsDAO.getRelatedFields(year);
        Map<String,Integer> count = new HashMap<>();
        for(RelativeSubjects item:res1){
            String subject1 = item.getId().getSubject1();
            String subject2 = item.getId().getSubject2();
            long paper_count = item.getPaperCount();
            if(subject1.equals(field)){
                if(count.containsKey(subject2)) {
                    int count_s1 = count.get(subject2) + (int) paper_count;
                    count.put(subject2, count_s1);
                }
                else{
                    count.put(subject2,(int)paper_count);
                }
            }
            else if(subject2.equals(field)){
                if(count.containsKey(subject1)) {
                    int count_s2 = count.get(subject1) + (int) paper_count;
                    count.put(subject1, count_s2);
                }
                else{
                    count.put(subject1,(int)paper_count);
                }
            }
        }
        Map<String,Integer> count_sort= MapSortUtil.sortByValueDesc(count);
        int ite_num = 0;
        for(String key:count_sort.keySet()){
            ite_num++;
            if(ite_num==10){
                break;
            }
            Map<String,String> sub_res = new HashMap<>();
            sub_res.put("field",key);
            sub_res.put("paperNumber",String.valueOf(count_sort.get(key)));
            res.add(sub_res);
        }
        return res;
    }

    @ApiOperation(value = "",notes="绘图相关数据")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "year", value = "年份", paramType = "query", dataType = "Long"),
            @ApiImplicitParam(name = "field", value = "领域", paramType = "query", dataType = "String")})
    @RequestMapping(value = "/AuthorConnections",method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> findIntersectionOfFields(@Param("year") Long year,@Param("field")String field){
        Map<String,Object> res = new HashMap<>();
        List<Object> links = new LinkedList();
        List<Object> nodes = new LinkedList();
        Set<String> categories = new HashSet<>();
        List<Collaborations> list_collaboration= collaborationDAO.getRelatedFields(year, field);
        List<AuthorConnectionsAuthor> list_author = authorConnectionsAuthorDAO.getAllByYearField(year, field);
        //先找link，要存入每个作者的合作者有多少，遍历一下
        //每个作者的合作者数
        Map<Long,Integer> collaborators = new HashMap<>();
        for(Collaborations collaboration:list_collaboration) {
            Map<String, String> res_link = new HashMap<>();
            if(collaboration.toString()==""){
                continue;
            }
            long subject_source = collaboration.getSourceAuthor();
            long subject_target = collaboration.getTargetAuthor();
            if(collaborators.containsKey(subject_source)){
                collaborators.put(subject_source,collaborators.get(subject_source)+1);
            }
            else{
                collaborators.put(subject_source,1);
            }
            if(collaborators.containsKey(subject_target)){
                collaborators.put(subject_target,collaborators.get(subject_target)+1);
            }
            else{
                collaborators.put(subject_target,1);
            }
            if (subject_source > subject_target) {
                long temp = 0;
                temp = subject_source;
                subject_source = subject_target;
                subject_target = temp;
            }
            res_link.put("source", String.valueOf(subject_source));
            res_link.put("target", String.valueOf(subject_target));
            links.add(res_link);
        }
        //再添加node
        for(AuthorConnectionsAuthor authorConnectionsAuthor:list_author){
            Map<String, Object> res_node= new HashMap<>();
            res_node.put("category", String.valueOf(authorConnectionsAuthor.getAuthorCategory()));
            res_node.put("id", String.valueOf(authorConnectionsAuthor.getId().getAuthorId()));
            res_node.put("name", authorConnectionsAuthor.getAuthorName());
            res_node.put("collaborators", collaborators.get(authorConnectionsAuthor.getId().getAuthorId()));
            nodes.add(res_node);
            categories.add(String.valueOf(authorConnectionsAuthor.getAuthorCategory()));
        }
        res.put("nodes", nodes);
        res.put("links", links);
        res.put("categories", categories);
        return res;
    }

    @ApiOperation(value = "",notes="返回两个作者之间所有可能的合作关系")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "year", value = "年份", paramType = "query", dataType = "Long"),
            @ApiImplicitParam(name = "field", value = "领域", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "author1", value = "作者1", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "author2", value = "作者2", paramType = "query", dataType = "String")})
    @RequestMapping(value = "/Collaborations",method = RequestMethod.POST)
    @ResponseBody
    public List<List<String>> findCollaborations(@Param("year") Long year,@Param("field") String field,
                                                 @Param("author1") String author1,@Param("author2") String author2){
        List<List<String>> res = new LinkedList<>();

        return res;
    }

}
