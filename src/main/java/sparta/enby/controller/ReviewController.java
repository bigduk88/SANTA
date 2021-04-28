package sparta.enby.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import sparta.enby.dto.ReviewRequestDto;
import sparta.enby.model.Review;
import sparta.enby.repository.ReviewRepository;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class ReviewController {

    private final ReviewRepository reviewRepository;

    @GetMapping("/board/review")
    public List<Review> getReview() {
        return reviewRepository.findAllByOrderByCreatedAtDesc();
    }

    @GetMapping("/board/reivew/{id}")
    public review getDetail (@PathVariable Long id) {
        return reviewRepository.findById(id).orElsethrow(
                () -> new IllegalArgumentException("null"));
    }

    @PostMapping("/board/review")
    public review createReveiw(@RequestBody ReviewRequestDto requestDto) {
        Review review = new Review(requestDto);
        reviewRepository.save(Review);
        return review;
    }
}