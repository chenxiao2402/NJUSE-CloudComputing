package com.group4.paperAnalyst.dao;

import com.group4.paperAnalyst.pojo.RelativeSubjects;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface RelativeSubjectsDAO extends JpaRepository<RelativeSubjects,Integer> {
    @Transactional
    @Modifying
    //选择近几年交叉领域前十名
    @Query(value = "select * from relative_subjects  where start_year >="+
            "Year(CURDATE())-:year",nativeQuery = true)
    List<RelativeSubjects> getRelatedFields(@Param("year") Long year);
}
