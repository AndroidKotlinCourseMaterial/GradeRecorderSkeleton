package edu.rosehulman.graderecorder

import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v7.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore
import edu.rosehulman.graderecorder.models.Assignment
import edu.rosehulman.graderecorder.models.Course
import edu.rosehulman.graderecorder.models.Owner
import edu.rosehulman.graderecorder.models.Student
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private val doInitialSetup = false

    private val baseRef = FirebaseFirestore.getInstance()
    private val coursesRef = baseRef.collection("courses")
    private val assignmentsRef = baseRef.collection("assignments")
    private val studentsRef = baseRef.collection("students")
    private val ownersRef = baseRef.collection("owners")
    private val gradeEntriesRef = baseRef.collection("gradeentries")

    private val courses = ArrayList<Course>()

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_home -> {
                getAssignmentsInCourse(idFromName("CSSE483"))
                // getStudentsInCourse(idFromName("CSSE374"))
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_dashboard -> {
                // I know this is kind of dumb since I know the name. But
                // this method was called from a place where I did just have a
                // course id.
                getCourseNameForCourseId(idFromName("CSSE479"))
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_notifications -> {
                getCoursesForOwner("boutell")
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        pushInitialCourses()

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
    }

    private fun getAssignmentsInCourse(courseId: String) {
        // TODO: set message.text with the appropriate String.
    }

    private fun getStudentsInCourse(courseId: String) {
        // TODO: set message.text with the appropriate String.
    }

    private fun getCourseNameForCourseId(courseId: String) {
        // TODO: set message.text with the appropriate String.
    }

    fun getCoursesForOwner(ownerName: String) {
        // TODO: set message.text with the appropriate String.
        // Hint: Use FieldPath in query.

    }

    fun getCoursesForOwner2(ownerName: String) {
        // Don't use this method. Instead, write the method above using the course
        // path.
//        // Need to get owner.id for name. Sometimes this is done already.
        // If we need to get it, we can run into trouble. See definition of this
        // helper function below.
//        var ownerId = getOwnerIdForName(ownerName)
//        ownersRef.document(ownerName).get()
//            .addOnSuccessListener { snapshot: DocumentSnapshot ->
//                val courseIds = snapshot["courses"] as Map<String, Boolean>
//                val courses = ArrayList<Course>()
//                for (courseId in courseIds.keys) {
//                    coursesRef.document(courseId).get()
//                        .addOnSuccessListener { courseSnapshot: DocumentSnapshot ->
//                            val course = Course.fromSnapshot(courseSnapshot)
//                            courses.add(course)
//                            message.text = courses.toString()
//                        }
//                }
//                // How will we know when all have been loaded?
//            }
    }

    private fun addOwnerForCourse(ownerId: String, courseId: String) {
        // TODO: add given owner to the given course. Remember it's double-linked.
    }

    // Setup
    private fun pushInitialCourses() {
        if (doInitialSetup) {
            coursesRef.add(Course("CSSE483", "boutell"))
            coursesRef.add(Course("CSSE479", "chenette"))
            coursesRef.add(Course("CSSE374", "hays"))
        }
        getCourses()
    }

    private fun getCourses() {
        coursesRef.get().addOnSuccessListener { snapshot ->
            courses.clear()
            for (document in snapshot) {
                courses.add(Course.fromSnapshot(document))
            }
            addAssignmentsForCourses()
            addOwnersForCourses()
        }
    }

    private fun addAssignmentsForCourses() {
        if (doInitialSetup) {
            val courseIdForCSSE483 = idFromName("CSSE483")
            val courseIdForCSSE374 = idFromName("CSSE374")
            val courseIdForCSSE479 = idFromName("CSSE479")

            assignmentsRef.add(Assignment(courseIdForCSSE483, "Lab1", 10.0))
            assignmentsRef.add(Assignment(courseIdForCSSE483, "Exam1", 45.0))
            assignmentsRef.add(Assignment(courseIdForCSSE483, "Layouts", 5.0))

            assignmentsRef.add(Assignment(courseIdForCSSE374, "ReadingQuiz1", 10.0))
            assignmentsRef.add(Assignment(courseIdForCSSE374, "Project M1 Design", 20.0))

            assignmentsRef.add(Assignment(courseIdForCSSE479, "Vigenere", 100.0))
            assignmentsRef.add(Assignment(courseIdForCSSE479, "AES", 80.0))

            studentsRef.add(Student(courseIdForCSSE483, "Jianan", "Pang", "pangj"))
            studentsRef.add(Student(courseIdForCSSE483, "Alex", "Dripchak", "dripchar"))

            studentsRef.add(Student(courseIdForCSSE374, "Eugene", "Kim", "kime2"))
            studentsRef.add(Student(courseIdForCSSE374, "Yang", "Gao", "gaoy2"))
            studentsRef.add(Student(courseIdForCSSE374, "Matthew", "Lyons", "lyonsmj"))

            studentsRef.add(Student(courseIdForCSSE479, "Jacob", "Gathof", "gathofjd"))
        }
    }

    private fun addOwnersForCourses() {
        if (doInitialSetup) {
            val boutell = Owner("boutell")
            boutell.addCourse(idFromName("CSSE483"))
            boutell.addCourse(idFromName("CSSE479"))
            ownersRef.add(boutell)

            // Owners and courses are 2-way, so if Boutell is an owner of 479,
            // we must record it in the owner (done above) and in the course:
            val csse479 = courseFromName("CSSE479")
            csse479.addOwner("boutell")
            coursesRef.document(idFromName("CSSE479")).set(csse479)

            // Add other owners
            val chenette = Owner("chenette")
            chenette.addCourse(idFromName("CSSE479"))
            ownersRef.add(chenette)

            val hays = Owner("hays")
            hays.addCourse(idFromName("CSSE374"))
            ownersRef.add(hays)

            val austinin = Owner("austinin")
            austinin.addCourse(idFromName("CSSE483"))
            ownersRef.add(austinin)

            val lewistd = Owner("lewistd")
            lewistd.addCourse(idFromName("CSSE483"))
            ownersRef.add(lewistd)

            val csse483 = courseFromName("CSSE483")
            csse483.addOwner("austinin")
            csse483.addOwner("lewistd")
            coursesRef.document(idFromName("CSSE483")).set(csse483)
        }
    }

    private fun idFromName(courseName: String): String {
        return courses.find { it.name == courseName }!!.id
    }

    private fun courseFromName(courseName: String): Course {
        return courses.find { it.name == courseName }!!
    }
}
