package sparta.enby.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Setter
public class BoardRequestDto {

    private MultipartFile boardImg;
    private String title;
    private String contents;
    private String location;
    private Float longitude;
    private Float latitude;
    private String meetDate;
    private String meetTime;
}
