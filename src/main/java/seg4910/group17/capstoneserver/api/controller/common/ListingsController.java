package seg4910.group17.capstoneserver.api.controller.common;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import seg4910.group17.capstoneserver.api.model.resource.ListingFilterResource;
import seg4910.group17.capstoneserver.dao.Listing;
import seg4910.group17.capstoneserver.dao.User;
import seg4910.group17.capstoneserver.repository.ListingJpaRepository;
import seg4910.group17.capstoneserver.repository.UserJpaRepository;
import seg4910.group17.capstoneserver.service.ListingService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/listings")
public class ListingsController extends BaseController {

    @Autowired
    private ListingJpaRepository listingJpaRepository;

    @Autowired
    private UserJpaRepository userJpaRepository;

    @Autowired
    private ListingService listingService;

    @GetMapping("")
    public List<Listing> getAll() {
        return listingJpaRepository.findAll();
    }

    @GetMapping("/filter")
    public List<Listing> filter(@RequestBody ListingFilterResource listingFilterResource) {
        return listingService.filterListings(listingFilterResource);
    }

    @GetMapping("/{id}")
    public Listing get(@PathVariable Integer id) {
        return listingJpaRepository.findById(id).get();
    }

    @GetMapping("/user/{user_id}")
    public List<Listing> getByUserId(@PathVariable Integer user_id) {
        User user = userJpaRepository.findById(user_id).get();
        return user.getListings();
    }

    @PostMapping("")
    public Listing save(@RequestBody Listing listing) {
        return listingJpaRepository.save(listing);
    }

    @PutMapping("")
    public Listing updateById(@Valid @RequestBody Listing listing) {
        return listingJpaRepository.save(listing);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Integer id) {
        listingJpaRepository.deleteById(id);
    }
}
