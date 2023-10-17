package talkhub.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.Base64Utils;
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
        newFile.renameTo(new File(uploadPath + "/" + resultFilename));

        return resultFilename;
    }
    public String uploadFiles(FileDto[] files, Long id){
        String dirPrefix = uploadPath + "/" + UUID.randomUUID() + id;
        File directory = new File(dirPrefix);
        directory.mkdir();
        for (FileDto file:files) {
            uploadFile(file, dirPrefix);
        }
        return dirPrefix;
    }

    private void uploadFile(FileDto fileDto, String dirPrefix){
        String partSeparator = ",";
        if (fileDto.getBase64().contains(partSeparator)) {
            String encoded = fileDto.getBase64().split(partSeparator)[1];
            fileDto.setBase64(encoded);
        }

        byte[] fileBytes = Base64.getDecoder().decode((fileDto.getBase64()));

        String uuidFile = UUID.randomUUID().toString();
        String resultFilename;
        resultFilename = uuidFile + "." +fileDto.getName();

        try (FileOutputStream fos = new FileOutputStream(uploadPath + "/" + dirPrefix + "/" + resultFilename)) {
            fos.write(fileBytes);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void deleteDir(String dirPrefix){
        File dir = new File(uploadPath+"/"+dirPrefix);
        dir.delete();
    }

}
