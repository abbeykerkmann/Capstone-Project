package seg4910.group17.capstoneserver;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import seg4910.group17.capstoneserver.api.controller.common.ListingsController;
import seg4910.group17.capstoneserver.api.controller.common.TagsController;
import seg4910.group17.capstoneserver.api.model.resource.ListingFilterResource;
import seg4910.group17.capstoneserver.dao.Listing;
import seg4910.group17.capstoneserver.dao.Tag;
import seg4910.group17.capstoneserver.dao.User;
import seg4910.group17.capstoneserver.repository.ListingJpaRepository;
import seg4910.group17.capstoneserver.repository.TagJpaRepository;
import seg4910.group17.capstoneserver.service.UserService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class ListingControllerTest extends BaseAppTest {

    @Autowired
    ListingsController listingsController;

    @Autowired
    TagJpaRepository tagJpaRepository;

    @Autowired
    UserService userService;

    private final String password = "userpassword";

    @Test
    void givenListingExists_whenSearchListing_thenReturnSingleListing() {
        User user = persistAUser();
        Tag tag = persistATag();
        List<Tag> tags1 = new ArrayList<>();
        tags1.add(tag);
        Listing listing1 = listingsController.save(new Listing(user, "Eggplants", true, "Large purple eggplants from my backyard garden", "Mississauga, ON", 5.00, "each", 20, tags1));
        List<String> keywords = Arrays.asList("eggplants");
        ListingFilterResource listingFilterResource = new ListingFilterResource(keywords, 5.0, 5.0, "mississauga");
        List<Listing> listingsFilter = listingsController.filter(listingFilterResource);
        assertThat(listingsFilter.get(0).getId()).isEqualTo(listing1.getId());
    }

    @Test
    void givenListingsExist_whenSearchListings_thenReturnMultipleListings() {
        User user = persistAUser();
        Tag tag = persistATag();
        List<Tag> tags1 = new ArrayList<>();
        tags1.add(tag);
        Tag carrotTag = tagJpaRepository.findById(4).get();
        List<Tag> tags2 = new ArrayList<>();
        tags2.add(carrotTag);
        tags2.add(tag);
        Listing listing1 = listingsController.save(new Listing(user, "Eggplants", true, "Large purple eggplants from my backyard garden", "Mississauga, ON", 5.00, "each", 20, tags1));
        Listing listing2 = listingsController.save(new Listing(user, "Carrots and Eggplants!", true, "Fresh carrots and eggplants grown in my backyard", "Ottawa, ON", 2.00, "each", 15, tags2));
        List<String> keywords = Arrays.asList("eggplants");
        ListingFilterResource listingFilterResource = new ListingFilterResource(keywords, 2.00, 10.00, "");
        List<Listing> listingsFilter = listingsController.filter(listingFilterResource);
        assertThat(listingsFilter.size()).isEqualTo(2);
        assertThat(listingsFilter.get(0).getId()).isEqualTo(listing1.getId());
        assertThat(listingsFilter.get(1).getId()).isEqualTo(listing2.getId());
    }

    @Test
    void givenListingsExist_whenSearchListings_thenReturnNoListings() {
        User user = persistAUser();
        Tag tag = persistATag();
        List<Tag> tags1 = new ArrayList<>();
        tags1.add(tag);
        Tag carrotTag = tagJpaRepository.findById(4).get();
        List<Tag> tags2 = new ArrayList<>();
        tags2.add(carrotTag);
        Listing listing1 = listingsController.save(new Listing(user, "Eggplants", true, "Large purple eggplants from my backyard garden", "Mississauga, ON", 5.00, "each", 20, tags1));
        Listing listing2 = listingsController.save(new Listing(user, "Carrots", true, "Fresh carrots grown in my backyard", "Ottawa, ON", 2.00, "each", 15, tags2));
        List<String> keywords = Arrays.asList("farm");
        ListingFilterResource listingFilterResource = new ListingFilterResource(keywords, 2.00, 10.00, "");
        List<Listing> listingsFilter = listingsController.filter(listingFilterResource);
        assertThat(listingsFilter.size()).isEqualTo(0);
    }

    @Test
    void givenListingsExist_whenSearchListings_thenReturnAvailableListings() {
        User user = persistAUser();
        Tag tag = persistATag();
        List<Tag> tags1 = new ArrayList<>();
        tags1.add(tag);
        Tag carrotTag = tagJpaRepository.findById(4).get();
        List<Tag> tags2 = new ArrayList<>();
        tags2.add(carrotTag);
        Listing listing1 = listingsController.save(new Listing(user, "Eggplants", true, "Large purple eggplants from my backyard garden", "Mississauga, ON", 5.00, "each", 20, tags1));
        Listing listing2 = listingsController.save(new Listing(user, "Carrots and Eggplants!", false, "Fresh carrots and eggplants grown in my backyard", "Ottawa, ON", 2.00, "each", 15, tags2));
        List<String> keywords = Arrays.asList("eggplants");
        ListingFilterResource listingFilterResource = new ListingFilterResource(keywords, 0.0, 99.0, "");
        List<Listing> listingsFilter = listingsController.filter(listingFilterResource);
        assertThat(listingsFilter.size()).isEqualTo(1);
        assertThat(listingsFilter.get(0).getId()).isEqualTo(listing1.getId());
    }

    private User persistAUser() {
        return userService.createUser(new User("user", "user@user.com",
                "user", "McUser", password));
    }

    private Tag persistATag() {
        return tagJpaRepository.save(new Tag("eggplants"));
    }
}
