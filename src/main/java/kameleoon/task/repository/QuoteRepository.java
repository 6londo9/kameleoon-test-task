package kameleoon.task.repository;

import kameleoon.task.model.Quote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuoteRepository extends JpaRepository<Quote, Long> {
    @Query("SELECT q FROM Quote q JOIN q.votes v WHERE v.voteType = 'LIKE' GROUP BY q.id ORDER BY COUNT (v.id) DESC")
    List<Quote> findMostLikedQuotes();
    @Query("SELECT q FROM Quote q JOIN q.votes v WHERE v.voteType = 'DISLIKE' GROUP BY q.id ORDER BY COUNT (v.id) DESC")
    List<Quote> findMostDislikedQuotes();
    @Query("SELECT q FROM Quote q JOIN q.votes v WHERE q.user.id = :userId "
            + "AND v.voteType = 'LIKE' GROUP BY q.id ORDER BY COUNT(v) DESC")
    List<Quote> findMostLikedQuotesForUser(Long userId);
    @Query("SELECT q FROM Quote q WHERE q.user.id = :userId ORDER BY q.createdAt DESC")
    Quote findUserLastAddedQuote(Long userId);
    List<Quote> findFirst10ByOrderByCreatedAtDesc();
    @Query("SELECT COUNT(v) FROM Quote q JOIN q.votes v WHERE q.id = :quoteId AND v.voteType = 'LIKE'")
    Long countLikesByQuoteId(Long quoteId);

    @Query("SELECT COUNT(v) FROM Quote q JOIN q.votes v WHERE q.id = :quoteId AND v.voteType = 'DISLIKE'")
    Long countDislikesByQuoteId(Long quoteId);
}
