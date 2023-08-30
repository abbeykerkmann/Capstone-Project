package seg4910.group17.capstoneserver.api.controller.common;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import seg4910.group17.capstoneserver.dao.Image;
import seg4910.group17.capstoneserver.repository.ImageJpaRepository;

import java.util.List;

@RestController
@RequestMapping("/images")
public class ImagesController {

  @Autowired
  private ImageJpaRepository imageJpaRepository;

  @GetMapping
  public List<Image> getAll() {
    return imageJpaRepository.findAll();
  }

  @DeleteMapping("/{id}")
  public void delete(@PathVariable Integer id) {
    imageJpaRepository.deleteById(id);
  }

}
