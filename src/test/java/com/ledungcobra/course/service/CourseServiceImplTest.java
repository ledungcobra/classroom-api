package com.ledungcobra.course.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ledungcobra.common.*;
import com.ledungcobra.course.entity.*;
import com.ledungcobra.course.repository.*;
import com.ledungcobra.dto.common.UpdateGradeSpecificArgs;
import com.ledungcobra.dto.course.createNewAssignment.CreateNewAssignmentArgs;
import com.ledungcobra.dto.course.postCreateCourse.CreateCourseRequest;
import com.ledungcobra.dto.course.postUpdateAssignmentNormal.UpdateGradeNormalRequest;
import com.ledungcobra.dto.course.postUpdateAssignmentNormal.UpdateGradeSpecificRequestBase;
import com.ledungcobra.dto.user.register.UserResponse;
import com.ledungcobra.exception.NotFoundException;
import com.ledungcobra.user.repository.UserRepository;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.flywaydb.test.annotation.FlywayTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ActiveProfiles;

import java.time.Instant;
import java.util.List;

import static com.ledungcobra.controller.CourseControllerTest.updateAssignmentsRequest_Success;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles({"default", "test"})
@FlywayTest
@ComponentScan(basePackages = {
        "com.ledungcobra.user",
        "com.ledungcobra.configuration.beans",
        "com.ledungcobra.course",
        "com.ledungcobra.common",
        "com.ledungcobra.configuration.database",
})
@Slf4j
class CourseServiceImplTest {

    @Autowired
    private CourseService courseService;

    @Autowired
    private AssignmentRepository assignmentRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private CourseStudentRepository courseStudentRepository;

    @Autowired
    private GradeRepository gradeRepository;

    @Autowired
    private CourseUserRepository courseUserRepository;

    @Autowired
    private UserRepository userRepository;

    private ObjectMapper objectMapper = new ObjectMapper();

    public static CreateCourseRequest createCourse_success = CreateCourseRequest.builder()
            .credits(7)
            .currentUser("tanhank2k")
            .description("test")
            .gradeId(0)
            .role(0)
            .title("Schedule")
            .schedule("Schedule")
            .subjectId(0)
            .build();
    ;

    @Test
    void getCoursesByTitleLike() {
        var pageRequest = new OffsetPageable(0, Integer.MAX_VALUE, Sort.by(Sort.Direction.ASC, "id"));
        List<Course> courses = courseService.getCoursesByTitleLike("%%", pageRequest, "tanhank2k");
        log.info("courses size {}", courses.size());
        assertThat(courses.size()).isEqualTo(7);
    }

    @Test
    void createCourse() {
        Course savedCourse = courseService.createCourse(createCourse_success);
        assertThat(savedCourse.getTitle()).isNotNull().isNotBlank();
        assertThat(savedCourse).isNotNull();
        CourseUser courseUser = courseUserRepository.findCourseUserByUserIdAndCourseId(userRepository.findByUserName("tanhank2k").get().getId(), savedCourse.getId());
        assertThat(courseUser.getCreateBy()).isEqualTo("tanhank2k");
    }

    @Test
    void countByOwner() {
        var count = courseService.countByOwner("tanhank2k");
        assertThat(count).isEqualTo(7);
    }

    @Test
    void findCourseById() {
        var foundCourse = courseService.findCourseById("tanhank2k", 1);
        assertThat(foundCourse).isNotNull();
        assertThat(foundCourse.getId()).isEqualTo(1);
        assertThat(foundCourse.getTitle()).isNotNull().isNotBlank();
        assertThat(foundCourse.getCreateBy()).isEqualTo("tanhank2k");
        assertThat(foundCourse.getCourseCode()).isNotNull().isNotBlank();
        assertThat(foundCourse.getDescription()).isNotNull().isNotBlank();

    }

    @Test
    void findAssignmentOfsCourseLikeName() {
        var page = PageableBuilder.getPageable(0, 1000, null);
        var assignments = courseService.findAssignmentOfsCourseLikeName("tanhank2k", 1, "%%", page);
        assertThat(assignments).isNotNull();
        assertThat(assignments.size()).isEqualTo(4);
        assignments = courseService.findAssignmentOfsCourseLikeName("tanhank2k", 1, "%tuần%", page);
        assertThat(assignments).isNotNull();
        assertThat(assignments.size()).isEqualTo(4);
    }

    @Test
    void findAssigmentById() {
        Assignment assignment = courseService.findAssigmentById(1, 1);
        assertThat(assignment).isNotNull();
    }

    @SneakyThrows
    @Test
    void updateAssignmentShould_Success() {
        var updateAssignment = courseService.updateAssignment(updateAssignmentsRequest_Success, "tanhank2k", 1);
        assertThat(updateAssignment).isNotNull();
        assertThat(updateAssignment.getName()).isEqualTo(updateAssignmentsRequest_Success.getName());
        assertThat(updateAssignment.getDescription()).isEqualTo(updateAssignmentsRequest_Success.getDescription());
        assertThat(updateAssignment.getMaxGrade()).isEqualTo(updateAssignmentsRequest_Success.getMaxGrade());
        assertThat(updateAssignment.getId()).isEqualTo(1);
    }

    @Test
    void updateAssignmentShould_Throw() {
        assertThatThrownBy(() -> {
            courseService.updateAssignment(updateAssignmentsRequest_Success, "tanhank2k", 9999);
        }).isInstanceOf(NotFoundException.class);
    }

    @Test
    @SneakyThrows
    void deleteAssignmentShould_Success() {
        courseService.deleteAssignment(21);
        Assignment assigment = courseService.findAssigmentById(5, 21);
        assertThat(assigment).isNull();
    }

    @Test
    void deleteAssignmentHaveGradeShould_Fail() {
        assertThatThrownBy(() -> {
            courseService.deleteAssignment(1);
            courseService.findAssigmentById(1, 1);
        }).isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    void deleteAssignmentShould_Throw() {
        assertThatThrownBy(() -> {
            courseService.deleteAssignment(999);
        }).isInstanceOf(NotFoundException.class);
        assertThat(courseService.findAssigmentById(1, 1)).isNotNull();
    }

    @Test
    void getAllGrades() {
        var result = courseService.getAllGrades(1);
        assertThat(result).isNotNull();
        assertThat(result.size()).isEqualTo(5);
        var first = result.stream().filter(r -> r.getId() != null && r.getId() == 1).findFirst().get();
        var second = result.stream().filter(r -> r.getId() != null && r.getId() == 2).findFirst().get();
        var third = result.stream().filter(r -> r.getId() != null && r.getId() == 3).findFirst().get();
        var fourth = result.stream().filter(r -> r.getId() != null && r.getId() == 4).findFirst().get();
        var fifth = result.stream().filter(r -> r.getId() != null && r.getId() == 5).findFirst().get();

        assertThat(first.getMssv()).isEqualTo("1");
        assertThat(first.getName()).isEqualTo("Bùi Tấn Hạnh");
        assertThat(first.getGrades().size()).isEqualTo(4);
        assertThat(second.getMssv()).isEqualTo("2");
        assertThat(second.getName()).isEqualTo("Lê Quốc Dũng");
        assertThat(second.getGrades().size()).isEqualTo(3);
        assertThat(third.getMssv()).isEqualTo("3");
        assertThat(third.getName()).isEqualTo("Phạm Minh Anh Hữu");
        assertThat(third.getGrades().size()).isEqualTo(3);

        assertThat(fourth.getId()).isEqualTo(4);
        assertThat(fourth.getMssv()).isEqualTo("4");
        assertThat(fourth.getName()).isEqualTo("Thằng nào đó");
        assertThat(fourth.getGrades().size()).isEqualTo(0);

        assertThat(fifth.getId()).isEqualTo(5);
        assertThat(fifth.getMssv()).isEqualTo("5");
        assertThat(fifth.getName()).isEqualTo("Ahihi");
        assertThat(fifth.getGrades().size()).isEqualTo(0);

    }

    @Test
    @SneakyThrows
    void getGradeOfCourseByStudentTableId() {
        var result = courseService.getGradeOfCourseByStudentTableId(1, 1);
        log.info("Result {}", result);
        assertThat(result).isNotNull();
        assertThat(result.getGrades()).isNotEmpty();
        assertThat(result.getName()).isEqualTo("Bùi Tấn Hạnh");
    }

    @Test
    void getAllGradeAssignment() {
        var result = courseService.getAllGradeAssignment(1);
        assertThat(result).isNotNull();
        assertThat(result.size()).isEqualTo(3);
    }

    @Test
    void saveGradeV1() {

        var assignment = assignmentRepository.findById(1).orElseThrow();
        var studentId = "1234567";
        var student = studentRepository.findByStudentId(studentId);
        assertThat(student).isNull();
        var g = Grade.builder()
                .assigment(assignment)
                .description("")
                .gradeAssignment(10.0f)
                .mssv(studentId)
                .isFinalized((byte) 0)
                .student(student)
                .build();

        var savedGrade = courseService.saveGrade(g, "tanhank2k", 1, 1);
        assertThat(savedGrade).isNotNull();
        assertThat(savedGrade.getMssv()).isEqualTo(studentId);
        assertThat(savedGrade.getAssigment()).isNotNull();
        assertThat(savedGrade.getAssigment().getId()).isEqualTo(assignment.getId());
        var dbStudent = studentRepository.findByStudentId(studentId);
        assertThat(savedGrade.getGradeAssignment()).isEqualTo(g.getGradeAssignment());
        assertThat(dbStudent).isNotNull();
        assertThat(savedGrade.getIsFinalized()).isEqualTo((byte) 0);
    }

    @Test
    void saveGradeV2() {

        var assignment = assignmentRepository.findById(1).orElseThrow();
        var studentId = "1";
        var student = studentRepository.findByStudentId(studentId);

        assertThat(student).isNotNull();
        var g = Grade.builder()
                .assigment(assignment)
                .description("")
                .gradeAssignment(10.0f)
                .mssv(studentId)
                .isFinalized((byte) 0)
                .student(student)
                .build();

        var savedGrade = courseService.saveGrade(g, "tanhank2k", 1, 1);
        assertThat(savedGrade).isNotNull();
        assertThat(savedGrade.getMssv()).isEqualTo(studentId);
        assertThat(savedGrade.getAssigment()).isNotNull();
        assertThat(savedGrade.getAssigment().getId()).isEqualTo(assignment.getId());
        var dbStudent = studentRepository.findByStudentId(studentId);
        assertThat(savedGrade.getGradeAssignment()).isEqualTo(g.getGradeAssignment());
        assertThat(dbStudent).isNotNull();
        assertThat(savedGrade.getIsFinalized()).isEqualTo((byte) 0);
    }


    @Test
    void updateStudentsInCourse() {
        var foundStudent = studentRepository.findByStudentId("1");
        assertThat(foundStudent).isNotNull();
        var updateStudent = Student.builder()
                .fullName("Test")
                .studentId("1")
                .dateOfBirth(Instant.now())
                .build();
        courseService.updateStudentsInCourse(List.of(updateStudent), 1, "tanhank2k");
        foundStudent = studentRepository.findByStudentId("1");
        assertThat(foundStudent.getFullName()).isEqualTo(updateStudent.getFullName());
    }

    @Test
    void updateStudentsInCourseV2() {
        var studentID = "999";
        var foundStudent = studentRepository.findByStudentId(studentID);
        assertThat(foundStudent).isNull();
        var updateStudent = Student.builder()
                .fullName("Test")
                .studentId(studentID)
                .dateOfBirth(Instant.now())
                .build();
        courseService.updateStudentsInCourse(List.of(updateStudent), 1, "tanhank2k");
        foundStudent = studentRepository.findByStudentId(studentID);
        assertThat(foundStudent).isNotNull();
        assertThat(courseStudentRepository.findByStudentCode(studentID)).isNotNull();
    }

    @Test
    void updateGradeNormal() {
        var grades = gradeRepository.findAll();
        grades.forEach(g -> {
            g.setIsFinalized(false);
            gradeRepository.save(g);
        });
        var scores = List.of(
                UpdateGradeSpecificRequestBase.builder()
                        .grade(10)
                        .mssv("1")
                        .build(),
                UpdateGradeSpecificRequestBase.builder()
                        .grade(50)
                        .mssv("2")
                        .build()
        );

        courseService.updateGradeNormal(UpdateGradeNormalRequest.builder()
                .isFinalized(true)
                .scores(scores)
                .currentUser("tanhank2k")
                .build(), 1, 1);
        var grade1 = gradeRepository.findGradeByStudentId(scores.get(0).getMssv(), 1, 1);
        var grade2 = gradeRepository.findGradeByStudentId(scores.get(1).getMssv(), 1, 1);
        assertThat(grade1.getIsFinalized()).isEqualTo((byte) 1);
        assertThat(grade2.getIsFinalized()).isEqualTo((byte) 1);
        assertThat(grade1.getGradeAssignment()).isEqualTo(scores.get(0).getGrade());
        assertThat(grade2.getGradeAssignment()).isEqualTo(scores.get(1).getGrade());
    }

    @Test
    void findGradeByCourseIdAndAssignmentId() {
        Integer gradeId = courseService.findGradeByCourseIdAndAssignmentId("1", 1, 1);
        assertThat(gradeId).isNotNull();
        assertThat(gradeId).isEqualTo(1);
    }

    @Test
    void updateGradeSpecific() {
        var args = UpdateGradeSpecificArgs.builder()
                .courseId(1)
                .currentUser("tanhank2k")
                .assignmentId(1)
                .isFinalized(true)
                .gradeAssignment(100f)
                .mssv("1")
                .build();
        var success = courseService.updateGradeSpecific(args);

        assertThat(success).isTrue();
        Integer id = courseService.findGradeByCourseIdAndAssignmentId("1", 1, 1);
        var grade = gradeRepository.findById(id).orElse(null);
        assertThat(grade).isNotNull();
        assertThat(grade.getIsFinalized()).isEqualTo((byte) (args.getIsFinalized() ? 1 : 0));
        assertThat(grade.getGradeAssignment()).isEqualTo(args.getGradeAssignment());
    }

    @Test
    void getTeachers() {
        var teachers = courseService.getTeachers(1);
        assertThat(teachers).isNotEmpty();
        var first = teachers.get(0);
        assertThat(first.getId()).isEqualTo(15);
        assertThat(first.getUsername()).isEqualTo("tanhank2k");
        assertThat(first.getFirstName()).isEqualTo("Bùi");
        assertThat(first.getGender()).isEqualTo(EGender.None.getValue());
        assertThat(first.getEmail()).isEqualTo("tanhanh2kocean@gmail.com");
        assertThat(first.getProfileImageUrl()).isNull();
        assertThat(first.getPersonalEmail()).isEqualTo("string");
        assertThat(first.getStudentID()).isEqualTo("1");
        assertThat(first.getUserStatus()).isEqualTo(EUserStatus.Active);
        assertThat(first.getFullname()).isEqualTo("BÙI TẤN HẠNH");

    }

    @Test
    void getTeachers_NotFound() {
        var teachers = courseService.getTeachers(9999);
        assertThat(teachers).isEmpty();
    }

    @Test
    @SneakyThrows
    void getStudents() {
        var testData = " [" + "      {" + "        \"id\": 4," + "        \"username\": \"test\"," + "        \"firstName\": \"Dung\"," + "        \"middleName\": null," + "        \"lastName\": \"Le\"," + "        \"role\": 0," + "        \"gender\": 0," + "        \"email\": \"truongsalacuavietnamnhe@gmail.com\"," + "        \"profileImageUrl\": null," + "        \"personalEmail\": null," + "        \"studentID\": \"2\"," + "        \"phoneNumber\": \"090999909\"," + "        \"personalPhoneNumber\": null," + "        \"userStatus\": 1," + "        \"createOn\": null," + "        \"fullname\": \"DUNG LE\"" + "      }," + "      {" + "        \"id\": 7," + "        \"username\": \"677SG\"," + "        \"firstName\": \"Hạ\"," + "        \"middleName\": null," + "        \"lastName\": \"Bùi\"," + "        \"role\": 0," + "        \"gender\": 0," + "        \"email\": \"tanhank2k@gmail.com\"," + "        \"profileImageUrl\": null," + "        \"personalEmail\": null," + "        \"studentID\": \"1\"," + "        \"phoneNumber\": null," + "        \"personalPhoneNumber\": null," + "        \"userStatus\": 1," + "        \"createOn\": null," + "        \"fullname\": \"HẠ BÙI\"" + "      }," + "      {" + "        \"id\": 0," + "        \"username\": \"Phạm Minh Anh Hữu\"," + "        \"firstName\": null," + "        \"middleName\": null," + "        \"lastName\": null," + "        \"role\": 0," + "        \"gender\": 0," + "        \"email\": null," + "        \"profileImageUrl\": null," + "        \"personalEmail\": null," + "        \"studentID\": \"3\"," + "        \"phoneNumber\": null," + "        \"personalPhoneNumber\": null," + "        \"userStatus\": 0," + "        \"createOn\": \"0001-01-01T00:00:00\"," + "        \"fullname\": null" + "      }," + "      {" + "        \"id\": 0," + "        \"username\": \"Thằng nào đó\"," + "        \"firstName\": null," + "        \"middleName\": null," + "        \"lastName\": null," + "        \"role\": 0," + "        \"gender\": 0," + "        \"email\": null," + "        \"profileImageUrl\": null," + "        \"personalEmail\": null," + "        \"studentID\": \"4\"," + "        \"phoneNumber\": null," + "        \"personalPhoneNumber\": null," + "        \"userStatus\": 0," + "        \"createOn\": null," + "        \"fullname\": null" + "      }," + "      {" + "        \"id\": 0," + "        \"username\": \"Ahihi\"," + "        \"firstName\": null," + "        \"middleName\": null," + "        \"lastName\": null," + "        \"role\": 0," + "        \"gender\": 0," + "        \"email\": null," + "        \"profileImageUrl\": null," + "        \"personalEmail\": null," + "        \"studentID\": \"5\"," + "        \"phoneNumber\": null," + "        \"personalPhoneNumber\": null," + "        \"userStatus\": 0," + "        \"createOn\": null," + "        \"fullname\": null" + "      }" + "    ]";
        var studentsTest = objectMapper.readValue(testData, new TypeReference<List<UserResponse>>() {
        });
        var students = courseService.getStudents(1);
        assertThat(students).isNotEmpty();
        assertThat(students.size()).isEqualTo(studentsTest.size());
    }


    @Test
    @SneakyThrows
    void getStudents_NotFound() {
        var students = courseService.getStudents(999);
        assertThat(students).isEmpty();
    }

    @Test
    void createAssignment() {
        var args = CreateNewAssignmentArgs.builder()
                .courseId(1)
                .maxGrade(10)
                .currentUser("tanhank2k")
                .name("Test")
                .description("Hello world")
                .build();
        var createdAssignment = courseService.createAssignment(args);
        assertThat(createdAssignment).isNotNull();
        assertThat(createdAssignment.getCourse().getId()).isEqualTo(args.getCourseId());
        assertThat(createdAssignment.getDescription()).isEqualTo(args.getDescription());
        assertThat(createdAssignment.getMaxGrade()).isEqualTo(args.getMaxGrade());
    }
}