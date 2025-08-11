package org.example


import java.time.LocalDate
import java.time.LocalDateTime

class AttendanceList : ArrayList<Attendance>() {

    companion object {
        val instance = AttendanceList()
    }

    fun hasAlreadyCheckedIn(employeeId: String, date: LocalDate): Boolean {
        return this.any {
            it.employeeId == employeeId &&
                    it.dateTimeOfCheckIn.toLocalDate() == date
        }
    }

    fun validateCheckedOut(employeeId: String, date: LocalDateTime): Attendance? {
        return this.find {
            it.employeeId == employeeId &&
                    it.dateTimeOfCheckIn.toLocalDate() == date.toLocalDate() &&
                    it.dateTimeOfCheckOut == null &&
                    date.isAfter(it.dateTimeOfCheckIn)
        }
    }
}
