// Initialize the bakeoff database with sample data
db = db.getSiblingDB('bakeoff');

// Create a users collection with sample data
db.users.insertMany([
  {
    _id: ObjectId(),
    name: "Alice Johnson",
    email: "alice@example.com",
    age: 28,
    department: "Engineering",
    salary: 75000,
    skills: ["Java", "Python", "Docker"],
    createdAt: new Date()
  },
  {
    _id: ObjectId(),
    name: "Bob Smith",
    email: "bob@example.com", 
    age: 32,
    department: "Engineering",
    salary: 85000,
    skills: ["Kotlin", "Go", "Kubernetes"],
    createdAt: new Date()
  },
  {
    _id: ObjectId(),
    name: "Carol Williams",
    email: "carol@example.com",
    age: 26,
    department: "Data Science",
    salary: 80000,
    skills: ["Scala", "Python", "ML"],
    createdAt: new Date()
  },
  {
    _id: ObjectId(),
    name: "David Brown",
    email: "david@example.com",
    age: 30,
    department: "DevOps",
    salary: 90000,
    skills: ["Go", "Docker", "AWS"],
    createdAt: new Date()
  },
  {
    _id: ObjectId(),
    name: "Eva Davis",
    email: "eva@example.com",
    age: 29,
    department: "Engineering",
    salary: 78000,
    skills: ["Java", "Micronaut", "MongoDB"],
    createdAt: new Date()
  }
]);

// Create additional sample users for performance testing
for (let i = 6; i <= 100; i++) {
  db.users.insertOne({
    _id: ObjectId(),
    name: `User ${i}`,
    email: `user${i}@example.com`,
    age: 20 + (i % 50),
    department: ["Engineering", "Data Science", "DevOps", "Marketing", "Sales"][i % 5],
    salary: 50000 + (i * 1000),
    skills: [
      ["Java", "Spring", "MySQL"],
      ["Python", "Django", "PostgreSQL"], 
      ["JavaScript", "React", "Node.js"],
      ["Go", "Gin", "Redis"],
      ["Scala", "ZIO", "Kafka"]
    ][i % 5],
    createdAt: new Date()
  });
}

// Create an index on email for faster queries
db.users.createIndex({ email: 1 });

// Create an index on department for filtering
db.users.createIndex({ department: 1 });

print("Database initialized with sample users and indexes");