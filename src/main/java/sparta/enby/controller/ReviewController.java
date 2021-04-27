package sparta.enby.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import sparta.enby.dto.ReviewRequestDto;
import sparta.enby.model.Review;
import sparta.enby.service.ReviewService;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
public class ReviewController {
    private final ReviewService reviewService;

    @PostMapping("/board/mating/{board_id}/review")
    public ResponseEntity<String> writeReview(@PathVariable Long board_id, @ModelAttribute ReviewRequestDto reviewRequestDto) throws IOException {
        return reviewService.writeReview(board_id, reviewRequestDto);
    }
}
