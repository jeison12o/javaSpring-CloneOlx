package com.example.main.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.example.main.domain.Authority;


@Repository
public interface AuthorityRepository extends CrudRepository<Authority, Long>  {
    Authority findByAuthority(String authority);
}