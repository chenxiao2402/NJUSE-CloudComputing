package com.group4.paperAnalyst.dao;

import com.group4.paperAnalyst.pojo.SubjectPaperCount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
@Repository
public interface SubjectPaperCountDAO extends JpaRepository<SubjectPaperCount,Integer> {
    @Transactional
    @Modifying
    //按照年份获取单一年份最热领域和论文数
    @Query(value = "select * from subject_paper_count where year =:year order by paper_count desc limit 1",nativeQuery = true)
    List<SubjectPaperCount> getPaperNumByYear(@Param("year") Long year);

    @Transactional
    @Modifying
    //按照年月获取该时间最热门的领域
    @Query(value = "select * from subject_paper_count where year =:year and month = :month order by paper_count desc limit 10",nativeQuery = true)
    List<SubjectPaperCount> getFieldTop10Bydate(@Param("year") Long year,@Param("month")Long month);

}