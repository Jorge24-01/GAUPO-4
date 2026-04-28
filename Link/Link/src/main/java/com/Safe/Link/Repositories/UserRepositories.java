package com.Safe.Link.Repositories;

import com.Safe.Link.Entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepositories extends JpaRepository<User, Long> {
}
