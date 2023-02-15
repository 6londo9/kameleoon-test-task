package kameleoon.task.service;

import kameleoon.task.exception.UserNotFoundException;
import kameleoon.task.model.Quote;
import kameleoon.task.model.User;
import kameleoon.task.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public final class UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private QuoteService quoteService;

    public Iterable<User> findAll() {
        return userRepository.findAll();
    }
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }
    public boolean checkIfUserExistByUsername(String username) {
        return userRepository.findByUsername(username).isPresent();
    }

    public User updateUser(long id, User user) {
        User updatedUser = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        updatedUser.setUsername(user.getUsername());
        return userRepository.save(updatedUser);
    }

    public void deleteUser(long id) {
        userRepository.deleteById(id);
    }

    public User createUser(User user) {
        return userRepository.save(user);
    }

    public Quote findMostUpvotedQuote(Long userId) {
        if (userRepository.findById(userId).isPresent()) {
            List<Quote> quotes = quoteService.findMostLikedQuoteByUserId(userId);
            Quote quote = quotes.isEmpty() ? null : quotes.get(0);
            return quote;
        }
        return null;
    }

    public Optional<User> findById(Long userId) {
        return userRepository.findById(userId);
    }

    public Quote findLastAddedQuote(Long userId) {
        return quoteService.findLastAddedQuoteByUserId(userId);
    }
}
