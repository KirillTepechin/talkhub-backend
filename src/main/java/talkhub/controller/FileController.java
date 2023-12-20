package talkhub.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import talkhub.service.FileService;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@RestController
@RequestMapping("/")
@CrossOrigin(origins = "*")
public class FileController {
    @Autowired
    private FileService fileService;
    @GetMapping("/get-files")
    public List<byte[]> getFiles(@RequestParam String path) {
        return fileService.getFiles(path);
    }
    @GetMapping("/get-file")
    public byte[] getFile(@RequestParam String path){
        return fileService.getFile(path);
    }
}
