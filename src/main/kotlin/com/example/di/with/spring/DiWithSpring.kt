package com.example.di.with.spring

import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.ApplicationContext
import org.springframework.stereotype.Repository
import org.springframework.stereotype.Service

@SpringBootApplication
class DiWithSpring(private val ctx: ApplicationContext) : CommandLineRunner {
    override fun run(vararg args: String?) {
        val userService = ctx.getBean(UserService::class.java)
        val user = userService.createUser("John", "Doe")

        val foundUser = userService.getUserById(user.id)
        println(foundUser)
    }
}

fun main(args: Array<String>) {
    runApplication<DiWithSpring>(*args)
}

data class User(val id: Long, val firstName: String, val lastName: String)

@Service
class UserService(val userRepository: UserRepository) {
    fun getUserById(id: Long): User {
        return userRepository.findById(id)
    }

    fun createUser(firstName: String, lastName: String): User {
        val user = User(System.currentTimeMillis(), firstName, lastName)
        userRepository.save(user)
        return user
    }
}

interface UserRepository {
    fun findById(id: Long): User
    fun save(user: User)
}

@Repository
class UserRepositoryImpl : UserRepository {
    private val users = mutableListOf<User>()

    override fun findById(id: Long): User =
        users.find { it.id == id }
            ?: throw IllegalArgumentException("User with ID $id not found")

    override fun save(user: User) {
        users.add(user)
    }
}
