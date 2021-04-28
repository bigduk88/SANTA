package sparta.enby.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sparta.enby.dto.ReviewRequestDto;
import sparta.enby.model.Review;

import javax.transaction.Transactional;

@RequiredArgsConstructor
@Service
public class ReviewService {

    @Transactional
    public Long update(Long id, ReviewRequestDto requestDto) {
        Review review = reviewRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("아이디가 존재하지 않습니다."));
        review.update(requestDto);
        return review.getId();
    }
}
