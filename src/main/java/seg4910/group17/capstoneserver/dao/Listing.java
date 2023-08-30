package seg4910.group17.capstoneserver.dao;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="tbl_listings")
public class Listing {
    private Listing(){};

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty("id")
    private Integer id;

    @ManyToOne
    @JoinColumn(name="user_id")
    @JsonProperty("user")
    @NotNull
    private User user;

    @Column(name="title")
    @JsonProperty("title")
    private String title;

    @Column(name="available")
    @JsonProperty("available")
    private Boolean available;

    @Column(name="description")
    @JsonProperty("description")
    private String description;

    @Column(name="location")
    @JsonProperty("location")
    private String location;

    @Column(name="price")
    @JsonProperty("price")
    private Double price;

    @Column(name="units")
    @JsonProperty("units")
    private String units;

    @Column(name="quantity")
    @JsonProperty("quantity")
    private Integer quantity;

    @ManyToMany
    @JoinTable(name="tbl_listings_tags",
            joinColumns = @JoinColumn(name="listing_id"),
            inverseJoinColumns = @JoinColumn(name="tag_id"))
    @JsonProperty("tags")
    private List<Tag> tags;

    @OneToMany(mappedBy = "listing", fetch = FetchType.LAZY, cascade={CascadeType.PERSIST, CascadeType.REMOVE, CascadeType.MERGE})
    private List<Image> photos;

  public Listing(User user, String title, Boolean available, String description, String location, Double price,
                 String units, Integer quantity, List<Tag> tags) {
        this.user = user;
        this.available = available;
        this.title = title;
        this.description = description;
        this.location = location;
        this.price = price;
        this.units = units;
        this.quantity = quantity;
        this.tags = tags;
        this.photos = new ArrayList<>();
    }

    public Integer getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public Boolean getAvailable() {
        return available;
    }

    public String getTitle() { return title; }

    public String getDescription() {
        return description;
    }

    public String getLocation() {
        return location;
    }

    public Double getPrice() {
        return price;
    }

    public String getUnits() { return units; }

    public Integer getQuantity() {
        return quantity;
    }

    public List<Tag> getTags() { return tags; }

    public List<Image> getPhotos() { return photos; }

    @JsonProperty("photos")
    public void setPhotos(List<Image> photos) {
        this.photos = photos;
        for(Image image : photos) {
            image.setListing(this);
        }
    }


}
