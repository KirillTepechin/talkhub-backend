package talkhub.service;

import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@Service
public class FileService {
    public List<byte[]> getFiles(String path){
        File directory = new File(path);
        File[] files = directory.listFiles();

        List<String> fileNames = new ArrayList<>();
        if (files != null) {
            for (File file : files) {
                if (file.isFile()) {
                    fileNames.add(file.getName());
                }
            }
        }
        List<byte[]> bytes = new ArrayList<>();
        for (String fileName: fileNames) {
            try {
                bytes.add(Files.readAllBytes(new File(path + "/" + fileName).toPath()));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        return bytes;
    }
    public byte[] getFile(String path) {
        byte[] bytes = new byte[0];
        try {
            bytes = Files.readAllBytes(new File(path).toPath());
        } catch (IOException e) {
            return Base64.getEncoder().encode(bytes);
        }
        return Base64.getEncoder().encode(bytes);
    }
}
