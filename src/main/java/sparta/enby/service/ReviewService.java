package sparta.enby.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import sparta.enby.dto.ReviewRequestDto;
import sparta.enby.model.Board;
import sparta.enby.model.Review;
import sparta.enby.repository.BoardRepository;
import sparta.enby.repository.ReviewRepository;
import sparta.enby.uploader.Uploader;

import javax.transaction.Transactional;
import java.io.IOException;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final Uploader uploader;
    private final BoardRepository boardRepository;

    @Transactional
    public ResponseEntity<String> writeReview(Long board_id, ReviewRequestDto reviewRequestDto) throws IOException {
        String review_imgUrl = uploader.upload(reviewRequestDto.getReviewImg(), "static");
        Board board = boardRepository.findById(board_id).orElse(null);

        if (board == null){
            return new ResponseEntity<>("없는 게시글입니다", HttpStatus.BAD_REQUEST);
        }

        Review review = Review.builder()
                .contents(reviewRequestDto.getContents())
                .review_imgUrl(review_imgUrl)
                .build();
        Review newReview = reviewRepository.save(review);
        newReview.addBoard(board);
        return new ResponseEntity<>("성공적으로 리뷰를 등록하였습니다", HttpStatus.OK);
    }
}
