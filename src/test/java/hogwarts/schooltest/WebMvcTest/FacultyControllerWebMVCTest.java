package hogwarts.schooltest.WebMvcTest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import hogwarts.schooltest.controller.FacultyController;
import hogwarts.schooltest.model.Faculty;
import hogwarts.schooltest.model.Student;
import hogwarts.schooltest.repository.FacultyRepository;
import hogwarts.schooltest.repository.StudentRepository;
import hogwarts.schooltest.service.FacultyService;
import org.checkerframework.checker.units.qual.A;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.nio.charset.StandardCharsets;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest (controllers = FacultyController.class)
public class FacultyControllerWebMVCTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @SpyBean
    private FacultyService facultyService;

    @MockBean
    private FacultyRepository facultyRepository;

    @MockBean
    private StudentRepository studentRepository;



    @Test
    public void updateTestMvc() throws Exception {
        long id = 1;
        Faculty oldfaculty = new Faculty();
        oldfaculty.setId(id);
        oldfaculty.setName("Слизерин");
        oldfaculty.setColor("green");

        Faculty newfaculty = new Faculty();
        newfaculty.setId(id);
        newfaculty.setName("Гриффиндор");
        newfaculty.setColor("red");

        when(facultyRepository.findById(any())).thenReturn(Optional.of(oldfaculty));
        when(facultyRepository.save(any())).thenReturn(Optional.of(newfaculty));

        mockMvc.perform(
                MockMvcRequestBuilders.put("/faculty/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .content(objectMapper.writeValueAsString(newfaculty))
        ).andExpect(result -> {
            MockHttpServletResponse response = result.getResponse();
            Faculty responseFaculty = objectMapper.readValue(response.getContentAsString(), Faculty.class);
        });

    }

    @Test
    public void getStudentByIdTest() throws Exception {
        long id = 1;
        Faculty faculty = new Faculty();
        faculty.setId(id);
        faculty.setName("Гриффиндор");
        faculty.setColor("red");

        when(facultyRepository.findById(id)).thenReturn(Optional.of(faculty));

        mockMvc.perform(
                        MockMvcRequestBuilders.get("/faculty/{id}", id)
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value("Гриффиндор"))
                .andExpect(jsonPath("$.color").value("red"));
    }

    @Test
    public void deleteStudentByIdTest() throws Exception {
        long id = 1;

        mockMvc.perform(
                        MockMvcRequestBuilders.delete("/faculty/{id}", id)
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isNoContent());

        verify(facultyRepository, times(1)).deleteById(id);
    }

    @Test
    public void createStudentTest() throws Exception {
        Faculty faculty = new Faculty();
        faculty.setId(1L);
        faculty.setName("Гриффиндор");
        faculty.setColor("red");

        when(facultyRepository.save(any(Faculty.class))).thenReturn(faculty);

        mockMvc.perform(
                        MockMvcRequestBuilders.post("/faculty")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(new ObjectMapper().writeValueAsString(faculty))
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Гриффиндор"))
                .andExpect(jsonPath("$.color").value("red"));

        verify(facultyRepository, times(1)).save(any(Faculty.class));
    }
}
