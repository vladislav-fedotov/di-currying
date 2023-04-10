package com.example.di.with.cyrrying

class DiWithCurrying

fun main() {
    val userService = userServiceFactory(userRepository())

    // Create a new user
    val newUser = createUser(userService)("John", "Doe")
    println(newUser)

    // Get user by ID
    val user = getUserById(userService)(newUser.id)
    println(user)
}

data class User(val id: Long, val firstName: String, val lastName: String)

interface UserRepository {
    fun findById(id: Long): User
    fun save(user: User)
}

class UserRepositoryImpl : UserRepository {
    private val users = mutableListOf<User>()

    override fun findById(id: Long): User =
        users.find { it.id == id } ?: throw IllegalArgumentException("User with ID $id not found")

    override fun save(user: User) {
        users.add(user)
    }
}

fun userRepository(): UserRepository = UserRepositoryImpl()

interface UserService {
    fun getUserById(id: Long): User
    fun createUser(firstName: String, lastName: String): User
}

class UserServiceImpl(
    private val findById: (Long) -> User,
    private val save: (User) -> Unit,
) : UserService {
    override fun getUserById(id: Long): User {
        return findById(id)
    }

    override fun createUser(firstName: String, lastName: String): User {
        val user = User(System.currentTimeMillis(), firstName, lastName)
        save(user)
        return user
    }
}

fun userServiceFactory(userRepository: UserRepository): UserService {
    val findById: (Long) -> User = { id -> userRepository.findById(id) }
    val save: (User) -> Unit = { user -> userRepository.save(user) }
    return UserServiceImpl(findById, save)
}

fun getUserById(userService: UserService): (Long) -> User = userService::getUserById

fun createUser(userService: UserService): (String, String) -> User = userService::createUser
