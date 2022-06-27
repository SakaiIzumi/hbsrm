package net.bncloud.file.repository;

import net.bncloud.file.domain.LocalStorage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LocalStorageRepository extends JpaRepository<LocalStorage, Long> {
}
