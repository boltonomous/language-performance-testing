package com.claude.bakeoff.model;

import io.micronaut.serde.annotation.Serdeable;
import org.bson.types.ObjectId;
import java.time.Instant;
import java.util.List;

@Serdeable
public record User(
    String id,
    String name,
    String email,
    int age,
    String department,
    double salary,
    List<String> skills,
    Instant createdAt
) {}