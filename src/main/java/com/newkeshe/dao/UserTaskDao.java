package com.newkeshe.dao;

import com.newkeshe.entity.Task;
import com.newkeshe.entity.User;
import com.newkeshe.entity.UserTask;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface UserTaskDao extends JpaRepository<UserTask,Integer> {
    @Override
    <S extends UserTask> S save(S s);
    @Transactional
    @Modifying
    void deleteByUserAndTask(User user,Task task);

    List<UserTask> findByUserAndTask(User user,Task task);

    @Transactional
    @Modifying
    void deleteById(Integer id);

    List<UserTask> findByUser(User user);
    List<UserTask> findByTask(Task task);
}
