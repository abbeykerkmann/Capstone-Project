package seg4910.group17.capstoneserver.dao;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.List;

@Entity
@Table(name="tbl_tags")
public class Tag {
    protected Tag(){};

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty("id")
    private Integer id;

    @Column(name="name")
    @JsonProperty("name")
    @NotBlank
    private String name;

    @ManyToMany(mappedBy = "tags")
    @JsonIgnore
    private List<Listing> listings;

    public Tag(String name) {
        this.name = name;
    }

    public Integer getId() { return id; }

    public String getName() { return name; }
}
