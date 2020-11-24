package com.group4.paperAnalyst.dao;

import com.group4.paperAnalyst.pojo.AuthorCitations;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface AuthorCitationsDAO extends JpaRepository<AuthorCitations,Integer> {
    @Transactional
    @Modifying
    //近几年各领域作者数
    @Query(value = "select count(author) as num from author_citations where year >="+
            "Year(CURDATE())-:year and subject=:subject group by subject",nativeQuery = true)
    List<Object> getAuthornumByYearSubject(@Param("year") Long year, @Param("subject") String subject);

    @Transactional
    @Modifying
    //近几年该领域高引用作者的前10名
    @Query(value = "select author,citations from author_citations where year >="+
            "Year(CURDATE())-:year and subject = :field ORDER BY citations desc limit 10",nativeQuery = true)
    List<Object[]> getAuthorByYearField(@Param("year") Long year, @Param("field") String field);

}
