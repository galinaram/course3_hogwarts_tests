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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

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

//    @MockBean
//    private AvatarRepository avatarRepository;

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

        when(studentRepository.findById(any())).thenReturn(Optional.of(oldStudent));
        when(studentRepository.save(any())).thenReturn(Optional.of(newStudent));

        mockMvc.perform(
                MockMvcRequestBuilders.put("/student/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newStudent))
        ).andExpect(result -> {
            MockHttpServletResponse response = result.getResponse();
            Student responseFaculty = objectMapper.readValue(response.getContentAsString(), Student.class);
        });
    }
}
