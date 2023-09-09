package com.example.impatiboard.repositories;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import com.example.impatiboard.configure.ApplicationConfigure;
import com.example.impatiboard.fixture.ArticleFixture;
import com.example.impatiboard.models.Article;
import com.example.impatiboard.models.BoardType;
import com.example.impatiboard.models.ClientType;
import com.example.impatiboard.services.article.dto.ArticleSearchCondition;

@DataJpaTest
@EnableJpaAuditing(setDates = false, modifyOnCreate = false)
@Import({ApplicationConfigure.class, ArticleSearchRepository.class})
class ArticleSearchRepositoryTest {

	@Autowired
	private ArticleSearchRepository articleSearchRepository;

	@Autowired
	private ArticleRepository articleRepository;

	@Test
	@DisplayName("게시글 제목으로만 검색")
	void searchOnlyTitleName() {
		final List<Article> articles = new ArrayList<>();
		articles.add(ArticleFixture.createDefault().title("abc").build());
		articles.add(ArticleFixture.createDefault().title("ABCDEF").build());
		articles.add(ArticleFixture.createDefault().title("ZXDSA").build());
		articleRepository.saveAll(articles);
		final String conditionTitle = "abc";
		final ArticleSearchCondition condition = createBuilder(0, 10)
			.titleName(conditionTitle)
			.build();

		final Page<Article> result = articleSearchRepository.search(condition);

		final List<Article> contents = result.getContent();
		assertThat(contents).hasSize(2);
	}

	@Test
	@DisplayName("게시글 타입으로만 검색")
	void searchOnlyBoardType() {
		final List<Article> articles = new ArrayList<>();
		articles.add(ArticleFixture.createDefault().boardType(BoardType.ERROR).build());
		articles.add(ArticleFixture.createDefault().boardType(BoardType.ETC).build());
		articles.add(ArticleFixture.createDefault().boardType(BoardType.FEEDBACK).build());
		articles.add(ArticleFixture.createDefault().boardType(BoardType.SUGGESTION).build());
		articleRepository.saveAll(articles);
		final ArticleSearchCondition condition = createBuilder(0, 10)
			.boardType(BoardType.ERROR)
			.build();

		final Page<Article> result = articleSearchRepository.search(condition);

		List<Article> contents = result.getContent();
		assertThat(contents.size()).isEqualTo(1);
		assertThat(contents.get(0).getBoardType()).isEqualTo(BoardType.ERROR);
	}

	@Test
	@DisplayName("게시글을 작성한 닉네임으로 검색")
	void searchOnlyNickname() {
		final String otherNickname = "wnsduds1";
		final String nickname = "test";
		List<Article> articles = new ArrayList<>();
		articles.add(ArticleFixture.createDefault().nickname(otherNickname).build());
		articles.add(ArticleFixture.createDefault().nickname(nickname).build());
		articles.add(ArticleFixture.createDefault().nickname(nickname).build());
		articleRepository.saveAll(articles);
		final ArticleSearchCondition condition = createBuilder(0, 10)
			.nickname(nickname)
			.build();

		final Page<Article> result = articleSearchRepository.search(condition);

		final List<Article> contents = result.getContent();
		assertThat(contents).hasSize(2);
		for (var element : contents) {
			assertThat(element.getCreatedBy()).isEqualTo(nickname);
		}
	}

	@Test
	@DisplayName("게시글을 작성한 클라이언트 타입으로 검색")
	void searchOnlyClientType() {
		final List<Article> articles = new ArrayList<>();
		articles.add(ArticleFixture.createDefault().clientType(ClientType.HEALTH_CHECKER).build());
		articles.add(ArticleFixture.createDefault().clientType(ClientType.SERVICE_HUB).build());
		articles.add(ArticleFixture.createDefault().clientType(ClientType.SERVICE_HUB).build());
		articleRepository.saveAll(articles);
		final ClientType clientType = ClientType.CUSTOMER_SERVER;
		final ArticleSearchCondition condition = createBuilder(0, 10)
			.clientType(clientType)
			.build();

		final Page<Article> result = articleSearchRepository.search(condition);

		final List<Article> contents = result.getContent();
		assertThat(contents).isEmpty();
	}

	@Test
	@DisplayName("게시글을 작성한 날짜 이후 검색")
	void searchOnlyCreatedAt() {
		final LocalDate current = LocalDate.of(2023, 6, 14);
		final LocalDate before = LocalDate.of(2022, 12, 31);
		final LocalDate after = LocalDate.of(2025, 12, 31);
		final List<Article> articles = new ArrayList<>();
		articles.add(ArticleFixture.createDefault().createAt(current.atStartOfDay()).build());
		articles.add(ArticleFixture.createDefault().createAt(before.atStartOfDay()).build());
		articles.add(ArticleFixture.createDefault().createAt(before.atStartOfDay()).build());
		articles.add(ArticleFixture.createDefault().createAt(after.atStartOfDay()).build());
		articleRepository.saveAll(articles);
		final ArticleSearchCondition condition = createBuilder(0, 10)
			.createdDate(current)
			.build();

		final Page<Article> result = articleSearchRepository.search(condition);

		final List<Article> contents = result.getContent();
		assertThat(contents).hasSize(2);
		for (var element : contents) {
			assertThat(element.getCreatedAt().toLocalDate()).isNotEqualTo(before);
		}
	}

	@Test
	@DisplayName("게시글을 타입과 타이틀로 검색")
	void searchTypeAndTileName() {
		final String title = "title";
		final String otherTitle = "other";
		final List<Article> articles = new ArrayList<>();
		articles.add(ArticleFixture.createDefault().title(title).boardType(BoardType.ERROR).build());
		articles.add(ArticleFixture.createDefault().title(title).boardType(BoardType.SUGGESTION).build());
		articles.add(ArticleFixture.createDefault().title(title).boardType(BoardType.ERROR).build());
		articles.add(ArticleFixture.createDefault().title(title).boardType(BoardType.FEEDBACK).build());
		articles.add(ArticleFixture.createDefault().title(otherTitle).boardType(BoardType.ERROR).build());
		articles.add(ArticleFixture.createDefault().title(otherTitle).boardType(BoardType.SUGGESTION).build());
		articles.add(ArticleFixture.createDefault().title(otherTitle).boardType(BoardType.ERROR).build());
		articles.add(ArticleFixture.createDefault().title(otherTitle).boardType(BoardType.FEEDBACK).build());
		articleRepository.saveAll(articles);
		final ArticleSearchCondition condition = createBuilder(0, 10)
			.boardType(BoardType.ERROR)
			.titleName(title)
			.build();

		final Page<Article> result = articleSearchRepository.search(condition);

		final List<Article> contents = result.getContent();
		assertThat(contents).hasSize(2);
	}

	@Test
	@DisplayName("게시글 모든 조건 검색")
	void searchAllCondition() {
		final String title = "AAA";
		final BoardType boardType = BoardType.ETC;
		final String nickname = "test";
		final LocalDate current = LocalDate.of(2023, 6, 14);
		final LocalDate before = LocalDate.of(2022, 12, 31);
		final LocalDate after = LocalDate.of(2025, 12, 31);
		final List<Article> articles = new ArrayList<>();
		articles.add(ArticleFixture.create(title, nickname, current.atStartOfDay(), boardType));
		articles.add(ArticleFixture.create(title, nickname.toUpperCase(), current.atStartOfDay(), boardType));
		articles.add(ArticleFixture.create(title, nickname, after.atStartOfDay(), boardType));
		articles.add(ArticleFixture.create("BBB", nickname, after.atStartOfDay(), boardType));
		articles.add(ArticleFixture.create(title, nickname, after.atStartOfDay(), BoardType.ERROR));
		articles.add(ArticleFixture.create(title, nickname, before.atStartOfDay(), boardType));
		articles.add(ArticleFixture.create(title, "nickname", before.atStartOfDay(), boardType));
		articleRepository.saveAll(articles);
		final ArticleSearchCondition condition = createBuilder(0, 10)
			.boardType(boardType)
			.titleName(title)
			.nickname(nickname)
			.createdDate(current)
			.build();

		final Page<Article> result = articleSearchRepository.search(condition);

		final List<Article> contents = result.getContent();
		assertThat(contents).hasSize(3);
	}

	private ArticleSearchCondition.ArticleSearchConditionBuilder createBuilder(final int page, final int size) {
		final PageRequest pageRequest = PageRequest.of(page, size);
		return ArticleSearchCondition.builder().pageable(pageRequest);
	}
}
