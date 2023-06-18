package com.example.impatiboard.interfaces;

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

import java.util.List;

import static com.example.impatiboard.fixture.ArticleFixture.createArticle;
import static java.time.LocalDateTime.now;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.handler;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
@AutoConfigureRestDocs(uriHost = "api.impati-board.com", uriPort = 80)
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
    public void searchCommentInArticleTest() throws Exception {
        // when
        Article article = createArticle();
        given(customerFetcher.bringCustomer("token"))
                .willReturn(null);
        given(commentManager.search(article.getId(), null))
                .willReturn(createCommentResults(article, null));

        // expected
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
    public void registerCommentTest() throws Exception {
        // when
        Article article = createArticle();
        String token = "mockToken";
        Long customerId = 1L;
        CommentRegistrationRequest request = createRegistrationComment();
        AuthenticationCustomer customer = CustomerFixture.createCustomer(customerId);
        given(customerFetcher.bringCustomer(token)).willReturn(customer);
        willDoNothing().given(commentManager).register(article.getId(), customerId, request.content());

        // expected
        mockMvc.perform(post("/api/v1/comments/{articleId}", article.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-AUTH", token)
                        .content(objectMapper.writeValueAsString(request))
                )
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
    public void editCommentTest() throws Exception {
        // when
        Article article = createArticle();
        String token = "mockToken";
        Long customerId = 1L;
        CommentEditRequest request = createCommentEditRequest();
        AuthenticationCustomer customer = CustomerFixture.createCustomer(customerId);
        given(customerFetcher.bringCustomer(token))
                .willReturn(customer);
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
    public void deleteCommentTest() throws Exception {
        // when
        String token = "mockToken";
        Long customerId = 1L;
        CommentEditRequest request = createCommentEditRequest();
        AuthenticationCustomer customer = CustomerFixture.createCustomer(customerId);
        given(customerFetcher.bringCustomer(token)).willReturn(customer);
        willDoNothing().given(commentManager).remove(1L, customerId);

        // expected
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

    Comment createComment(Article article, String content, Long customerId) {
        return Comment.builder()
                .article(article)
                .content(content)
                .customerId(customerId)
                .createdAt(now())
                .build();
    }

    CommentResult createCommentResult(Article article, Long customerId) {
        return createCommentResults(article, customerId).get(0);
    }

    List<CommentResult> createCommentResults(Article article, Long customerId) {
        return List.of(
                CommentResult.of(createComment(article, "test comment", article.getCustomerId()), customerId),
                CommentResult.of(createComment(article, "좋아요", article.getCustomerId()), customerId),
                CommentResult.of(createComment(article, "싫어요", article.getCustomerId()), customerId)
        );
    }
}