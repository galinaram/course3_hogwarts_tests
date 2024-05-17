package hogwarts.schooltest.controller;

import hogwarts.schooltest.model.Faculty;
import hogwarts.schooltest.service.FacultyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("faculty")
public class FacultyController {
    @Autowired
    private FacultyService facultyService;

    @GetMapping("{id}")
    public ResponseEntity<Faculty> getFacultyInfo(@PathVariable long id){
        Faculty faculty = facultyService.findFaculty(id);
        if (faculty == null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(faculty);
    }

    @PostMapping
    public Faculty createFaculty(@RequestBody Faculty faculty){
        return facultyService.createFaculty(faculty);
    }

    @PutMapping
    public ResponseEntity<Faculty> editFaculty(@RequestBody Faculty faculty){
        Faculty editedFaculty = facultyService.editFaculty(faculty);
        if (editedFaculty == null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(editedFaculty);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Faculty> deleteFaculty(@PathVariable long id){
        facultyService.deleteFaculty(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity findFaculty(@RequestParam(required = false) String name,
                                      @RequestParam(required = false) String color){
        if(name!=null && !name.isBlank()){
            return ResponseEntity.ok(facultyService.findByName(name));
        }
        if(color!=null && !color.isBlank()){
            return ResponseEntity.ok(facultyService.findByColor(color));
        }
        return ResponseEntity.ok(facultyService.getAllFaculty());
    }
}
