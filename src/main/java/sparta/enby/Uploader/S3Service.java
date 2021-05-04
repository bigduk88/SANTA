package sparta.enby.Uploader;

import com.amazonaws.services.s3.model.ObjectMetadata;

import java.io.InputStream;

public class S3Service {
    void uploadFile(InputStream inputStream, Objectmetadata objectmetadata, String filename);

    String getFileUrl(String fileName);

    void removeFile(String fileName);
}
