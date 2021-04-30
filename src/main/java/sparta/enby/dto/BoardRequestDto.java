package sparta.enby.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.Base64;

@Getter
@Setter
public class BoardRequestDto {

    private MultipartFile boardImg;
    private String title;
    private String contents;
    private String location;
    private String meetTime;
}
