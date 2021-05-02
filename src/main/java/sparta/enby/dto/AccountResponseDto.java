package sparta.enby.dto;

import lombok.Data;

@Data
public class AccountResponseDto {
    private String nickname;

    public AccountResponseDto(String nickname){
        this.nickname = nickname;
    }
}
