package net.bncloud.file.web;

import net.bncloud.common.api.R;
import net.bncloud.file.domain.FileInfo;
import net.bncloud.file.service.FileService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/files")
public class FileController {

    private final FileService fileService;

    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    @PostMapping("/upload")
    public R<List<FileInfo>> upload(@RequestParam("file") MultipartFile[] files) throws Exception {
        List<FileInfo> fileList = fileService.upload(files);
        return R.data(fileList);
    }

    @GetMapping("/download/{id}")
    public void download(@PathVariable String id, HttpServletResponse httpServletResponse) throws Exception {
        fileService.download(id, httpServletResponse);
    }

    @PostMapping("/list")
    public R<List<FileInfo>> list(@RequestBody List<Long> idList){
        return R.data(fileService.list(idList));
    }

    @DeleteMapping("delete/{id}")
    public R delete(@PathVariable String id , HttpServletResponse httpServletResponse) throws IOException {
        fileService.delete(id,httpServletResponse);
        return R.success();
    }

}
