package sparta.enby.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterRequestDto {
    private String contents;
    private boolean accepted;
}
