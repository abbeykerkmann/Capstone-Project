package seg4910.group17.capstoneserver.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import seg4910.group17.capstoneserver.dao.Listing;

import java.util.List;

public interface ListingJpaRepository extends JpaRepository<Listing, Integer>, JpaSpecificationExecutor<Listing> {}
