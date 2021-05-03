package sparta.enby.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import sparta.enby.dto.ReviewRequestDto;
import sparta.enby.dto.ReviewResponseDto;
import sparta.enby.model.Account;
import sparta.enby.model.Board;
import sparta.enby.model.Review;
import sparta.enby.repository.BoardRepository;
import sparta.enby.repository.ReviewRepository;
import sparta.enby.security.UserDetailsImpl;
import sparta.enby.uploader.FileUploaderService;
import sparta.enby.uploader.S3Service;

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
    private final FileUploaderService fileUploaderService;


    public ResponseEntity getReviewList(){
        List<Review> reviews = reviewRepository.findAll(Sort.by(Sort.Direction.DESC,"createdAt"));
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


    //게시글 작성
    @Transactional
    public ResponseEntity<String> writeReview(Long board_id, ReviewRequestDto reviewRequestDto, Account account) throws IOException {
        String review_imgUrl = fileUploaderService.uploadImage(reviewRequestDto.getReviewImg());
        Board board = boardRepository.findById(board_id).orElse(null);

        if (board == null){
            return new ResponseEntity<>("없는 게시글입니다", HttpStatus.BAD_REQUEST);
        }

        Review review = Review.builder()
                .contents(reviewRequestDto.getContents())
                .review_imgUrl(review_imgUrl)
                .account(account)
                .build();
        Review newReview = reviewRepository.save(review);
        newReview.addBoardAndAccount(board,account);
        return new ResponseEntity<>("성공적으로 리뷰를 등록하였습니다", HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<String>deleteReview(Long review_id, Account account){
        Review review = reviewRepository.findById(review_id).orElse(null);
        Board board = boardRepository.findByReviews(review);
        if (review == null){
            return ResponseEntity.badRequest().body("없는 리뷰입니다");
        }
        if (!review.getAccount().getNickname().equals(account.getNickname())){
            return ResponseEntity.badRequest().body("다른 사용자의 리뷰를 지울 수 없습니다.");
        }
        review.removeBoardAndAccount(board,account);
        reviewRepository.deleteById(review_id);
        return ResponseEntity.ok().body("성공적으로 삭제하였습니다");
    }
}
