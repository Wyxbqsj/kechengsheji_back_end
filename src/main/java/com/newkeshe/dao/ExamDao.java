package com.newkeshe.dao;

import com.newkeshe.entity.Exam;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Optional;

@Repository
public interface ExamDao extends JpaRepository<Exam,Integer> {
    Optional<Exam> findById (Integer ivgId);

    @Override
    <S extends Exam> S save(S s);
    @Transactional
    @Modifying
    void deleteById(Integer ivgId);

}
