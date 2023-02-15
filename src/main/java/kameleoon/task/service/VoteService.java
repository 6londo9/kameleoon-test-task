package kameleoon.task.service;

import kameleoon.task.model.Quote;
import kameleoon.task.model.User;
import kameleoon.task.model.Vote;
import kameleoon.task.model.VoteType;
import kameleoon.task.repository.VoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public final class VoteService {

    @Autowired
    private VoteRepository voteRepository;

    public Vote getVote(User user, Quote quote) {
        return voteRepository.findByUserAndQuote(user, quote);
    }

    public Vote createVote(User user, Quote quote, VoteType voteType) {
        Vote vote = new Vote(voteType, user, quote);
        return voteRepository.save(vote);

    }

    public Vote updateVote(User user, Quote quote, VoteType voteType) {
        Vote vote = voteRepository.findByUserAndQuote(user, quote);
        vote.setVoteType(voteType);
        return voteRepository.save(vote);
    }
    public List<Vote> findLast10UpdatedByUser(Long userId) {
        return voteRepository.findLast10UpdatedVotesByUserId(userId);
    }
}
