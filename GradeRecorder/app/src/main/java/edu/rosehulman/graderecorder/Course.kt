package edu.rosehulman.graderecorder

import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Exclude

class Course(var name: String="", uid: String="") {
    var owners = HashMap<String, Boolean>()
    @get:Exclude var id: String = ""

    init {
        owners[uid] = true
    }

    companion object {
        fun fromSnapshot(document: DocumentSnapshot): Course {
            val course = document.toObject(Course::class.java)!!
            course.id = document.id
            return course
        }
    }
}