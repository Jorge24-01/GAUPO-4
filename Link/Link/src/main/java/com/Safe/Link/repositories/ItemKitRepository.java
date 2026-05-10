package com.Safe.Link.repositories;

import com.Safe.Link.entities.ItemKit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemKitRepository extends JpaRepository<ItemKit, Long> {
    @Query("SELECT i FROM ItemKit i WHERE i.kit.id= :idKit")
    List<ItemKit>findByKitId(@Param("idKit") Long idKit);

    @Query("SELECT i FROM ItemKit i WHERE i.categoria = :categoria")
    List<ItemKit>findByCategoria(@Param("categoria")String categoria);
}
