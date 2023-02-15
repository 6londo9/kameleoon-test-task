package kameleoon.task.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;

import java.time.Instant;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "quotes")
public final class Quote {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Lob
    @NotNull(message = "Your quote must not be empty")
    private String content;
    @CreatedDate
    private Instant createdAt;
    @ManyToOne(cascade = CascadeType.ALL,
        fetch = FetchType.LAZY)
    @JsonIgnore
    private User user;
    @OneToMany(mappedBy = "quote")
    private Set<Vote> votes;
    private Long likeCount;
    private Long dislikeCount;

    public Quote(String content, User user) {
        this.content = content;
        this.user = user;
        this.likeCount = 0L;
        this.dislikeCount = 0L;
    }
}
