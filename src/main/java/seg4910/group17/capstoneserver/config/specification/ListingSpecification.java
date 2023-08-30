package seg4910.group17.capstoneserver.config.specification;

import org.springframework.data.jpa.domain.Specification;
import seg4910.group17.capstoneserver.dao.Listing;
import seg4910.group17.capstoneserver.dao.Tag;

import javax.persistence.criteria.*;
import java.util.List;

public class ListingSpecification implements Specification<Listing> {

    private SearchCriteria searchCriteria;

    public ListingSpecification(SearchCriteria searchCriteria) {
        this.searchCriteria = searchCriteria;
    }

    @Override
    public Predicate toPredicate(Root<Listing> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
        Join<Listing, Tag> listingTagJoin = root.join("tags");
        query.distinct(true);
        if(searchCriteria.getOperation().equalsIgnoreCase(">=")) {
            return builder.greaterThanOrEqualTo(root.<String> get(searchCriteria.getKey()), searchCriteria.getValue().toString());
        }
        else if(searchCriteria.getOperation().equalsIgnoreCase("<=")) {
            return builder.lessThanOrEqualTo(root.<String> get(searchCriteria.getKey()), searchCriteria.getValue().toString());
        }
        else if(searchCriteria.getOperation().equalsIgnoreCase(":")) {
            if(root.get(searchCriteria.getKey()).getJavaType() == String.class) {
                return builder.like(builder.lower(root.<String> get(searchCriteria.getKey())), "%" + String.valueOf(searchCriteria.getValue()).toLowerCase() + "%");
            }
            else if(root.get(searchCriteria.getKey()).getJavaType() == List.class) {
                return builder.equal(builder.lower(listingTagJoin.<String> get("name")), searchCriteria.getValue());
            }
            else {
                return builder.equal(root.get(searchCriteria.getKey()), searchCriteria.getValue());
            }
        }
        return null;
    }
}
