package kameleoon.task.repository;

import kameleoon.task.model.Quote;
import kameleoon.task.model.User;
import kameleoon.task.model.Vote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VoteRepository extends JpaRepository<Vote, Long> {
    Vote findByUserAndQuote(User user, Quote quote);
    @Query("SELECT v FROM Vote v WHERE v.user.id = :userId ORDER BY v.updatedAt DESC LIMIT 10")
    List<Vote> findLast10UpdatedVotesByUserId(Long userId);
}
