package sparta.enby.dto;

import lombok.Data;

@Data
public class RegistrationResponseDto {
    private boolean accepted;
    private String contents;
    private String nickname;
    private String profile_img;
    private Long kakaoId;

    public RegistrationResponseDto(boolean accepted, String contents,String nickname, String profile_img, Long kakaoId){
        this.accepted = accepted;
        this.contents = contents;
        this.nickname = nickname;
        this.profile_img = profile_img;
        this.kakaoId = kakaoId;
    }
}
