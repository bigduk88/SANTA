package sparta.enby.service;

import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.http.fileupload.FileUpload;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import sparta.enby.Uploader.FileUploaderService;
import sparta.enby.Uploader.S3Service;
import sparta.enby.dto.ReviewRequestDto;
import sparta.enby.dto.ReviewResponseDto;
import sparta.enby.model.Board;
import sparta.enby.model.Review;
import sparta.enby.repository.AccountRepository;
import sparta.enby.repository.BoardRepository;
import sparta.enby.repository.ReviewRepository;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final S3Service uploader;
    private final BoardRepository boardRepository;
    private final FileUploaderService fileUploadService;
    private final AccountRepository accountRepository;

    public ResponseEntity getReviewList() {
        List<Review> reviews = reviewRepository.findAll(Sort.by(Sort.Direction.DESC, "createdAt"));
        List<ReviewResponseDto> toList = reviews.stream().map(
                review -> new ReviewResponseDto(
                        review.getId(),
                        review.getReview_imgUrl(),
                        review.getContents(),
                        review.getBoard().getId()
                )
        ).collect(Collectors.toList());
        return ResponseEntity.ok().body(toList);
    }

    public Page

    @Transactional
    public ResponseEntity<String> writeReview(Long board_id, ReviewRequestDto reviewRequestDto) throws IOException {
        String review_imgUrl = fileUploaderService.uploadImage(reviewRequestDto.getReviewImg());
        Board board = boardRepository.findById(board_id).orElse(null);

        if (board == null) {
            return new ResponseEntity<>("없는 게시글입니다", HttpStatus.BAD_REQUEST);
        }

        Review review = Review.builder()
                .contenst(reviewRequestDto.getContents())
                .review_imgUrl(review_imgUrl)
                .build();
        Review newReview = reviewRepository.save(review);
        newReview.addBoard(board);
        return new ResponseEntity<>("성공적으로 리뷰를 등록하였습니다", HttpStatus.OK);
    }
}
