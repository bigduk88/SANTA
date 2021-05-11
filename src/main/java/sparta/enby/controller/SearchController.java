package sparta.enby.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import sparta.enby.service.SearchService;

import java.util.Map;

@RestController
@RequiredArgsConstructor
public class SearchController {
    private final SearchService searchService;

    @PostMapping("/board/search")
    public ResponseEntity<Map<String,Object>> boardSearch(@RequestParam("Keyword") String keyword){
        return searchService.boardSearch(keyword);
    }

    @PostMapping("/review/search")
    public ResponseEntity<Map<String,Object>> reviewSearch(@RequestParam("Keyword") String keyword){
        return searchService.reviewSearch(keyword);
    }
}
