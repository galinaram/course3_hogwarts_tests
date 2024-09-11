package hogwarts.schooltest.WebMvcTest;


import com.fasterxml.jackson.databind.ObjectMapper;
import hogwarts.schooltest.controller.StudentController;
import hogwarts.schooltest.model.Student;
import hogwarts.schooltest.repository.AvatarRepository;
import hogwarts.schooltest.repository.FacultyRepository;
import hogwarts.schooltest.repository.StudentRepository;
import hogwarts.schooltest.service.AvatarService;
import hogwarts.schooltest.service.StudentService;


import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import net.minidev.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;

import static org.mockito.Mockito.when;

@WebMvcTest(StudentController.class)
public class StudentControllerWebMVCTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;


    @MockBean
    private StudentService studentService;

    @MockBean
    private AvatarService avatarService;

    @Test
    public void getStudentTest() throws Exception{
        Long studentId = 1L;
        Student student = new Student(studentId,"Ivan", 20);

        when(studentService.findStudent(studentId)).thenReturn(student);

        ResultActions perform = mockMvc.perform(get("/students/{all}", studentId));

        perform
                .andExpect(jsonPath("$.name").value("Ivan"))
                .andExpect(jsonPath("$.age").value(20))
                .andDo(print());
    }

    @Test
    public void createStudentTest() throws Exception{
        Long studentId = 1L;
        Student student = new Student(studentId, "Ivan", 20);
        Student savedStudent = new Student(studentId, "Ivan", 20);

        when (studentService.createStudent(student)).thenReturn(savedStudent);

        ResultActions perform = mockMvc.perform(post("/students")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(student)));

        perform
                .andExpect(jsonPath("$.id").value(savedStudent.getId()))
                .andExpect(jsonPath("$.name").value(savedStudent.getName()))
                .andExpect(jsonPath("$.age").value(savedStudent.getAge()))
                .andDo(print());

    }

    @Test
    public void getAllStudentsTest() throws Exception{
//        Student student1 = new Student(1L, "Bob", 13);
//        Student student2 = new Student(2L, "Bin", 15);
//        Collection<Student> students = Arrays.asList(student1, student2);
//
//        when(studentService.getAllStudents()).thenReturn(students);
//
//        mockMvc.perform(get("/students/all") // проверьте путь
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$[0].name").value("Bob"))
//                .andExpect(jsonPath("$[1].name").value("Bin"));
//
//        verify(studentService, times(1)).getAllStudents();
    }

    @Test
    public void saveUserTest() throws Exception {
//        long id = 1L;
//        String name = "Bob";
//
//        JSONObject studentObject = new JSONObject();
//        studentObject.put("name", name);
//
//        Student student = new Student();
//        student.setId(id);
//        student.setName(name);
//
//        when(studentRepository.save(any(Student.class))).thenReturn(student);
//        when(studentRepository.findById(any(Long.class))).thenReturn(Optional.of(student));
//
//        mockMvc.perform(MockMvcRequestBuilders
//                        .post("/user") //send
//                        .content(studentObject.toString())
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .accept(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk()) //receive
//                .andExpect(jsonPath("$.id").value(id))
//                .andExpect(jsonPath("$.name").value(name));
    }
}
