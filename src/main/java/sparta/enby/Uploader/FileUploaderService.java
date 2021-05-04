package sparta.enby.Uploader;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;
import java.io.InputStream;

@Service
@Transactional
@RequiredArgsConstructor
public class FileUploaderService {

    private static final String URL = "https://hanghae99-gitlog.s3.ap-northeast-2.amazonaws.com/";

    private final S3Service s3Service;

    public String uploadImage(MultipartFile multipartFile) {
        String fileName = createFileName(multipartFile.getOriginalFilename());

        ObjectMetaData objectMetaData = new ObjectMetadate();
        objectMetaData.setContentType(multipartFile.getContentType());

        try (InputStream inputStream = multipartFile.getInputStream()) {
            s3Service.uploadFile(inputStream, objectMetadata, fileNmae);
        } catch (IOException e) {
            throw new IllegalArgumentException(String.format("파일 변환 중 에러가 발행하였습니다(%s)", multipartFile.getOriginalFilename()));
        }
        return s3Service.getFileUrl(fileName);
    }


}
