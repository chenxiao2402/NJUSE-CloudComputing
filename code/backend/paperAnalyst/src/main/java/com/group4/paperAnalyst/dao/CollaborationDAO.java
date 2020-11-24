package com.group4.paperAnalyst.dao;

import com.group4.paperAnalyst.pojo.Collaborations;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.List;

public interface CollaborationDAO extends JpaRepository<Collaborations,Integer> {
    @Transactional
    @Modifying
    //
    @Query(value = "select * from collaborations  where start_year ="+
            ":year and subject = :field",nativeQuery = true)
    List<Collaborations> getRelatedFields(@Param("year") Long year,@Param("field")String field);
}
