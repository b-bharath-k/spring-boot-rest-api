package com.bharath.rest.webservices.restfulwebservices.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.bharath.rest.webservices.restfulwebservices.entities.Post;

@Repository
public interface PostRepository extends JpaRepository<Post, Integer>{

}
