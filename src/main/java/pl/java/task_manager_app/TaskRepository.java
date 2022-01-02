package pl.java.task_manager_app;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {

    @Query("SELECT t FROM Task t WHERE t.isFinish=false")
    List<Task> getAllByFinishIsFalse();

    @Query("SELECT t FROM Task t WHERE t.isFinish=false AND  t.deadLine >= CURRENT_TIMESTAMP  ORDER BY t.deadLine ASC ")
    List<Task> getAllByFinishIsFalseSortByDateAsc();

    @Query("SELECT t FROM Task t WHERE t.isFinish=true")
    List<Task> getAllByFinishIsTrue();

    @Query("SELECT t FROM Task t")
    List<Task> getAll();

}
