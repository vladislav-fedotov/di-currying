package com.example.di.with.pure

private val users = mutableListOf<User>()

// Define the user data model
data class User(val id: Long, val firstName: String, val lastName: String)

// Define the user repository functions
fun findById(id: Long): User =
    users.find { it.id == id } ?: throw IllegalArgumentException("User with ID $id not found")

fun save(user: User) = users.add(user)

// Define the user service functions
fun getUserById(findById: (Long) -> User): (Long) -> User = { id -> findById(id) }

fun createUser(save: (User) -> Unit): (String, String) -> User = { firstName, lastName ->
    val user = User(System.currentTimeMillis(), firstName, lastName)
    save(user)
    user
}

fun main() {
    // Create a new user
    // val createUserFunc = createUser(::findById)
    val newUser = createUser(::save)("John", "Doe")

    // Get user by ID
    // val getUserByIdFunc = getUserById(::findById)
    val user = getUserById(::findById)(newUser.id)
    println(user)
}
