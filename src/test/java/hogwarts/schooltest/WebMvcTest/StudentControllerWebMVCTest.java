package hogwarts.schooltest.WebMvcTest;


import com.fasterxml.jackson.databind.ObjectMapper;
import hogwarts.schooltest.controller.StudentController;
import hogwarts.schooltest.model.Faculty;
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

import net.minidev.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(StudentController.class)
public class StudentControllerWebMVCTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @SpyBean
    private StudentService studentService;

    @MockBean
    private StudentRepository studentRepository;

    @MockBean
    private AvatarService avatarService;

    @Test
    public void updateTestMvc() throws Exception {
        long id = 1;
        Student oldStudent = new Student();
        oldStudent.setId(id);
        oldStudent.setAge(17);
        oldStudent.setName("Гермиона");

        Student newStudent = new Student();
        newStudent.setId(id);
        newStudent.setAge(17);
        newStudent.setName("Гарри Поттер");

        when(studentRepository.findById(id)).thenReturn(Optional.of(oldStudent));
        when(studentRepository.save(any())).thenReturn(newStudent);

        mockMvc.perform(
                        MockMvcRequestBuilders.put("/student/{id}", id)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(newStudent))
                ).andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value("Гарри Поттер"));
    }

    @Test
    public void getStudentByIdTest() throws Exception {
        long id = 1;
        Student student = new Student();
        student.setId(id);
        student.setName("Гарри Поттер");
        student.setAge(17);

        // Настройка mock-репозитория
        when(studentRepository.findById(id)).thenReturn(Optional.of(student));

        // Выполнение запроса и проверка результата
        mockMvc.perform(
                        MockMvcRequestBuilders.get("/student/{id}", id)
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value("Гарри Поттер"))
                .andExpect(jsonPath("$.age").value(17));
    }

    @Test
    public void deleteStudentByIdTest() throws Exception {
        long id = 1;

        mockMvc.perform(
                        MockMvcRequestBuilders.delete("/student/{id}", id)
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isNoContent());

        verify(studentRepository, times(1)).deleteById(id);
    }

    @Test
    public void createStudentTest() throws Exception {
        Student student = new Student();
        student.setId(1L);
        student.setName("John Doe");
        student.setAge(20);

        when(studentRepository.save(any(Student.class))).thenReturn(student);

        mockMvc.perform(
                        MockMvcRequestBuilders.post("/student")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(new ObjectMapper().writeValueAsString(student))
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("John Doe"))
                .andExpect(jsonPath("$.age").value(20));

        verify(studentRepository, times(1)).save(any(Student.class));
    }

}
