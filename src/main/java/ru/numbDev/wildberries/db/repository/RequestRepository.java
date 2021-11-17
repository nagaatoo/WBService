package ru.numbDev.wildberries.db.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.numbDev.wildberries.db.entity.RequestEntity;

import java.util.Optional;

public interface RequestRepository extends JpaRepository<RequestEntity, String> {

    @Query(nativeQuery = true, value = "select * from request where request_string in '%:str%'")
    Optional<RequestEntity> findByRequest(@Param("str") String str);

    @Query(nativeQuery = true, value = "select exists (select id from request where request_string in ':str')")
    boolean findEqualRequest(@Param("str") String str);

}
