package com.group4.paperAnalyst.dao;

import com.group4.paperAnalyst.pojo.PaperCitations;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
@Repository
public interface PaperCitationsDAO extends JpaRepository<PaperCitations,Integer> {
    @Transactional
    @Modifying
    //近几年各领域文章数
    @Query(value = "select subject,count(title) as num from paper_citations where year >="+
            ":year group by `subject` ORDER BY num desc limit 20",nativeQuery = true)
    List<Object[]> getPapernumByYear(@Param("year") Long year);

    @Transactional
    @Modifying
    //近几年某一领域每年的文章数
    @Query(value = "select year,count(title) as num from paper_citations where year >="+
            ":year and subject = :field group by year ORDER BY year desc",nativeQuery = true)
    List<Object[]> getPapernumByYearField(@Param("year") Long year,@Param("field") String field);

    @Transactional
    @Modifying
    //近几年该领域引用前十名的文章
    @Query(value = "select title,citations from paper_citations where year >="+
            ":year and subject = :field ORDER BY citations desc limit 10",nativeQuery = true)
    List<Object[]> getPapersByYearField(@Param("year") Long year, @Param("field") String field);

    @Transactional
    @Modifying
    //近几年该领域引用前十名的文章
    @Query(value = "select title,citations from paper_citations where year >="+
            ":year and subject = :field ORDER BY citations desc limit 10",nativeQuery = true)
    List<Object[]> getPapersNumByYear(@Param("year") Long year, @Param("field") String field);


}
