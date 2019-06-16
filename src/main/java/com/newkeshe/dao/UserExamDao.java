package com.newkeshe.dao;

import com.newkeshe.entity.Exam;
import com.newkeshe.entity.User;
import com.newkeshe.entity.UserExam;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface UserExamDao extends JpaRepository<UserExam,Integer> {
    @Override
    <S extends UserExam> S save(S s);

    @Override
    List<UserExam> findAll();

    List<UserExam> findByUserAndExam(User user,Exam exam);
    @Transactional
    @Modifying
    void deleteByUserAndExam(User user, Exam exam);

    @Transactional
    @Modifying
    void deleteById(Integer id);

    List<UserExam> findByExam(Exam exam);
    List<UserExam> findByUser(User user);

    @Query(value = "select count(u.exam) from UserExam u where u.exam.id = ?1")
    Integer findCountExamByExamId(Integer examId);

    @Query(value = "select count(u.user) from UserExam u where u.user.id =?1")
    Integer findCountUserByUserId(Integer userId);
}
