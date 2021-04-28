package sparta.enby.dto;

import lombok.Data;

@Data
public class RegistrationResponseDto {
    private boolean register;
    private String contents;

    public RegistrationResponseDto(boolean register, String contents){
        this.register = register;
        this.contents = contents;
    }
}
