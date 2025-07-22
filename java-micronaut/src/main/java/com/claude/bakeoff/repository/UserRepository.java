package com.claude.bakeoff.repository;

import com.claude.bakeoff.model.User;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Filters;
import jakarta.inject.Singleton;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Singleton
public class UserRepository {
    
    private final MongoCollection<Document> collection;
    
    public UserRepository(MongoClient mongoClient) {
        this.collection = mongoClient.getDatabase("bakeoff").getCollection("users");
    }
    
    public List<User> findAll() {
        List<User> users = new ArrayList<>();
        try (MongoCursor<Document> cursor = collection.find().iterator()) {
            while (cursor.hasNext()) {
                users.add(documentToUser(cursor.next()));
            }
        }
        return users;
    }
    
    public List<User> findByDepartment(String department) {
        List<User> users = new ArrayList<>();
        try (MongoCursor<Document> cursor = collection.find(Filters.eq("department", department)).iterator()) {
            while (cursor.hasNext()) {
                users.add(documentToUser(cursor.next()));
            }
        }
        return users;
    }
    
    public Optional<User> findByEmail(String email) {
        Document doc = collection.find(Filters.eq("email", email)).first();
        return doc != null ? Optional.of(documentToUser(doc)) : Optional.empty();
    }
    
    public long count() {
        return collection.countDocuments();
    }
    
    private User documentToUser(Document doc) {
        // Handle salary type conversion (might be Integer or Double)
        Object salaryValue = doc.get("salary");
        double salary = 0.0;
        if (salaryValue instanceof Integer) {
            salary = ((Integer) salaryValue).doubleValue();
        } else if (salaryValue instanceof Double) {
            salary = (Double) salaryValue;
        }
        
        return new User(
            doc.getObjectId("_id").toString(),
            doc.getString("name"),
            doc.getString("email"),
            doc.getInteger("age", 0),
            doc.getString("department"),
            salary,
            doc.getList("skills", String.class),
            doc.getDate("createdAt").toInstant()
        );
    }
}