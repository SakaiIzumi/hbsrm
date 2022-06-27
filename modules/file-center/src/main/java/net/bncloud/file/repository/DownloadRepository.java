package net.bncloud.file.repository;

import net.bncloud.file.domain.Download;
import net.bncloud.file.domain.FileInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DownloadRepository extends JpaRepository<Download, Long> {

    void deleteByFileInfo(FileInfo fileInfo);

}
