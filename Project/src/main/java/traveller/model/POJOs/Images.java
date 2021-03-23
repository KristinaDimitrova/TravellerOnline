package traveller.model.POJOs;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Images {
    @Id
    private int id;
    @Column
    private int postId;
    @Column
    private String url;
}
