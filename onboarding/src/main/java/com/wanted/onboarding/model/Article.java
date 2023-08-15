package com.wanted.onboarding.model;

import com.wanted.onboarding.dto.ArticleRequestDTO;
import lombok.*;

import javax.persistence.*;

@Getter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Table(name = "article")
public class Article extends BaseTime{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Long id;

    @Column(length = 20, nullable = false)
    private String title;

    @Column(length = 500, nullable = false)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private User writer;

    public void update(ArticleRequestDTO DTO){
        this.title = DTO.getTitle();
        this.content = DTO.getContent();
    }
}
