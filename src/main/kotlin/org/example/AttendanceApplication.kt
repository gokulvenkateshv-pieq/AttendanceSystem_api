package org.example

import io.dropwizard.core.Application
import io.dropwizard.core.setup.Environment
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.databind.SerializationFeature


class AttendanceApplication : Application<AttendanceConfiguration>() {
    override fun run(configuration: AttendanceConfiguration, environment: Environment) {
        environment.jersey().register(AttendanceResource())
        //for local date time
        environment.objectMapper.registerModule(KotlinModule.Builder().build())
        environment.objectMapper.registerModule(JavaTimeModule())

        environment.objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)

    }
}

fun main(args: Array<String>) {
    AttendanceApplication().run(*args)
}
