package ru.numbDev.wildberries.db.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.numbDev.wildberries.db.entity.MetaEntity;

@Repository
public interface MetaRepository extends JpaRepository<MetaEntity, Long> {

}
