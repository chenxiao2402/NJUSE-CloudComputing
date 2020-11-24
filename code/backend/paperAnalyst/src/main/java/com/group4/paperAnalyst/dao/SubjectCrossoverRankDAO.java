package com.group4.paperAnalyst.dao;

import com.group4.paperAnalyst.pojo.RelativeSubjects;
import com.group4.paperAnalyst.pojo.SubjectCrossoverRank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface SubjectCrossoverRankDAO extends JpaRepository<SubjectCrossoverRank,Integer> {
    @Transactional
    @Modifying
    //选择近几年交叉领域前十名
    @Query(value = "select * from subject_crossover_rank  where start_year >= "+
            "Year(CURDATE())-:year order by crossover_rank desc limit 20",nativeQuery = true)
    List<SubjectCrossoverRank> getRelatedFields(@Param("year") Long year);
}
