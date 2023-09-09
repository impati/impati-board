package com.example.impatiboard.services.comment;

import static com.example.impatiboard.fixture.ArticleFixture.*;
import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import com.example.impatiboard.configure.JpaConfigure;
import com.example.impatiboard.models.Article;
import com.example.impatiboard.models.Comment;
import com.example.impatiboard.repositories.ArticleRepository;
import com.example.impatiboard.repositories.CommentRepository;
import com.example.impatiboard.services.comment.dto.CommentResult;
import com.example.impatiboard.services.common.EntityFinder;

@DataJpaTest
@Import({
	CommentManager.class,
	JpaConfigure.class,
	EntityFinder.class})
class CommentManagerTest {

	@Autowired
	private CommentManager commentManager;

	@Autowired
	private ArticleRepository articleRepository;

	@Autowired
	private CommentRepository commentRepository;

	@Test
	@DisplayName("댓글 저장")
	void registerCommentInArticle() {
		final Article article = createDefaults();
		articleRepository.save(article);
		final Long commentWriter = 99L;

		// when
		commentManager.register(article.getId(), commentWriter, "안녕하세요");

		// then
		assertThat(commentRepository.findAll()).hasSize(1);
	}

	@Test
	@DisplayName("게시글에 대한 댓글 조회")
	void searchCommentsInArticle() {
		final Article article = createDefaults();
		articleRepository.save(article);
		final Long targetWriter = 99L;
		final Long otherWriter = 200L;
		commentManager.register(article.getId(), targetWriter, "안녕하세요");
		commentManager.register(article.getId(), targetWriter, "반갑습니다.");
		commentManager.register(article.getId(), otherWriter, "헬로우");

		final List<CommentResult> results = commentManager.search(article.getId(), targetWriter);

		assertThat(results).hasSize(3);
		assertThat(results.stream().filter(CommentResult::isWritten).count()).isEqualTo(2);
	}

	@Test
	@DisplayName("게시글 댓글 수정")
	void editCommentInArticle() {
		final Article article = createDefaults();
		articleRepository.save(article);
		final Long targetWriter = 99L;
		final String editContent = "테스트";
		commentManager.register(article.getId(), targetWriter, "안녕하세요");
		final CommentResult commentResult = commentManager.search(article.getId(), targetWriter).get(0);

		final CommentResult result = commentManager.edit(commentResult.commentId(), targetWriter, editContent);

		final Comment comment = commentRepository.findById(commentResult.commentId()).get();
		assertThat(comment.getContent()).isEqualTo(editContent);
		assertThat(result.content()).isEqualTo(editContent);
	}

	@Test
	@DisplayName("게시글 댓글 삭제")
	void removeCommentInArticle() {
		// given
		final Article article = createDefaults();
		articleRepository.save(article);
		final Long targetWriter = 99L;
		commentManager.register(article.getId(), targetWriter, "안녕하세요");
		final CommentResult commentResult = commentManager.search(article.getId(), targetWriter).get(0);

		// when
		commentManager.remove(commentResult.commentId(), targetWriter);

		// then
		assertThat(commentRepository.count()).isZero();
	}
}
