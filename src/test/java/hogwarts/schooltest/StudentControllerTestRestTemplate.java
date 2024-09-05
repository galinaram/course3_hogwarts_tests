package hogwarts.schooltest;

import hogwarts.schooltest.controller.StudentController;
import hogwarts.schooltest.model.Student;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;

import java.util.Collection;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class StudentControllerTestRestTemplate {

    @LocalServerPort
    public int port;

    @Autowired
    public StudentController studentController;
    @Autowired
    public TestRestTemplate testRestTemplate;

    @Test
    void contextLoads() throws Exception {
        Assertions.assertThat(studentController).isNotNull();
    }

    @Test
    public void testAuthor() throws Exception {
        Assertions
                .assertThat(this.testRestTemplate.getForObject("http://localhost:" + port + "/students/info" , String.class))
                .isEqualTo("Author of this application is Good person!");
    }

    @Test
    public void testGetStudents() throws Exception{
        Assertions
                .assertThat(this.testRestTemplate.getForObject("http://localhost:" + port + "/students/all", String.class))
                .isNotNull();
    }

    @Test
    public void testPostStudent() throws Exception{
        Student student = new Student();
        student.setId(2);

        Student response = this.testRestTemplate.postForObject("http://localhost:" + port + "/students", student, Student.class);

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getId()).isEqualTo(2);
    }

    @Test
    public void testGetStudentById() throws Exception{
        long existingStudentId = 2;

        ResponseEntity<Student> responseEntity = testRestTemplate.getForEntity("/students/" + existingStudentId, Student.class);

        Assertions.assertThat(200).isEqualTo(responseEntity.getStatusCodeValue());

        Student student = responseEntity.getBody();
        Assertions.assertThat(student).isNotNull();
        Assertions.assertThat(existingStudentId).isEqualTo(student.getId());
    }

    @Test
    public void testEditStudent() throws  Exception{
        Student studentToUpdate = new Student();
        studentToUpdate.setId(1);
        studentToUpdate.setName("new");

        HttpEntity<Student> requestEntity = new HttpEntity<>(studentToUpdate);

        ResponseEntity<Student> responseEntity = testRestTemplate.exchange(
                "/students", // Путь к вашему эндпоинту
                HttpMethod.PUT,
                requestEntity,
                Student.class
        );

        Assertions.assertThat(200).isEqualTo(responseEntity.getStatusCodeValue());

        Student responseStudent = responseEntity.getBody();
        Assertions.assertThat(responseStudent).isNotNull();
        Assertions.assertThat("new").isEqualTo(responseStudent.getName());
    }

    @Test
    public void testDeleteExistingStudent() {
        long existingStudentId = 1;
        ResponseEntity<Void> responseEntity = testRestTemplate.exchange("/students/" + existingStudentId, HttpMethod.DELETE, null, Void.class);

        Assertions.assertThat(200).isEqualTo(responseEntity.getStatusCodeValue());

        ResponseEntity<Student> getResponse = testRestTemplate.getForEntity("/students/" + existingStudentId, Student.class);
        Assertions.assertThat(404).isEqualTo(getResponse.getStatusCodeValue());
    }

    @Test
    public void testGetStudentsWithAgeBetween() {
        Long minAge = 11L;
        Long maxAge = 18L;

        ResponseEntity<Collection<Student>> responseEntity = testRestTemplate.exchange(
                "/students?min=" + minAge + "&max=" + maxAge,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<Collection<Student>>() {}
        );

        Assertions.assertThat(200).isEqualTo(responseEntity.getStatusCodeValue());

        Collection<Student> students = responseEntity.getBody();
        Assertions.assertThat(students).isNotNull();
        for (Student student : students) {
            Assertions.assertThat(student.getAge() >= minAge && student.getAge() <= maxAge).isTrue();
        }
    }

}
