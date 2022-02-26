package com.ledungcobra.course.service;

import com.ledungcobra.common.AuditUtils;
import com.ledungcobra.common.ERole;
import com.ledungcobra.course.entity.*;
import com.ledungcobra.course.repository.*;
import com.ledungcobra.dto.common.UpdateGradeSpecificArgs;
import com.ledungcobra.dto.course.createNewAssignment.CreateNewAssignmentArgs;
import com.ledungcobra.dto.course.getAllGrades.GradeOfCourseResponse;
import com.ledungcobra.dto.course.getAllGrades.GradeSimpleResponse;
import com.ledungcobra.dto.course.getAllGradesV2.GradeOfAssignmentResponse;
import com.ledungcobra.dto.course.postCreateCourse.CreateCourseRequest;
import com.ledungcobra.dto.course.postUpdateAssignmentNormal.UpdateGradeNormalRequest;
import com.ledungcobra.dto.course.postUpdateAssignmentNormal.UpdateGradeSpecificRequestBase;
import com.ledungcobra.dto.course.updateAssigment.UpdateAssignmentsRequest;
import com.ledungcobra.dto.user.register.UserResponse;
import com.ledungcobra.exception.NotFoundException;
import com.ledungcobra.user.entity.ClassRole;
import com.ledungcobra.user.entity.User;
import com.ledungcobra.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class CourseServiceImpl implements CourseService {

    private final CourseRepository courseRepository;
    private final AssignmentRepository assignmentRepository;
    private final GradeRepository gradeRepository;
    private final CourseStudentRepository courseStudentRepository;
    private final StudentRepository studentRepository;
    private final UserRepository userRepository;
    private final EntityManager entityManager;
    private final CourseUserRepository courseUserRepository;

    @Override
    public List<Course> getCoursesByTitleLike(String title, Pageable pageable, String username) {
        return courseRepository.getCoursesByTitleLike(title, username, pageable);
    }

    @Override
    public Course createCourse(CreateCourseRequest request) {
        var course = Course.builder().schedule(request.getSchedule()).description(request.getDescription()).gradeId(request.getGradeId()).courseCode(UUID.randomUUID().toString()).title(request.getTitle()).credits(request.getCredits())

                .build();
        return courseRepository.save(AuditUtils.createAudit(course, request.getCurrentUser()));
    }

    @Override
    public long countByOwner(String owner) {
        return courseRepository.countByOwner(owner);
    }

    @Override
    public Course findCourseById(String currentUser, Integer id) {
        return courseRepository.findCourseByIdAndUserName(currentUser, id);
    }

    @Override
    public List<Assignment> findAssignmentOfsCourseLikeName(String currentUser, Integer courseId, String name, Pageable pageable) {
        return courseRepository.findAssignmentsOfCourseLikeName(currentUser, courseId, name, pageable);
    }

    @Override
    public Assignment findAssigmentById(Integer courseId, Integer assignmentId) {
        return courseRepository.findAssignmentById(courseId, assignmentId);
    }

    @Override
    public Assignment updateAssignment(UpdateAssignmentsRequest request, String currentUser, Integer assignmentId) throws NotFoundException {
        Assignment assignment = assignmentRepository.findById(assignmentId).orElseThrow(() -> new NotFoundException(String.format("Not found assignment by id %d", assignmentId)));
        assignment.setName(request.getName());
        assignment.setDescription(request.getDescription());
        assignment.setMaxGrade(request.getMaxGrade());
        return assignmentRepository.save(AuditUtils.updateAudit(assignment, currentUser));
    }

    @Override
    public void deleteAssignment(Integer assignmentId) throws NotFoundException {
        assignmentRepository.delete(assignmentRepository.findById(assignmentId).orElseThrow(() -> new NotFoundException(String.format("Not found assignment by id %d", assignmentId))));
    }

    @Override
    public List<GradeOfCourseResponse> getAllGrades(Integer courseId) {
        var grades = gradeRepository.findByCourseId(courseId);
        var studentsInCourse = courseStudentRepository.findAllStudentInCourse(courseId);
        return studentsInCourse.stream().map(s -> {
            var gradesSimples = grades.stream().filter(g -> g.getMssv() != null && g.getMssv().equals(s.getStudentId())).map(GradeSimpleResponse::new).toList();
            return GradeOfCourseResponse.builder().name(s.getFullName()).id(s.getId()).mssv(s.getStudentId()).grades(gradesSimples).build();
        }).toList();
    }

    @Override
    public GradeOfCourseResponse getGradeOfCourseByStudentTableId(Integer courseId, Integer studentTableId) throws NotFoundException {
        var grades = gradeRepository.getGradeOfCourseForStudentFinalized(courseId, studentTableId);
        var student = studentRepository.findById(studentTableId).orElseThrow(() -> new NotFoundException("Not found Student by id: " + studentTableId));
        return new GradeOfCourseResponse(student, grades);
    }

    @Override
    public List<GradeOfAssignmentResponse> getAllGradeAssignment(Integer assignmentId) {
        var grades = gradeRepository.findAllGradeByAssignmentId(assignmentId);
        return grades.stream().map(g -> new GradeOfAssignmentResponse(g, userRepository.findByStudentID(g.getMssv()))).toList();
    }

    public Student createNewStudent(String mssv, String auditor) {
        return studentRepository.save(AuditUtils.createAudit(Student.builder().studentId(mssv).dateOfBirth(Instant.now()).build(), auditor));
    }

    @Override
    public void updateStudentsInCourse(List<Student> studentList, Integer courseId, String auditor) {
        for (Student student : studentList) {
            var studentInserted = insertIfNotExist(student.getStudentId(), courseId, auditor);
            if (!Objects.equals(studentInserted.getFullName(), student.getFullName())) {
                studentInserted.setFullName(student.getFullName());
                studentRepository.save(AuditUtils.updateAudit(studentInserted, auditor));
            }
        }
    }

    @Override
    public boolean updateGradeNormal(UpdateGradeNormalRequest request, Integer assignmentsId, Integer courseId) {
        var assignment = assignmentRepository.findById(assignmentsId).orElse(null);
        if (assignment == null || !Objects.equals(assignment.getCourse().getId(), courseId)) {
            return false;
        }

        var studentIds = request.getScores().stream().map(UpdateGradeSpecificRequestBase::getMssv).toList();
        List<Grade> grades = studentIds.stream().map(studentId -> gradeRepository.findGradeByStudentId(studentId, assignmentsId, courseId)).toList();
        for (UpdateGradeSpecificRequestBase score : request.getScores()) {
            var gradeExist = grades.stream().filter(g -> Objects.equals(g.getMssv(), score.getMssv())).findFirst().orElse(null);
            if (gradeExist != null) {
                gradeExist.setGradeAssignment(score.getGrade());
                gradeExist.setIsFinalized(true);
                gradeRepository.save(AuditUtils.updateAudit(gradeExist, request.getCurrentUser()));
            } else {
                var student = studentRepository.findByStudentId(score.getMssv());
                if (student == null) {
                    student = createNewStudent(score.getMssv(), request.getCurrentUser());
                }
                var grade = Grade.builder().assigment(assignment).description("").gradeAssignment(score.getGrade()).mssv(score.getMssv()).isFinalized((byte) (request.getIsFinalized() ? 1 : 0)).student(student).build();
                gradeRepository.save(AuditUtils.createAudit(grade, request.getCurrentUser()));
            }
        }
        return true;
    }


    @Override
    public boolean updateGradeSpecific(UpdateGradeSpecificArgs args) {
        var assignment = assignmentRepository.findById(args.getAssignmentId()).orElse(null);
        if (assignment == null || (assignment.getCourse() != null && !Objects.equals(assignment.getCourse().getId(), args.getCourseId()))) {
            return false;
        }
        var student = studentRepository.findByStudentId(args.getMssv());
        if (student == null) return false;
        var grade = gradeRepository.findGradeByStudentId(args.getMssv(), assignment.getId(), args.getCourseId());
        if (grade == null) {
            grade = Grade.builder().assigment(assignment).description("").gradeAssignment(args.getGradeAssignment()).mssv(args.getMssv()).isFinalized((byte) (args.getIsFinalized() ? 1 : 0)).student(student).build();
            gradeRepository.save(AuditUtils.createAudit(grade, args.getCurrentUser()));
        } else {
            grade.setGradeAssignment(args.getGradeAssignment());
            grade.setIsFinalized(args.getIsFinalized());
            gradeRepository.save(AuditUtils.updateAudit(grade, args.getCurrentUser()));
        }

        return true;
    }

    @Override
    public Integer findGradeByCourseIdAndAssignmentId(String mssv, Integer courseId, Integer assignmentsId) {
        var grade = gradeRepository.findGradeByStudentId(mssv, assignmentsId, courseId);
        return grade == null ? 0 : grade.getId();
    }


    private Student insertIfNotExist(String mssv, Integer courseId, String auditor) {
        Student student = studentRepository.findByStudentId(mssv);
        if (student == null) {
            student = createNewStudent(mssv, auditor);
            var cs = courseStudentRepository.findAllByCourseId(courseId);
            if (cs.stream().noneMatch(c -> c.getStudentCode() != null && c.getStudentCode().equals(mssv))) {
                var courseStudent = CourseStudent.builder().studentCode(mssv).course(entityManager.getReference(Course.class, courseId)).student(student).build();
                courseStudentRepository.save(AuditUtils.createAudit(courseStudent, auditor));
            }
        }
        return student;
    }

    @Override
    public Grade saveGrade(Grade grade, String auditor, Integer assignmentsId, Integer courseId) {
        var dbGrade = gradeRepository.findGradeByStudentId(grade.getMssv(), assignmentsId, courseId);
        if (dbGrade == null) {
            var student = insertIfNotExist(grade.getMssv(), courseId, auditor);
            grade.setStudent(student);
            return gradeRepository.save(AuditUtils.createAudit(grade, auditor));
        } else {
            dbGrade.setMssv(grade.getMssv());
            dbGrade.setDescription(grade.getDescription());
            dbGrade.setIsFinalized(grade.getIsFinalized() == 1);
            dbGrade.setGradeAssignment(grade.getGradeAssignment());
            return gradeRepository.save(AuditUtils.updateAudit(dbGrade, auditor));
        }

    }

    @Override
    public List<UserResponse> getTeachers(Integer courseId) {
        return courseUserRepository.findUserByCourseIdAndRole(courseId, ERole.Teacher.getValue()).stream().map(UserResponse::new).toList();
    }

    @Override
    public List<UserResponse> getStudents(Integer courseId) {
        var listStudent1 = courseUserRepository.findUserByCourseIdAndRole(courseId, ERole.Student.getValue()).stream().map(UserResponse::new).toList();
        var listStudent2 = courseStudentRepository.findAllByCourseId(courseId).stream().filter(cs -> listStudent1.stream().noneMatch(s -> Objects.equals(s.getStudentID(), cs.getStudentCode()))).map(cs -> new UserResponse(cs.getStudent())).toList();

        return Stream.concat(listStudent1.stream(), listStudent2.stream()).toList();
    }

    @Override
    public List<GradeOfCourseResponse> findAllGradeOfCourse(Integer courseId) {
        var studentInCourse = courseStudentRepository.findAllStudentInCourse(courseId);
        return studentInCourse.stream().map(s -> new GradeOfCourseResponse(s, gradeRepository.getGradeOfCourseForStudentNotFinalized(courseId, s.getId()))).toList();
    }

    @Override
    public List<Course> findAllCourses() {
        return courseRepository.findAll();
    }

    @Override
    public boolean addMemberIntoCourse(User user, Integer role, Integer courseId) {
        var userExist = courseUserRepository.findCourseUserByRoleAndUserIdAndCourseId(role, user.getId(), courseId);
        if (userExist != null) return false;
        var courseUser = CourseUser.builder().course(entityManager.getReference(Course.class, courseId)).user(user).role(entityManager.getReference(ClassRole.class, role)).build();
        courseUserRepository.save(AuditUtils.createAudit(courseUser, user.getUserName()));
        return true;
    }

    @Override
    public boolean addMemberIntoCourse(Integer inviteeId, String currentUser, Integer role, Integer courseId) {
        var userExist = courseUserRepository.findCourseUserByUserIdAndCourseId(inviteeId, courseId);
        if (userExist != null) {
            return false;
        }
        var courseUser = CourseUser.builder().user(entityManager.getReference(User.class, inviteeId)).course(entityManager.getReference(Course.class, courseId)).role(entityManager.getReference(ClassRole.class, role)).build();
        courseUserRepository.save(AuditUtils.createAudit(courseUser, currentUser));
        return true;
    }

    @Override
    public void updateRole(CourseUser courseUser, Integer role, String currentUser) {
        courseUser.setRole(entityManager.getReference(ClassRole.class, role));
        courseUserRepository.save(AuditUtils.updateAudit(courseUser, currentUser));
    }

    @Override
    public Assignment createAssignment(CreateNewAssignmentArgs args) {
        var numberOfAssignments = assignmentRepository.countAssignmentByCourseId(args.getCourseId());
        return assignmentRepository.save(AuditUtils.createAudit(Assignment.builder()
                .course(entityManager.getReference(Course.class, args.getCourseId()))
                .name(args.getName())
                .maxGrade(args.getMaxGrade())
                .description(args.getDescription())
                .order(numberOfAssignments + 1)
                .build(), args.getCurrentUser()));
    }

    @Override
    public List<Integer> getTeacherIds(Integer courseId) {
        return courseUserRepository.findUserIdsByCourseIdAndRole(courseId,ERole.Teacher.getValue());
    }
}
