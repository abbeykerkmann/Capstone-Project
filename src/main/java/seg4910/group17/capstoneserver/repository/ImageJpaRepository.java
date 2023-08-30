package seg4910.group17.capstoneserver.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import seg4910.group17.capstoneserver.dao.Image;

public interface ImageJpaRepository extends JpaRepository<Image, Integer> {

}
