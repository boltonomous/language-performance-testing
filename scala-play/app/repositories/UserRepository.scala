package repositories

import models.User

import javax.inject.{Inject, Singleton}
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class UserRepository @Inject()(implicit executionContext: ExecutionContext) {

  // Mock data matching the MongoDB test data for consistency
  private val allUsers: List[User] = List(
    User(Some("687fd2b1ce6f80447574e39a"), "Alice Johnson", "alice@example.com", 28, "Engineering", 75000.0, List("Java", "Python", "Docker"), Some("Tue Jul 22 18:04:33 UTC 2025")),
    User(Some("687fd2b1ce6f80447574e39b"), "Bob Smith", "bob@example.com", 32, "Engineering", 85000.0, List("Kotlin", "Go", "Kubernetes"), Some("Tue Jul 22 18:04:33 UTC 2025")),
    User(Some("687fd2b1ce6f80447574e39c"), "Carol Williams", "carol@example.com", 26, "Data Science", 80000.0, List("Scala", "Python", "ML"), Some("Tue Jul 22 18:04:33 UTC 2025")),
    User(Some("687fd2b1ce6f80447574e39d"), "David Brown", "david@example.com", 30, "DevOps", 90000.0, List("Go", "Docker", "AWS"), Some("Tue Jul 22 18:04:33 UTC 2025")),
    User(Some("687fd2b1ce6f80447574e39e"), "Eva Davis", "eva@example.com", 29, "Engineering", 78000.0, List("Java", "Micronaut", "MongoDB"), Some("Tue Jul 22 18:04:33 UTC 2025"))
  ) ++ (6 to 100).map { i =>
    val departments = List("Engineering", "Data Science", "DevOps", "Marketing", "Sales")
    val skillSets = List(
      List("Java", "Spring", "MySQL"),
      List("Python", "Django", "PostgreSQL"),
      List("JavaScript", "React", "Node.js"),
      List("Go", "Gin", "Redis"),
      List("Scala", "ZIO", "Kafka")
    )
    User(
      Some(s"687fd2b1ce6f80447574e${39 + i}"),
      s"User $i",
      s"user$i@example.com",
      20 + (i % 50),
      departments(i % 5),
      50000.0 + (i * 1000),
      skillSets(i % 5),
      Some("Tue Jul 22 18:04:33 UTC 2025")
    )
  }.toList

  def findAll(): Future[List[User]] = {
    Future.successful(allUsers)
  }

  def findByDepartment(department: String): Future[List[User]] = {
    Future.successful(allUsers.filter(_.department == department))
  }

  def count(): Future[Long] = {
    Future.successful(allUsers.length.toLong)
  }
}