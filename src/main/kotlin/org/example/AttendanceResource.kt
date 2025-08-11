package org.example

import jakarta.ws.rs.*
import jakarta.ws.rs.core.MediaType
import jakarta.ws.rs.core.Response
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Path("/attendance")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
class AttendanceResource {

    private val attendanceList = AttendanceList.instance

    data class CheckInRequest(val employeeId: String, val checkIn: LocalDateTime)
    data class CheckOutRequest(val employeeId: String, val checkOut: LocalDateTime)

    // ✅ POST: Check-in
    @POST
    @Path("/checkin")
    fun checkIn(request: CheckInRequest): Response {
        val date = request.checkIn.toLocalDate()

        if (attendanceList.hasAlreadyCheckedIn(request.employeeId, date)) {
            return Response.status(Response.Status.CONFLICT)
                .entity("Already checked in today for employee ${request.employeeId}")
                .build()
        }

        val attendance = Attendance(request.employeeId, request.checkIn)
        attendanceList.add(attendance)

        return Response.status(Response.Status.CREATED)
            .entity(attendance)
            .build()
    }

    // ✅ PUT: Check-out
    @PUT
    @Path("/checkout")
    fun checkOut(request: CheckOutRequest): Response {
        val attendance = attendanceList.validateCheckedOut(request.employeeId, request.checkOut)
            ?: return Response.status(Response.Status.NOT_FOUND)
                .entity("No valid check-in record found for employee ${request.employeeId}")
                .build()

        attendance.checkOut(request.checkOut)

        return Response.ok(attendance).build()
    }

    // ✅ GET: View all records or filter by employeeId and/or date
    @GET
    fun getAttendance(
        @QueryParam("employeeId") employeeId: String?,
        @QueryParam("date") dateStr: String?
    ): Response {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val date = dateStr?.let {
            try {
                LocalDate.parse(it, formatter)
            } catch (e: Exception) {
                return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Invalid date format. Use yyyy-MM-dd.")
                    .build()
            }
        }

        val filtered = attendanceList.filter {
            (employeeId == null || it.employeeId == employeeId) &&
                    (date == null || it.dateTimeOfCheckIn.toLocalDate() == date)
        }

        return Response.ok(filtered).build()
    }
}
