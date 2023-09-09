package com.example.impatiboard.interfaces;

import static com.example.impatiboard.fixture.ArticleFixture.*;
import static java.time.LocalDateTime.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.*;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.operation.preprocess.Preprocessors;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import com.example.impatiboard.fixture.CustomerFixture;
import com.example.impatiboard.interfaces.argument.AuthenticationCustomer;
import com.example.impatiboard.interfaces.argument.CustomerFetcher;
import com.example.impatiboard.interfaces.request.CommentEditRequest;
import com.example.impatiboard.interfaces.request.CommentRegistrationRequest;
import com.example.impatiboard.models.Article;
import com.example.impatiboard.models.Comment;
import com.example.impatiboard.services.comment.CommentManager;
import com.example.impatiboard.services.comment.dto.CommentResult;
import com.fasterxml.jackson.databind.ObjectMapper;

@AutoConfigureMockMvc
@SpringBootTest
@AutoConfigureRestDocs(uriHost = "service-hub.org/board", uriPort = 80)
@ExtendWith(RestDocumentationExtension.class)
class CommentControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@MockBean
	private CustomerFetcher customerFetcher;

	@MockBean
	private CommentManager commentManager;

	@Test
	@DisplayName("[GET] [/api/v1/comments/{articleId}] 게시글 댓글 모두 조회")
	void searchCommentInArticleTest() throws Exception {
		final Article article = createArticle();
		given(customerFetcher.bringCustomer("token"))
			.willReturn(null);
		given(commentManager.search(article.getId(), null))
			.willReturn(createCommentResults(article, null));

		mockMvc.perform(get("/api/v1/comments/{articleId}", article.getId())
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(handler().methodName("searchCommentInArticle"))
			.andDo(MockMvcResultHandlers.print())
			.andDo(document(
				"comment/search",
				preprocessRequest(Preprocessors.prettyPrint()),
				preprocessResponse(Preprocessors.prettyPrint()),
				pathParameters(
					parameterWithName("articleId").description("게시글 ID")),
				requestHeaders(
					headerWithName("X-AUTH").description("인증 토큰").optional()),
				responseFields(
					fieldWithPath("data[].content").description("댓글 내용"),
					fieldWithPath("data[].commentId").description("댓글 ID"),
					fieldWithPath("data[].articleId").description("게시글 ID"),
					fieldWithPath("data[].nickname").description("댓글 작성자 닉네임"),
					fieldWithPath("data[].createdAt").description("댓글 생성일"),
					fieldWithPath("data[].isWritten").description("댓글 작성자 여부"),
					fieldWithPath("error").type(JsonFieldType.NULL).description("에러 필드"))));
	}

	@Test
	@DisplayName("[POST] [/api/v1/comments/{articleId}] 게시글에 댓글 다는 API 테스트")
	void registerCommentTest() throws Exception {
		final Article article = createArticle();
		final String token = "mockToken";
		final Long customerId = 1L;
		final CommentRegistrationRequest request = createRegistrationComment();
		final AuthenticationCustomer customer = CustomerFixture.createCustomer(customerId);
		given(customerFetcher.bringCustomer(token)).willReturn(customer);
		willDoNothing().given(commentManager).register(article.getId(), customerId, request.content());

		mockMvc.perform(post("/api/v1/comments/{articleId}", article.getId())
				.contentType(MediaType.APPLICATION_JSON)
				.header("X-AUTH", token)
				.content(objectMapper.writeValueAsString(request)))
			.andExpect(status().isOk())
			.andExpect(handler().methodName("registerComment"))
			.andDo(MockMvcResultHandlers.print())
			.andDo(document(
				"comment/register",
				preprocessRequest(Preprocessors.prettyPrint()),
				preprocessResponse(Preprocessors.prettyPrint()),
				pathParameters(
					parameterWithName("articleId").description("게시글 ID")),
				requestHeaders(
					headerWithName("X-AUTH").description("인증 토큰")),
				requestFields(
					fieldWithPath("content").type(JsonFieldType.STRING).description("댓글 내용")),
				responseFields(
					fieldWithPath("data").type(JsonFieldType.STRING).optional().description("응답 상태"),
					fieldWithPath("error").type(JsonFieldType.NULL).description("에러 필드"))));

	}

	@Test
	@DisplayName("[PUT] [/api/v1/comments/comment/{commentId]")
	void editCommentTest() throws Exception {
		final Article article = createArticle();
		final String token = "mockToken";
		final Long customerId = 1L;
		final CommentEditRequest request = createCommentEditRequest();
		final AuthenticationCustomer customer = CustomerFixture.createCustomer(customerId);
		given(customerFetcher.bringCustomer(token)).willReturn(customer);
		given(commentManager.edit(1L, customerId, request.content()))
			.willReturn(createCommentResult(article, customerId));

		// expected
		mockMvc.perform(put("/api/v1/comments/comment/{commentId}", 1L)
				.contentType(MediaType.APPLICATION_JSON)
				.header("X-AUTH", token)
				.content(objectMapper.writeValueAsString(request)))
			.andExpect(status().isOk())
			.andExpect(handler().methodName("editComment"))
			.andDo(MockMvcResultHandlers.print())
			.andDo(document(
				"comment/edit",
				preprocessRequest(Preprocessors.prettyPrint()),
				preprocessResponse(Preprocessors.prettyPrint()),
				pathParameters(
					parameterWithName("commentId").description("댓글 ID")),
				requestHeaders(
					headerWithName("X-AUTH").description("인증 토큰")),
				requestFields(
					fieldWithPath("content").type(JsonFieldType.STRING).description("댓글 내용")),
				responseFields(
					fieldWithPath("data.content").description("댓글 내용"),
					fieldWithPath("data.commentId").description("댓글 ID"),
					fieldWithPath("data.articleId").description("게시글 ID"),
					fieldWithPath("data.nickname").description("댓글 작성자 닉네임"),
					fieldWithPath("data.createdAt").description("댓글 생성일"),
					fieldWithPath("data.isWritten").description("댓글 작성자 여부"),
					fieldWithPath("error").type(JsonFieldType.NULL).description("에러 필드"))));

	}

	@Test
	@DisplayName("[DELETE] [/api/v1/comments/comment/{commentId]")
	void deleteCommentTest() throws Exception {
		final String token = "mockToken";
		final Long customerId = 1L;
		final CommentEditRequest request = createCommentEditRequest();
		final AuthenticationCustomer customer = CustomerFixture.createCustomer(customerId);
		given(customerFetcher.bringCustomer(token)).willReturn(customer);
		willDoNothing().given(commentManager).remove(1L, customerId);

		mockMvc.perform(delete("/api/v1/comments/comment/{commentId}", 1L)
				.contentType(MediaType.APPLICATION_JSON)
				.header("X-AUTH", token)
				.content(objectMapper.writeValueAsString(request)))
			.andExpect(status().isOk())
			.andExpect(handler().methodName("deleteComment"))
			.andDo(MockMvcResultHandlers.print())
			.andDo(document(
				"comment/delete",
				preprocessRequest(Preprocessors.prettyPrint()),
				preprocessResponse(Preprocessors.prettyPrint()),
				pathParameters(
					parameterWithName("commentId").description("댓글 ID")),
				requestHeaders(
					headerWithName("X-AUTH").description("인증 토큰")),
				responseFields(
					fieldWithPath("data").type(JsonFieldType.STRING).optional().description("응답 상태"),
					fieldWithPath("error").type(JsonFieldType.NULL).description("에러 필드"))));

	}

	CommentEditRequest createCommentEditRequest() {
		return new CommentEditRequest("edit comment");
	}

	CommentRegistrationRequest createRegistrationComment() {
		return new CommentRegistrationRequest("test comment");
	}

	Comment createComment(
		final Article article,
		final String content,
		final Long customerId
	) {
		return Comment.builder()
			.article(article)
			.content(content)
			.customerId(customerId)
			.createdAt(now())
			.build();
	}

	CommentResult createCommentResult(final Article article, final Long customerId) {
		return createCommentResults(article, customerId).get(0);
	}

	List<CommentResult> createCommentResults(final Article article, final Long customerId) {
		return List.of(
			CommentResult.of(createComment(article, "test comment", article.getCustomerId()), customerId),
			CommentResult.of(createComment(article, "좋아요", article.getCustomerId()), customerId),
			CommentResult.of(createComment(article, "싫어요", article.getCustomerId()), customerId)
		);
	}
}
