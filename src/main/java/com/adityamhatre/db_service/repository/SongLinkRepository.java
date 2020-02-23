package com.adityamhatre.db_service.repository;

import com.adityamhatre.db_service.entity.SongLink;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SongLinkRepository extends JpaRepository<SongLink, String> {

}
