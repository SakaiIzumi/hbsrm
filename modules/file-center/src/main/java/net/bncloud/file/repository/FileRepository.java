package net.bncloud.file.repository;

import net.bncloud.common.repository.BaseRepository;
import net.bncloud.file.domain.FileInfo;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FileRepository extends BaseRepository<FileInfo, Long> {
    Optional<FileInfo> findOneByFilename(String filename);
}
