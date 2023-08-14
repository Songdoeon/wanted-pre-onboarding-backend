package com.wanted.onboarding.controllder;

import com.wanted.onboarding.dto.ArticleRequestDTO;
import com.wanted.onboarding.dto.ArticleResponseDTO;
import com.wanted.onboarding.service.ArticleService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/article")
public class ArticleController {
    private final ArticleService service;
    @PostMapping("/register")
    public ResponseEntity<Long> register(@RequestBody ArticleRequestDTO articleRequestDTO){
        Long id = service.register(articleRequestDTO);
        return ResponseEntity.ok(id);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ArticleResponseDTO> read(@PathVariable Long id){
        ArticleResponseDTO article = service.read(id);
        return ResponseEntity.ok(article);
    }
    @PatchMapping("/{id}")
    public ResponseEntity<ArticleResponseDTO> update(@PathVariable Long id, @RequestBody ArticleRequestDTO articleRequestDTO){
        ArticleResponseDTO article = service.update(id,articleRequestDTO);
        return ResponseEntity.ok(article);
    }

    @GetMapping(value = "/pageable")
    public ResponseEntity<List<ArticleResponseDTO>> list(Pageable pageable) {
        List<ArticleResponseDTO> list = service.pageable(pageable);

        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Long> delete(@PathVariable Long id){
        service.delete(id);
        return ResponseEntity.ok(id);
    }
}
