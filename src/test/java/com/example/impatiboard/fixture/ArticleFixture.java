package com.example.impatiboard.fixture;

import static java.time.LocalDateTime.*;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Random;

import org.jeasy.random.EasyRandom;
import org.jeasy.random.EasyRandomParameters;

import com.example.impatiboard.models.Article;
import com.example.impatiboard.models.BoardType;
import com.example.impatiboard.models.ClientType;

public class ArticleFixture {

	public static final BoardType DEFAULT_BOARD_TYPE = BoardType.ERROR;
	public static final Long DEFAULT_CUSTOMER_ID = 1L;
	public static final String DEFAULT_TITLE = "에러 발생했습니다";
	public static final String DEFAULT_CONTENT = "문세 상황 : ... ";
	public static final LocalDateTime DEFAULT_CREATED_AT = now();
	public static final String DEFAULT_NICKNAME = "impati";
	public static final ClientType DEFAULT_CLIENT_TYPE = ClientType.SERVICE_HUB;

	public static Article createArticle() {
		final EasyRandomParameters params = new EasyRandomParameters()
			.seed(new Random().nextLong())
			.charset(StandardCharsets.UTF_8)
			.stringLengthRange(5, 20)
			.ignoreRandomizationErrors(true);
		final EasyRandom easyRandom = new EasyRandom(params);
		return easyRandom.nextObject(Article.class);
	}

	public static Article createDefaults() {
		return createDefault().build();
	}

	public static Article create(
		final String title,
		final String nickname,
		final LocalDateTime localDateTime,
		final BoardType boardType
	) {
		return createDefault()
			.title(title)
			.nickname(nickname)
			.createAt(localDateTime)
			.boardType(boardType)
			.build();
	}

	public static Article.ArticleBuilder createDefault() {
		return Article.builder()
			.customerId(DEFAULT_CUSTOMER_ID)
			.boardType(DEFAULT_BOARD_TYPE)
			.title(DEFAULT_TITLE)
			.content(DEFAULT_CONTENT)
			.nickname(DEFAULT_NICKNAME)
			.createAt(DEFAULT_CREATED_AT)
			.updatedAt(DEFAULT_CREATED_AT)
			.clientType(DEFAULT_CLIENT_TYPE);
	}
}
