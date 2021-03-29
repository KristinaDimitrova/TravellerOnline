package traveller.model.pojo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import traveller.model.pojo.User;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name="verification_token")
public class VerificationToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(nullable = false)
    private String token;
    @Column(name="created_at", nullable = false)
    private LocalDateTime createdAt;
    @Column(name = "expires_at", nullable = false)
    private LocalDateTime expiresAt;
    @Column(name = "confirmed_at", nullable = true)
    private LocalDateTime confirmedAt;

    @JoinColumn(nullable = false, name = "user_id")
    @ManyToOne
    private User user;
    /* //cascadeType.ALL fixes bug :
    Not-null property references a transient value -
     transient instance must be saved before current operation
     Reason : I am trying to save a token into DB without having an actual user with id
     */
    public VerificationToken(User user) {
        token = UUID.randomUUID().toString();
        createdAt = LocalDateTime.now();
        expiresAt = createdAt.plusMinutes(60);
        this.user = user;
    }

}
