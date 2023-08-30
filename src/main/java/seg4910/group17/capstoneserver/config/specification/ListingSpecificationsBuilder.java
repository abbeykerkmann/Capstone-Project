package seg4910.group17.capstoneserver.config.specification;

import org.springframework.data.jpa.domain.Specification;
import seg4910.group17.capstoneserver.dao.Listing;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ListingSpecificationsBuilder {

    private final List<SearchCriteria> params;

    public ListingSpecificationsBuilder() {
        params = new ArrayList<>();
    }

    public ListingSpecificationsBuilder with(String key, String operation, Object value, String type) {
        params.add(new SearchCriteria(key, operation, value, type));
        return this;
    }

    public Specification<Listing> build() {
        if (params.size() == 0) {
            return null;
        }

        List<Specification> specs = params.stream()
                .map(ListingSpecification::new)
                .collect(Collectors.toList());

        Specification result = specs.get(0);

        for (int i = 1; i < params.size(); i++) {
            result = params.get(i).isOrPredicate()
                    ? Specification.where(result).or(specs.get(i))
                    : Specification.where(result).and(specs.get(i));
        }
        return result;
    }
}
