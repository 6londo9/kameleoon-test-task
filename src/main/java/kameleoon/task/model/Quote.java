package kameleoon.task.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "quotes")
public class Quote {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Lob
    private String content;
    private int upvotes;
    private int downvotes;
    @ManyToOne(targetEntity = User.class,
    cascade = CascadeType.ALL,
    fetch = FetchType.LAZY)
    private User user;
}
