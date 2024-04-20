package com.fastcampus.projectboard.domain;

import com.fastcampus.projectboard.Repository.ArticleCommentRepository;
import com.fastcampus.projectboard.Repository.ArticleRepository;
import com.fastcampus.projectboard.config.JpaConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@DisplayName("JPA 연결 테스트")
@Import(JpaConfig.class)
@DataJpaTest
class JpaRepositoryTest {
    private final ArticleRepository articleRepository;
    private final ArticleCommentRepository articleCommentRepository;


    public JpaRepositoryTest(@Autowired ArticleRepository articleRepository,
                             @Autowired ArticleCommentRepository articleCommentRepository) {
        this.articleRepository = articleRepository;
        this.articleCommentRepository = articleCommentRepository;
    }

    @DisplayName("select 테스트")
    @Test
    void givenTestData_whenSelecting_thenWorksFine() {

        List<Article> articles = articleRepository.findAll();

        assertThat(articles)
                .isNotNull()
                .hasSize(123);

    }


    @DisplayName("insert 테스트")
    @Test
    void givenTestData_whenInserting_thenWorksFine() {

        long previousCount = articleRepository.count();

        Article savedArticle = articleRepository.save(Article.of("new aritcle", "new content", "Red"));

        assertThat(articleRepository.count()).isEqualTo(previousCount + 1);


    }

    @DisplayName("update 테스트")
    @Test
    void givenTestData_whenUpdating_thenWorksFine() {

        Article article = articleRepository.findById(1L).orElseThrow();

        String updatedHashtag = "#springboot";

        article.setHashtag(updatedHashtag);
        Article savedArticle = articleRepository.saveAndFlush(article);

        assertThat(savedArticle).hasFieldOrPropertyWithValue("hashtag",updatedHashtag);


    }


    @DisplayName("delete 테스트")
    @Test
    void givenTestData_whenDeleteing_thenWorksFine() {

        Article article = articleRepository.findById(1L).orElseThrow();

        long previousArticleCount = articleRepository.count();
        long previousArticleCommentCount = articleCommentRepository.count();

        long deletedArticleComments = article.getArticleComments().size();


        articleRepository.deleteById(1L);

        assertThat(previousArticleCount - 1).isEqualTo(articleRepository.count());
        assertThat(previousArticleCommentCount - deletedArticleComments).isEqualTo(articleCommentRepository.count());




    }
}
