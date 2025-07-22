package com.claude.bakeoff.repository

import com.claude.bakeoff.model.User
import com.mongodb.client.{MongoClient, MongoCollection}
import com.mongodb.client.model.Filters
import org.bson.Document
import zio.*

import scala.jdk.CollectionConverters.*

trait UserRepository:
  def findAll(): Task[List[User]]
  def findByDepartment(department: String): Task[List[User]]
  def findByEmail(email: String): Task[Option[User]]
  def count(): Task[Long]

class UserRepositoryImpl(mongoClient: MongoClient) extends UserRepository:
  
  private val collection: MongoCollection[Document] = 
    mongoClient.getDatabase("bakeoff").getCollection("users")
  
  override def findAll(): Task[List[User]] = ZIO.attempt {
    collection.find().asScala.map(documentToUser).toList
  }
  
  override def findByDepartment(department: String): Task[List[User]] = ZIO.attempt {
    collection.find(Filters.eq("department", department))
      .asScala.map(documentToUser).toList
  }
  
  override def findByEmail(email: String): Task[Option[User]] = ZIO.attempt {
    Option(collection.find(Filters.eq("email", email)).first()).map(documentToUser)
  }
  
  override def count(): Task[Long] = ZIO.attempt {
    collection.countDocuments()
  }
  
  private def documentToUser(doc: Document): User = {
    val salary: Double = doc.get("salary") match {
      case i: Integer => i.toDouble
      case d: java.lang.Double => d.doubleValue()
      case _ => 0.0
    }
    
    User(
      id = doc.getObjectId("_id").toString,
      name = doc.getString("name"),
      email = doc.getString("email"),
      age = doc.getInteger("age", 0),
      department = doc.getString("department"),
      salary = salary,
      skills = Option(doc.getList("skills", classOf[String])).map(_.asScala.toList).getOrElse(List.empty),
      createdAt = doc.getDate("createdAt").toInstant.toString
    )
  }

object UserRepositoryImpl:
  val layer: ZLayer[MongoClient, Nothing, UserRepository] =
    ZLayer.fromFunction(UserRepositoryImpl.apply)