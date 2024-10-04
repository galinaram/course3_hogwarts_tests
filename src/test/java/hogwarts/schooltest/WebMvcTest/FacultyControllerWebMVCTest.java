package hogwarts.schooltest.WebMvcTest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import hogwarts.schooltest.controller.FacultyController;
import hogwarts.schooltest.model.Faculty;
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
import static org.mockito.Mockito.when;

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

}
