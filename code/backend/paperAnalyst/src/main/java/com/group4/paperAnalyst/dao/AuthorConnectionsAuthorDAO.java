package com.group4.paperAnalyst.dao;

import com.group4.paperAnalyst.pojo.AuthorConnectionsAuthor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface AuthorConnectionsAuthorDAO extends JpaRepository<AuthorConnectionsAuthor,Integer> {
    @Transactional
    @Modifying
    //pagerank算法？
    @Query(value = "select subject,count(author_id) as count_au,sum(paper_count) as count_paper from author_connections_author where year >="+
            "Year(CURDATE())-:year group by `subject` ORDER BY count_au desc limit 20",nativeQuery = true)
    List<Object[]> getAuthorTitle(@Param("year") Long year);

    @Transactional
    @Modifying
    //
    @Query(value = "select * from author_connections_author where start_year >="+
            "Year(CURDATE())-:year and subject = :field ",nativeQuery = true)
    List<AuthorConnectionsAuthor> getAllByYearField(@Param("year") Long year,@Param("field") String field);

}
