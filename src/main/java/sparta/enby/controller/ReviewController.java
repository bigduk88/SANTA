package sparta.enby.controller;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import sparta.enby.dto.ReviewRequestDto;
import sparta.enby.dto.ReviewResponseDto;
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

    @GetMapping("/board/mating/review")
    public Page<ReviewResponseDto> getReviewPage(@RequestParam("page") int page, @RequestParam("size") int size){
        return reviewService.getReviewPage(page, size);
    }

    //review 작성
    @PostMapping("/board/mating/{board_id}/review")
    public ResponseEntity<String> writeReview(@PathVariable Long board_id, @ModelAttribute ReviewRequestDto reviewRequestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) throws IOException {
        return reviewService.writeReview(board_id, reviewRequestDto, userDetails);
    }

    @PutMapping("/board/mating/{board_id}/review/{review_id}")
    public ResponseEntity editReview(@ModelAttribute ReviewRequestDto reviewRequestDto, @PathVariable Long board_id, @PathVariable Long review_id, @AuthenticationPrincipal UserDetailsImpl userDetails){
        return reviewService.editReview(reviewRequestDto,board_id, review_id, userDetails);
    }

    @DeleteMapping("/board/mating/{board_id}/review/{review_id}")
    public ResponseEntity<String> deleteReview(@PathVariable Long review_id, @PathVariable Long board_id, @AuthenticationPrincipal UserDetailsImpl userDetails){
        return reviewService.deleteReview(review_id,board_id,userDetails);
    }