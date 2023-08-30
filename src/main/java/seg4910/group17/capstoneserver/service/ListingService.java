package seg4910.group17.capstoneserver.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import seg4910.group17.capstoneserver.api.model.resource.ListingFilterResource;
import seg4910.group17.capstoneserver.config.specification.ListingSpecificationsBuilder;
import seg4910.group17.capstoneserver.dao.Listing;
import seg4910.group17.capstoneserver.repository.ListingJpaRepository;

import java.util.List;

@Service
public class ListingService {

    @Autowired
    private ListingJpaRepository listingJpaRepository;

    public List<Listing> filterListings(ListingFilterResource listingFilterResource) {
        ListingSpecificationsBuilder builder = new ListingSpecificationsBuilder();
        builder.with("available", ":", true, "and");

        if(listingFilterResource.getKeywords() != null) {
            for(String keyword : listingFilterResource.getKeywords()) {
                builder.with("title", ":", keyword, "and");
                builder.with("tags", ":", keyword, "and");
                builder.with("description", ":", keyword, "and");
            }
        }

        if(listingFilterResource.getPriceLow() != null) {
            System.out.println(listingFilterResource.getPriceLow());
            builder.with("price", ">=", listingFilterResource.getPriceLow(), "and");
        }

        if(listingFilterResource.getPriceHigh() != null) {
            System.out.println(listingFilterResource.getPriceHigh());
            builder.with("price", "<=", listingFilterResource.getPriceHigh(), "and");
        }

        if(listingFilterResource.getLocation() != null) {
            System.out.println(listingFilterResource.getLocation());
            builder.with("location", ":", listingFilterResource.getLocation(), "and");
        }

        Specification<Listing> spec = builder.build();
        return listingJpaRepository.findAll(spec);
    }
}
