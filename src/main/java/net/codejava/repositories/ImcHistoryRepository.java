package net.codejava.repositories;

import net.codejava.entity.ImcHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ImcHistoryRepository extends JpaRepository<ImcHistory, Long> {
    List<ImcHistory> findAllByOrderByDateDesc();
    List<ImcHistory> findByUsername(String username);
}