package hogwarts.schooltest.testRestTemplate;

import hogwarts.schooltest.controller.FacultyController;
import hogwarts.schooltest.model.Faculty;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;

import java.util.Collection;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class FacultyControllerTestRestTemplate {

    @LocalServerPort
    public int port;

    @Autowired
    public TestRestTemplate testRestTemplate;

    @Autowired
    public FacultyController facultyController;

    @Test
    public void getAllFacultyTest() throws Exception{
        Assertions
                .assertThat(this.testRestTemplate.getForObject("http://localhost:" + port + "/faculty/all", String.class)).isNotNull();
    }

    @Test
    public void getFacultyByIdTest() throws Exception{
        long existingFacultyId = 1;

        ResponseEntity<Faculty> responseEntity = this.testRestTemplate.getForEntity("/faculty/" + existingFacultyId, Faculty.class);

        Assertions.assertThat(200).isEqualTo(responseEntity.getStatusCodeValue());

        Faculty faculty = responseEntity.getBody();
        Assertions.assertThat(faculty).isNotNull();
        Assertions.assertThat(faculty.getId()).isEqualTo(existingFacultyId);
    }

    @Test
    public void createFacultyTest() throws Exception{
        Faculty faculty = new Faculty();
        faculty.setId(2);

        Faculty response = this.testRestTemplate.postForObject("http://localhost:" + port + "/faculty", faculty, Faculty.class);

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getId()).isEqualTo(2);
    }

    @Test
    public void deleteExistingFacultyTest() throws Exception{
        long existingFacultyId = 1;
        ResponseEntity<Void> responseEntity = testRestTemplate.exchange("/faculty/" + existingFacultyId, HttpMethod.DELETE, null, Void.class);

        Assertions.assertThat(200).isEqualTo(responseEntity.getStatusCodeValue());

        ResponseEntity<Faculty> getResponse = testRestTemplate.getForEntity("/faculty" + existingFacultyId, Faculty.class);

        Assertions.assertThat(404).isEqualTo(getResponse.getStatusCodeValue());
    }

    @Test
    public void findFacultyByColorAndName() throws Exception{
        String color = "красный";
        String name = "Слизерин";

        ResponseEntity<Collection<Faculty>> response = testRestTemplate.exchange(
                "/faculty?name=" + name + "&color=" + color,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<Collection<Faculty>>() {
                }
        );

        Assertions.assertThat(200).isEqualTo(response.getStatusCodeValue());

        Collection<Faculty> faculties = response.getBody();

        Assertions.assertThat(faculties).isNotNull();
        for (Faculty faculty : faculties)
            {
                Assertions.assertThat(faculty.getName().equals(name) || faculty.getColor().equals(color)).isTrue();
            }
    }
}
