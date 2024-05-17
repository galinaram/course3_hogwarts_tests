package hogwarts.schooltest.controller;

import hogwarts.schooltest.model.Student;
import hogwarts.schooltest.service.AvatarService;
import hogwarts.schooltest.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Collection;

@RestController
@RequestMapping("students")
public class StudentController {
    @Autowired
    public StudentService studentService;
    @Autowired
    public AvatarService avatarService;

    @GetMapping("{id}")
    public ResponseEntity<Student> getStudentInfo(@PathVariable long id){
        Student student = studentService.findStudent(id);
        if (student == null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(student);
    }

    @PostMapping
    public Student createStudent(@RequestBody Student student){
        return studentService.createStudent(student);
    }

    @PutMapping
    public ResponseEntity<Student> editStudent(@RequestBody Student student){
        Student editedStudent = studentService.editStudent(student);
        if (editedStudent == null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(student);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Student> deleteStudent(@PathVariable long id){
        studentService.deleteStudent(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<Collection<Student>> getStudentWithAgeBetween(@RequestParam(required = false) Long min,
                                                                        @RequestParam(required = false) Long max){
        if (min != null && max != null){
            return ResponseEntity.ok(studentService.findByAgeBetween(min, max));
        }
        return ResponseEntity.ok(studentService.getAllStudents());
    }

    @PostMapping(value = "/{id}/avatar", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> createStudent (@PathVariable Long id, @RequestParam MultipartFile avatar) throws IOException {
        if (avatar.getSize() >= 1024 * 300){
            return  ResponseEntity.badRequest().body("File is too big");
        }
        avatarService.uploadAvatar(id, avatar);
        return ResponseEntity.ok().build();
    }
}
