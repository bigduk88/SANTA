package sparta.enby.Uploader;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class S3ServiceImpl implements S3Service{

    private final AmazonS3Client amazonS3Client;

    private static final String BUCKET = "hanghae99-gitlog";

    @Override
    public String getFileUrl(String fileName) {
        return String.ValueOf(amazonS3Client.getUrl(BUCEKT,fileName));
    }

    @Override
    public void removeFile(String fileName) {
        amazonS3Client.deleteObject(new DeleteObjectRequest(BUCKET, fileName));
    }

}
