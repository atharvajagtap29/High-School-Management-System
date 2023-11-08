package com.App.Controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.App.Model.Classes;
import com.App.Model.Courses;
import com.App.Model.Person;
import com.App.Repository.ClassesRepo;
import com.App.Repository.CoursesRepo;
import com.App.Repository.PersonRepo;

import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("admin")
public class AdminController {

	@Autowired
	ClassesRepo classRepo;

	@Autowired
	PersonRepo personRepo;

	@Autowired
	CoursesRepo coursesRepo;

	@RequestMapping("/displayClasses")
	public ModelAndView displayClasses(Model model) {
		List<Classes> classesList = classRepo.findAll();
		ModelAndView modelandView = new ModelAndView("classes.html");
		modelandView.addObject("classesList", classesList);
		modelandView.addObject("classesObj", new Classes());
		return modelandView;
	}

	@RequestMapping(value = "/addNewClass", method = { RequestMethod.POST })
	public ModelAndView addNewClass(Model model, @ModelAttribute("classesObj") Classes classes) {
		classRepo.save(classes);
		ModelAndView modelandview = new ModelAndView("redirect:/admin/displayClasses");
		return modelandview;
	}

	@RequestMapping("/deleteClass")
	public ModelAndView deleteClass(Model model, @RequestParam int id) {
		// the below code cuts the foreign key link between classes table and person
		// table, so if i delete my class, the person[students] inside it wont also be
		// deleted
		Optional<Classes> classById = classRepo.findById(id);
		for (Person person : classById.get().getPersons()) {
			person.setClasses(null);
			personRepo.save(person);
		}
		classRepo.deleteById(id); // delete from classes where id = ?
		ModelAndView modelAndView = new ModelAndView("redirect:/admin/displayClasses");
		return modelAndView;
	}

	// This method will return the list of students enrolled to a particular class
	@RequestMapping("/displayStudents")
	public ModelAndView displayStudents(Model model, @RequestParam int classId, HttpSession session,
			@RequestParam(value = "error", required = false) String error) {

		String errorMsg = null;
		ModelAndView modelAndView = new ModelAndView("students.html");
		Optional<Classes> TheClass = classRepo.findById(classId);
		modelAndView.addObject("TheClass", TheClass.get());
		modelAndView.addObject("person", new Person());
		if (error != null) {
			errorMsg = "Invalid Email Entered!";
			modelAndView.addObject("errorMessage", errorMsg);
		}
		session.setAttribute("TheClass", TheClass.get());
		return modelAndView;

	}

	// this method is responsible to add a new student to a particular class
	@PostMapping("/addStudent")
	public ModelAndView addStudent(Model model, @ModelAttribute("person") Person person, HttpSession session) {
		ModelAndView modelAndView = new ModelAndView();
		Classes theClass = (Classes) session.getAttribute("TheClass");
		Person personEntity = personRepo.readByEmail(person.getEmail());
		if (personEntity == null || !(personEntity.getPersonId() > 0)) {
			modelAndView
					.setViewName("redirect:/admin/displayStudents?classId=" + theClass.getClassId() + "&error=true");
			return modelAndView;
		}
		personEntity.setClasses(theClass); // setting class details in person entity. i.e child details in Parent entity
		personRepo.save(personEntity); // and saving the person entity as the link in established between class and
										// person
		//theClass.getPersons().add(personEntity);
		classRepo.save(theClass);
		modelAndView.setViewName("redirect:/admin/displayStudents?classId=" + theClass.getClassId());
		return modelAndView;
	}

	// This method will delete the student with the personId we are receiveing from
	// the parameters
	@GetMapping("/deleteStudent")
	public ModelAndView deleteStudent(Model model, @RequestParam int personId, HttpSession session) {
		Classes theClass = (Classes) session.getAttribute("TheClass");
		Optional<Person> person = personRepo.findById(personId);
		person.get().setClasses(null);
		theClass.getPersons().remove(person.get());
		Classes classSaved = classRepo.save(theClass);
		session.setAttribute("theClass", classSaved);
		ModelAndView modelAndView = new ModelAndView(
				"redirect:/admin/displayStudents?classId=" + theClass.getClassId());
		return modelAndView;
	}

	// This method is responsible for displaying a new courses page in admin
	// dashboard whenever use clicks on courses link or button
	@GetMapping("/displayCourses")
	public ModelAndView displayCourses(Model model, Sort sort) {
		//List<Courses> courses = coursesRepo.findByOrderByName(); // select all the courses present in the table and order by ?
		List <Courses> courses = coursesRepo.findAll(sort.by("name").ascending());
		ModelAndView modelAndView = new ModelAndView("courses_secure.html"); // new name as we already have courses.html
		modelAndView.addObject("courses", courses); // sending to UI the list of already registered courses by admin
		modelAndView.addObject("course", new Courses()); // sending UI a new object of courses class to populate and
															// send back in order to save in database
		return modelAndView;
	}

	@RequestMapping(value = "/addNewCourse", method = { RequestMethod.POST })
	public ModelAndView addNewCourse(Model model, @ModelAttribute("course") Courses course) {
		ModelAndView modelAndView = new ModelAndView();
		coursesRepo.save(course);
		modelAndView.setViewName("redirect:/admin/displayCourses");
		return modelAndView;
	}

	@GetMapping("/viewStudents")
	public ModelAndView viewStudents(Model model, @RequestParam int id, HttpSession session,
			@RequestParam(required = false) String error) {
		String errorMessage = null;
		ModelAndView modelAndView = new ModelAndView("course_students.html");
		Optional<Courses> courses = coursesRepo.findById(id);
		modelAndView.addObject("courses", courses.get());
		modelAndView.addObject("person", new Person());
		session.setAttribute("courses", courses.get()); // here we are setting the current focused course like
														// [football]
		if (error != null) {
			errorMessage = "Invalid Email address entered!";
			modelAndView.addObject("errorMessage", errorMessage);
		}
		return modelAndView;
	}

	@PostMapping("/addStudentToCourse")
	public ModelAndView addStudentToCourse(Model model, @ModelAttribute("person") Person person, HttpSession session) {
		ModelAndView modelAndView = new ModelAndView();
		Courses courses = (Courses) session.getAttribute("courses"); // here we are catching the focused course that we
																		// set above. Like football or cricket
		Person personEntity = personRepo.readByEmail(person.getEmail()); // select * from person where email = ?
		if (personEntity == null || !(personEntity.getPersonId() > 0)) {
			modelAndView.setViewName("redirect:/admin/viewStudents?id=" + courses.getCourseId() + "&error=true");
			return modelAndView;
		}
		personEntity.getCourses().add(courses);
		courses.getPersons().add(personEntity); // here we add new students to existing list of students
		personRepo.save(personEntity);
		session.setAttribute("courses", courses);
		modelAndView.setViewName("redirect:/admin/viewStudents?id=" + courses.getCourseId());
		return modelAndView;
	}

	@GetMapping("/deleteStudentFromCourse")
	public ModelAndView deleteStudentFromCourse(Model model, @RequestParam int personId, HttpSession session) {
		Courses course = (Courses) session.getAttribute("courses"); // we got the current focused course here
		Optional<Person> person = personRepo.findById(personId); // fetched the person by person id we got in parameter and
															// querried it in the database and got the person
		person.get().getCourses().remove(course); // got the exact person by get method and of that person we got
													// courses associated to him and deleted the focused course we got
													// above
		course.getPersons().remove(person); // got the target person above in variable 'person' and from the list of
											// persons associated with that course, deleting the target person
		personRepo.save(person.get());
		session.setAttribute("courses", course);
		ModelAndView modelAndView = new ModelAndView("redirect:/admin/viewStudents?id=" + course.getCourseId());
		return modelAndView;
	}
}
