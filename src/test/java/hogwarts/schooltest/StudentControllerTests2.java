package hogwarts.schooltest;

import hogwarts.schooltest.controller.StudentController;
import hogwarts.schooltest.model.Student;
import hogwarts.schooltest.repository.AvatarRepository;
import hogwarts.schooltest.repository.FacultyRepository;
import hogwarts.schooltest.repository.StudentRepository;
import hogwarts.schooltest.service.AvatarService;
import hogwarts.schooltest.service.StudentService;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.MockBeans;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@WebMvcTest
public class StudentControllerTests2 {
    @Autowired
    public MockMvc mockMvc;

    @MockBean
    private StudentRepository studentRepository;

    @MockBean
    private AvatarRepository avatarRepository;

    @MockBean
    private FacultyRepository facultyRepository;

    @SpyBean
    private StudentService studentService;

    @SpyBean
    private AvatarService avatarService;

    @InjectMocks
    private StudentController studentController;

    @Test
    public void saveStudentTest() throws Exception{
        JSONObject studentObject = new JSONObject();
        studentObject.put("name", "greds");
        studentObject.put("age", 10);

        Student student = new Student();
        student.setId(1);
        student.setName("greds");
        student.setAge(10);

//        when(StudentRepository.save(any(Student.class))).thenReturn(student);
    }
}
