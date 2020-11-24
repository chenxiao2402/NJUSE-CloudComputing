package com.group4.paperAnalyst.dao;

import com.group4.paperAnalyst.pojo.SubjectPaperCount;
import com.group4.paperAnalyst.vo.YearSubjectPaperCountVO;
import com.group4.paperAnalyst.vo.PopularFieldsVO;
import com.group4.paperAnalyst.vo.YearPaperCount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface SubjectPaperCountDAO extends JpaRepository<SubjectPaperCount, Integer> {
    @Transactional
    @Modifying
    //按照年份获取单一年份最热领域和论文数
    @Query(value = "select * from subject_paper_count where year =:year order by paper_count desc limit 1", nativeQuery = true)
    List<SubjectPaperCount> getPaperNumByYear(@Param("year") Long year);

    @Transactional
    @Modifying
    //按照年月获取该时间最热门的领域
    @Query(value = "select * from subject_paper_count where year =:year and month = :month order by paper_count desc limit 10", nativeQuery = true)
    List<SubjectPaperCount> getFieldTop10Bydate(@Param("year") Long year, @Param("month") Long month);

    @Query(value = "select new com.group4.paperAnalyst.vo.YearPaperCount(s.id.year, sum(s.paperCount)) " +
            "from SubjectPaperCount s where s.id.year >= 2010 group by s.id.year")
    List<YearPaperCount> getYearPaperCount();

    @Query("select s " +
            "from SubjectPaperCount s where s.id.year = :year order by s.id.month, s.paperCount desc")
    List<SubjectPaperCount> getPopularFieldRankingByYear(@Param("year") Long year);

    @Query("select new com.group4.paperAnalyst.vo.PopularFieldsVO(s.id.subject, sum(s.paperCount), sum(s.authorCount)) " +
            "from SubjectPaperCount s where s.id.year >= :year group by s.id.subject")
    List<PopularFieldsVO> getPopularFieldsInfoAfterYear(@Param("year") Long year);

    @Query("select new com.group4.paperAnalyst.vo.YearSubjectPaperCountVO(s.id.year, s.id.subject, sum(s.paperCount)) " +
            "from SubjectPaperCount s where s.id.year >= :year " +
            "group by s.id.year, s.id.subject ")
    List<YearSubjectPaperCountVO> getYearSubjectPaperCount(@Param("year") Long year);

    @Query("select new com.group4.paperAnalyst.vo.YearSubjectPaperCountVO(s.id.year, s.id.subject, sum(s.paperCount)) " +
            "from SubjectPaperCount s " +
            "where s.id.year >= :year and s.id.subject = :field " +
            "group by s.id.year")
    List<YearSubjectPaperCountVO> getSubjectYearTrend(@Param("year") Long year, @Param("field") String field);

    @Transactional
    @Modifying
    @Query(value = "select subject,paper_count,author_count from subject_paper_count where subject = :subject and "
            + "year >= :year", nativeQuery = true)
    List<Object[]> getCountByYearSubject(@Param("year") Long year, @Param("subject") String subject);
}
