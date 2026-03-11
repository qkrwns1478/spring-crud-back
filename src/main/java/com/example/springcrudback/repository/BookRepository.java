package com.example.springcrudback.repository;

import com.example.springcrudback.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Long> {
}