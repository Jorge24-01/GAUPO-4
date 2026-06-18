package com.Safe.Link.repositories;

import com.Safe.Link.entities.ItemDelKit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemDelKitRepository extends JpaRepository<ItemDelKit, Long> {

    List<ItemDelKit> findByKitEmergencias_IdKit(Long idKit);
}
