package seg4910.group17.capstoneserver;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import seg4910.group17.capstoneserver.dao.Image;
import seg4910.group17.capstoneserver.dao.Listing;
import seg4910.group17.capstoneserver.dao.Tag;
import seg4910.group17.capstoneserver.dao.User;
import seg4910.group17.capstoneserver.repository.ListingJpaRepository;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

public class ListingEntityTest extends BaseEntityTest {

    @Autowired
    private ListingJpaRepository listingRepo;

    @Test
    public void testPersistsOneListingEntity() {
        User user = entityManager.persistAndFlush(new User("admin", "admin@mcadmin.com",
                "admin", "McAdmin", "password"));
        Tag tag = entityManager.persistAndFlush(new Tag("eggplants"));
        List<Tag> tags = new ArrayList<>();
        tags.add(tag);
        Listing listing = entityManager.persistAndFlush(new Listing(user, "Tomatoes", true, "Large red tomatoes from my backyard garden", "Mississauga, ON", 5.00, "each", 20, tags));
        // make sure it got an id
        Integer id = listing.getId();
        assertThat(id).isNotNull();
        assertThat(listingRepo.count()).isEqualTo(1);

        // retrieve
        Listing savedListing = listingRepo.getOne(id);
        assertThat(listing.getAvailable()).isEqualTo(true);
        assertThat(listing.getTitle()).isEqualTo("Tomatoes");
        assertThat(listing.getDescription()).isEqualTo("Large red tomatoes from my backyard garden");
        assertThat(listing.getLocation()).isEqualTo("Mississauga, ON");
        assertThat(listing.getPrice()).isEqualTo(5.00);
        assertThat(listing.getUnits()).isEqualTo("each");
        assertThat(listing.getQuantity()).isEqualTo(20);
        assertThat(listing.getUser().getId()).isNotNull().isEqualTo(user.getId());
        assertThat(listing.getTags().get(0).getName()).isEqualTo(tag.getName());
    }

    @Test
    public void testCascadePersistsImages() {
        User user = entityManager.persistAndFlush(new User("admin", "admin@mcadmin.com",
                "admin", "McAdmin", "password"));
        Listing listing = new Listing(user, "Tomatoes", true,
                "Large red tomatoes from my backyard garden", "Mississauga, ON", 5.00, "each", 20, null);

        // looks messy, but basically just reads input file into a string
        String filename = "base64image.jpeg";
        String imageContent = new BufferedReader(
                    new InputStreamReader(Objects.requireNonNull(this.getClass().getResourceAsStream("/base64image.txt")))
                )
                .lines()
                .collect(Collectors.joining());
        Image image = new Image(
                filename,
                imageContent
        );
        List<Image> photos = new ArrayList<>();
        photos.add(image);
        listing.setPhotos(photos);
        entityManager.persistAndFlush(listing);

        Listing savedListing = listingRepo.getOne(listing.getId());
        Image savedImage = savedListing.getPhotos().get(0);
        assertThat(savedImage.getId()).isNotNull();
        assertThat(savedImage.getName()).isEqualTo(filename);
        assertThat(savedImage.getContent()).isEqualTo(imageContent);
        assertThat(savedImage.getListing()).isEqualTo(listing);
    }


}
