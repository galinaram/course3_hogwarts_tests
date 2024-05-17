package hogwarts.schooltest.service;

import hogwarts.schooltest.model.Faculty;
import hogwarts.schooltest.repository.FacultyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class FacultyService {
    @Autowired
    private FacultyRepository facultyRepository;
    public Faculty createFaculty(Faculty faculty){
        return facultyRepository.save(faculty);
    }
    public Faculty findFaculty(long id){
        return facultyRepository.findById(id).get();
    }
    public Faculty editFaculty(Faculty faculty){
        return facultyRepository.save(faculty);
    }
    public void deleteFaculty(long id){
        facultyRepository.deleteById(id);
    }
    public Collection<Faculty> getAllFaculty(){
        return facultyRepository.findAll();
    }

    public Collection<Faculty> findByName(String name){
        return facultyRepository.findFacultyByNameContainsIgnoreCase(name);
    }
    public Collection<Faculty> findByColor(String color){
        return facultyRepository.findFacultyByColorContainsIgnoreCase(color);
    }
}

