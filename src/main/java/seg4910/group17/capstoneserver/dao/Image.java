package seg4910.group17.capstoneserver.dao;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;

@Entity
@Table(name="tbl_listing_images")
public class Image {
  private Image(){}

  public Image(String fileName, String content){
    this.name = fileName;
    this.content = content;
  }

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @JsonProperty("name")
  @Column(name="file_name")
  private String name;

  @JsonProperty("content")
  @Column(name="content")
  private String content;

  @ManyToOne
  @JoinColumn(name = "listing_id", nullable = false)
  @JsonIgnore
  private Listing listing;

  public Integer getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public String getContent() {
    return content;
  }

  public Listing getListing() {
    return listing;
  }

  public void setListing(Listing listing) {
    this.listing = listing;
  }

}
