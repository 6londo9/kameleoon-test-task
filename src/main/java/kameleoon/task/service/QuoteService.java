package kameleoon.task.service;

import kameleoon.task.exception.QuoteNotFoundException;
import kameleoon.task.model.Quote;
import kameleoon.task.repository.QuoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
public final class QuoteService {

    @Autowired
    QuoteRepository quoteRepository;

    public List<Quote> findAll() {
        return quoteRepository.findAll();
    }

    public Quote findQuote(long id) {
        return quoteRepository.findById(id)
                .orElseThrow(() -> new QuoteNotFoundException("Quote not found"));
    }

    public List<Quote> findMostUpvotedQuotes() {
        return quoteRepository.findMostLikedQuotes();
    }

    public List<Quote> findMostDownvotedQuotes() {
        return quoteRepository.findMostDislikedQuotes();
    }

    public List<Quote> findMostLikedQuoteByUserId(Long userId) {
        return quoteRepository.findMostLikedQuotesForUser(userId);
    }

    public Quote findLastAddedQuoteByUserId(Long userId) {
        return quoteRepository.findUserLastAddedQuote(userId);
    }

    public Optional<Quote> findById(Long id) {
        return quoteRepository.findById(id);
    }

    public Quote createQuote(Quote quote) {
        return quoteRepository.save(quote);
    }

    public Quote findRandomQuote() {
        Random random = new Random();
        List<Quote> quotes = quoteRepository.findAll();
        if (quotes.isEmpty()) {
            return null;
        }
        int index = random.nextInt(quotes.size());

        return quotes.get(index);
    }

    public List<Quote> getLast10Quotes() {
        return quoteRepository.findFirst10ByOrderByCreatedAtDesc();
    }
    public Long getLikesCount(Long quoteId) {
        return quoteRepository.countLikesByQuoteId(quoteId);
    }
    public Long getDislikesCount(Long quoteId) {
        return quoteRepository.countDislikesByQuoteId(quoteId);
    }
}
