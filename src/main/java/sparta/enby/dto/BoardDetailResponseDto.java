package sparta.enby.dto;

import lombok.Data;
import org.apache.tomcat.jni.Local;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class BoardDetailResponseDto {
    private String createdBy;
    private Long id;
    private String title;
    private String contents;
    private String board_imgUrl;
    private String location;
    private LocalDateTime meetTime;
    private List<ReviewResponseDto> reviews = new ArrayList<>();
    private List<RegistrationResponseDto> registrations = new ArrayList<>();

    public BoardDetailResponseDto(Long id, String cretedBy, String title, String contents, LocalDateTime meetTime, String location, String board_imgUrl, List<ReviewResponseDto>, List<RegistrationResponseDto> registrations) {
        this.id = id;
        this.createdBy = cretedBy;
        this.title = title;
        this.contents = contents;
        this.meetTime = meetTime;
        this.location = location;
        this.board_imgUrl = board_imgUrl;
        this.reviews = reviews;
        this.registrations = registrations;
    }
}
