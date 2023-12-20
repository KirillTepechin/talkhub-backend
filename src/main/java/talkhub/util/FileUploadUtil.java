package talkhub.util;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import talkhub.dto.FileDto;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Base64;
import java.util.UUID;

@Component
public class FileUploadUtil {
    @Value("${upload.path}")
    private String uploadPath;

    public String uploadFile(MultipartFile file){
        Path destinationFile = Path.of(uploadPath).resolve(Paths.get(file.getOriginalFilename())).normalize().toAbsolutePath();

        InputStream inputStream = null;
        try {
            inputStream = file.getInputStream();
            Files.copy(inputStream, destinationFile, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        String uuidFile = UUID.randomUUID().toString();
        String resultFilename;
        resultFilename = uuidFile + "." +file.getOriginalFilename();

        File newFile = new File(String.valueOf(destinationFile));
        newFile.renameTo(new File(uploadPath  + resultFilename));

        return uploadPath + resultFilename;
    }
    public String uploadFiles(FileDto[] files, Long id){
        String dirPrefix = uploadPath + UUID.randomUUID() + id;
        File directory = new File(dirPrefix);
        directory.mkdir();
        for (FileDto file:files) {
            uploadFile(file, dirPrefix);
        }
        return dirPrefix;
    }

    private void uploadFile(FileDto dto, String dirPrefix){
        String fileName = UUID.randomUUID() + "." + dto.getName();
        String partSeparator = ",";
        if (dto.getBase64().contains(partSeparator)) {
            String encoded = dto.getBase64().split(partSeparator)[1];
            dto.setBase64(encoded);
        }

        byte[] bytes = Base64.getDecoder().decode(dto.getBase64().trim());

        try (FileOutputStream fos = new FileOutputStream(dirPrefix + "/" + fileName)) {
            fos.write(bytes);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void deleteDir(String dirPrefix){
        try {
            FileUtils.deleteDirectory(new File(dirPrefix));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void deleteFile(String path){
        File file = new File(path);
        file.delete();
    }
}
