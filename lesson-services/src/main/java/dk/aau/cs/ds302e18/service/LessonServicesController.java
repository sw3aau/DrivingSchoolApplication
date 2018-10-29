package dk.aau.cs.ds302e18.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/lessons")
public class LessonServicesController
{

    private static final Logger LOGGER = LoggerFactory.getLogger(LessonServicesController.class);

    private final LessonRepository repository;

    public LessonServicesController(LessonRepository repository){
        super();
        this.repository = repository;
    }

    @GetMapping
    public List<Lesson> getAllLessons(){
        return new ArrayList<>(this.repository.findAll());
    }

    @PostMapping
    public ResponseEntity<Lesson> addLesson(@RequestBody LessonModel model){
        Lesson lesson = this.repository.save(model.translateModelToLesson());
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(lesson.getId()).toUri();
        return ResponseEntity.created(location).body(lesson);
    }

    @GetMapping("/{id}")
    public Lesson getLesson(@PathVariable Long id){
        Optional<Lesson> lesson = this.repository.findById(id);
        if(lesson.isPresent()){
            return lesson.get();
        }
        throw new LessonNotFoundException("Lesson not found with id: " + id);
    }

    @PutMapping("/{id}")
    public Lesson updateLesson(@PathVariable Long id, @RequestBody LessonModel model){
        Optional<Lesson> existing = this.repository.findById(id);
        if(!existing.isPresent()){
            throw new LessonNotFoundException("Lesson not found with id: " + id);
        }
        Lesson lesson = model.translateModelToLesson();
        lesson.setId(id);
        return this.repository.save(lesson);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.RESET_CONTENT)
    public void deleteLesson(@PathVariable Long id){
        Optional<Lesson> existing = this.repository.findById(id);
        if(!existing.isPresent()){
            throw new LessonNotFoundException("Lesson not found with id: " + id);
        }
        this.repository.deleteById(id);
    }
}
