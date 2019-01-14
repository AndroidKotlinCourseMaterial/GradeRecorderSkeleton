package edu.rosehulman.graderecorder

import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v7.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private val doInitialSetup = true

    private val baseRef = FirebaseFirestore.getInstance()
    private val courseRef = baseRef.collection("courses")
    private val assignmentRef = baseRef.collection("assignments")
    private val studentRef = baseRef.collection("students")
    private val ownerRef = baseRef.collection("owners")
    private val gradeEntryRef = baseRef.collection("gradeentries")

    private val courses = ArrayList<Course>()


    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_home -> {
                message.setText(R.string.title_home)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_dashboard -> {
                message.setText(R.string.title_dashboard)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_notifications -> {
                message.setText(R.string.title_notifications)
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        pushInitialCourses()
        getCourses()

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
    }

    private fun pushInitialCourses() {
        if (doInitialSetup) {
            courseRef.add(Course("CSSE483", "boutell"))
            courseRef.add(Course("CSSE479", "chenette"))
            courseRef.add(Course("CSSE374", "hays"))
        }


    }

    private fun getCourses() {
        courseRef.get().addOnSuccessListener { snapshot ->
            courses.clear()
            for (document in snapshot) {
                courses.add(Course.fromSnapshot(document))
            }
            addAssignmentsForCourses()
        }
    }

    private fun addAssignmentsForCourses() {
        if (doInitialSetup) {
            val courseIdForCSSE483 = courses.find { it.name == "CSSE483" }!!.id
            assignmentRef.add(Assignment(courseIdForCSSE483, "Lab1", 10.0))
            assignmentRef.add(Assignment(courseIdForCSSE483, "Exam1", 45.0))
            assignmentRef.add(Assignment(courseIdForCSSE483, "Layouts", 5.0))

            studentRef.add(Student(courseIdForCSSE483, "Eugene", "Kim", "kime2"))

            val courseIdForCSSE374 = courses.find { it.name == "CSSE374" }!!.id
            assignmentRef.add(Assignment(courseIdForCSSE374, "ReadingQuiz1", 10.0))
            assignmentRef.add(Assignment(courseIdForCSSE374, "Project M1 Design", 20.0))

            val courseIdForCSSE479 = courses.find { it.name == "CSSE479" }!!.id
            assignmentRef.add(Assignment(courseIdForCSSE479, "Vigenere", 100.0))
            assignmentRef.add(Assignment(courseIdForCSSE479, "AES", 80.0))
        }
    }
}
