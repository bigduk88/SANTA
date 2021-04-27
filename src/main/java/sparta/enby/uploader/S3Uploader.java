package sparta.enby.uploader;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

@RequiredArgsConstructor
@Component
//실질적인 s3에 파일 업로드 해주는 부분
public class S3Uploader implements Uploader{
    // 임시로 저장할 디렉터리 위치
    private final static String TEMP_FILE_PATH = "src/main/resources/";
    // 아마존 s3와 연결시켜주는 sdk
    private final AmazonS3Client amazonS3Client;
    // s3 bucket 이름
    public String bucket = "hanghae99-gitlog";

    //파일을 로컬 스토리지에 업로드 하는 부분.
    public String upload(MultipartFile multipartFile, String dirName) throws IOException {
        //multipartfile을 file로 변화 해주는 부분
        // 밑에 FileOutputStream 으로 새로운 파일을 만들어 주는 부분이 선언되어 있다.
//        System.out.println(multipartFile);
        File convertedFile = convert(multipartFile);
        //uploader 의 upload에서 파일이 비어있는지 확인
        return upload(convertedFile, dirName);
    }

    private String upload(File uploadFile, String dirName) {
        // 파일이름은 directory/파일이름
        String fileName = dirName + "/" + uploadFile.getName();
        //이미지 업로드 하고 받아올 s3 access URL
        String uploadImageUrl = putS3(uploadFile, fileName);
        //로컬에 있는 파일을 지우는 부분
        removeNewFile(uploadFile);
        return uploadImageUrl;
    } // 실질적으로 S3와 연동해서 파일을 올리는 부분
    private String putS3(File uploadFile, String fileName) {
        // 업로드 하려는 파일과 파일 이름을 받아서
        //s3와 연결하는 amazonS3Client가 pubobject를 통해서 파일을 업로드 한다.
        //업로드 되고 나서 해당 객체가 보여져야하기 때문에 설정된 acl을 통해 read 권한만 설정해준다.
        //Canned acl : Amazon S3 supports a set of predefined grants, known as canned ACLs.
        //https://docs.aws.amazon.com/AmazonS3/latest/userguide/acl-overview.html#canned-acl
        amazonS3Client.putObject(new PutObjectRequest(bucket, fileName, uploadFile).withCannedAcl(CannedAccessControlList.PublicRead));
        //업로드 하고 나서 객체를 접근 할 수 있는 URL을 string 형식으로 받아오는 부분
        return amazonS3Client.getUrl(bucket, fileName).toString();
    }
    // s3에 올려진 파일은 로컬에 저장되어 있을 필요가 없기때문에 해당 파일을 지워주고
    private void removeNewFile(File targetFile) {
        if (targetFile.delete()) {
            return;
        }
    }

    //  Multipartfile로 올려져 있던 파일을 일반 파일로 변환해주는 부분,
    private File convert(MultipartFile file) throws IOException {
        // 변경되 파일을 임시 디렉터리 + 원래 파일 이름을 더해서
        File convertFile = new File(TEMP_FILE_PATH + file.getOriginalFilename());
        // 만약에 이 파일이 임시 디렉토리에 없다면 새로운 파일로 만들어주는데
        if (convertFile.createNewFile()) {
            //파일로 만들어 주는 부분
            System.out.println("converting started");
            try (FileOutputStream fos = new FileOutputStream(convertFile)) {
                fos.write(file.getBytes());
            }
            //변경되 파일을 반환하는 부분

            return convertFile;
        }
        throw new IllegalArgumentException(String.format("파일 변환이 실패했습니다. 파일 이름: %s", file.getName()));
    }
}
