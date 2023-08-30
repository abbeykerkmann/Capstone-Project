package seg4910.group17.capstoneserver.api.controller.common;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import seg4910.group17.capstoneserver.dao.Tag;
import seg4910.group17.capstoneserver.repository.TagJpaRepository;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/tags")
public class TagsController {

    @Autowired
    private TagJpaRepository tagJpaRepository;

    @GetMapping("")
    public List<Tag> getAllTags() { return tagJpaRepository.findAll(); }

    @GetMapping("/{id}")
    public Tag getTagById(@PathVariable Integer id) { return tagJpaRepository.findById(id).get(); }

    @PostMapping("")
    public Tag save(@Valid @RequestBody final Tag tag) { return tagJpaRepository.save(tag); }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Integer id) { tagJpaRepository.deleteById(id); }
}
