package sparta.enby.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import sparta.enby.dto.ReviewRequestDto;
import sparta.enby.model.Review;
import sparta.enby.security.UserDetailsImpl;
import sparta.enby.service.ReviewService;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
public class ReviewController {
    private final ReviewService reviewService;

    @GetMapping("/main/review")
    public ResponseEntity getReviewList(){
        return reviewService.getReviewList();
    }

    //review 작성
    @PostMapping("/board/mating/{board_id}/review")
    public ResponseEntity<String> writeReview(@PathVariable Long board_id, @ModelAttribute ReviewRequestDto reviewRequestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) throws IOException {
        return reviewService.writeReview(board_id, reviewRequestDto, userDetails.getAccount());
    }

    @DeleteMapping("/board/review/{review_id}")
    public ResponseEntity<String> deleteReview(@PathVariable Long review_id, @AuthenticationPrincipal UserDetailsImpl userDetails){
        return reviewService.deleteReview(review_id,userDetails.getAccount());
    }
}
