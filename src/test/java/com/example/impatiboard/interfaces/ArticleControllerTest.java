package com.example.impatiboard.interfaces;


import com.example.impatiboard.interfaces.argument.AuthenticationCustomer;
import com.example.impatiboard.interfaces.argument.CustomerFetcher;
import com.example.impatiboard.interfaces.request.ArticleEditRequest;
import com.example.impatiboard.interfaces.request.ArticleRegistrationRequest;
import com.example.impatiboard.interfaces.request.ArticleSearchRequest;
import com.example.impatiboard.models.Article;
import com.example.impatiboard.models.BoardType;
import com.example.impatiboard.models.ClientType;
import com.example.impatiboard.services.article.ArticleEditor;
import com.example.impatiboard.services.article.ArticleFinder;
import com.example.impatiboard.services.article.ArticleRegister;
import com.example.impatiboard.services.article.ArticleRemover;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.operation.preprocess.Preprocessors;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static com.example.impatiboard.fixture.ArticleFixture.createArticle;
import static com.example.impatiboard.fixture.CustomerFixture.createCustomer;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.handler;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
@AutoConfigureRestDocs(uriHost = "api.impati-board.com", uriPort = 80)
@ExtendWith(RestDocumentationExtension.class)
class ArticleControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ArticleFinder articleFinder;

    @MockBean
    private CustomerFetcher customerFetcher;

    @MockBean
    private ArticleRegister articleRegister;

    @MockBean
    private ArticleEditor articleEditor;

    @MockBean
    private ArticleRemover articleRemover;

    @Test
    @DisplayName("[GET] [/api/v1/articles] 게시글 페이징 조회")
    public void searchArticlesTest() throws Exception {

        // when
        ArticleSearchRequest request = createSearchRequest();
        Pageable pageable = PageRequest.of(0, 10);
        Page<Article> articleResponses = stubArticleResponseWithPage(3, pageable);
        given(articleFinder.search(request.toCondition(pageable))).willReturn(articleResponses);
        //expected
        mockMvc.perform(post("/api/v1/articles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("size", String.valueOf(pageable.getPageSize()))
                        .param("page", String.valueOf(pageable.getOffset()))
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(handler().methodName("searchArticles"))
                .andDo(MockMvcResultHandlers.print())
                .andDo(document(
                        "article/",
                        preprocessRequest(Preprocessors.prettyPrint()),
                        preprocessResponse(Preprocessors.prettyPrint()),
                        relaxedRequestParameters(
                                parameterWithName("size").description("페이지 사이즈").optional(),
                                parameterWithName("page").description("페이지 번호").optional()),
                        requestFields(
                                fieldWithPath("titleName").optional().description("게시글 제목"),
                                fieldWithPath("boardType").optional().description("ERROR,FEEDBACK,SUGGESTION,ETC"),
                                fieldWithPath("clientType").optional().description("SERVICE_HUB,CUSTOMER_SERVER,HEALTH_CHECKER"),
                                fieldWithPath("nickname").description("게시글 작성자 닉네임"),
                                fieldWithPath("createdDate").optional().description("게시글 생성일")),
                        responseFields(
                                fieldWithPath("data.content[].articleId").description("게시글 ID"),
                                fieldWithPath("data.content[].title").description("게시글 제목"),
                                fieldWithPath("data.content[].boardType").description("ERROR,FEEDBACK,SUGGESTION,ETC"),
                                fieldWithPath("data.content[].clientType").description("SERVICE_HUB,CUSTOMER_SERVER,HEALTH_CHECKER"),
                                fieldWithPath("data.content[].nickname").description("게시글 작성자 닉네임"),
                                fieldWithPath("data.content[].createdAt").description("게시글 생성일"),
                                fieldWithPath("data.pageable.page").description("현재 페이지"),
                                fieldWithPath("data.pageable.size").description("페이지 사이즈"),
                                fieldWithPath("data.pageable.sort.orders").description(" - "),
                                fieldWithPath("data.total").description("게시글 총 수"),
                                fieldWithPath("error").type(JsonFieldType.NULL).description("에러 필드"))));
    }


    @Test
    @DisplayName("[GET] [/api/v1/articles/{articleId}] 게시글 단일 조회 - 인증된 사용자")
    public void searchArticleTestWithAuthenticationCustomer() throws Exception {

        // when
        String token = "mockToken";
        Article article = createArticle();
        AuthenticationCustomer authenticationCustomer = createCustomer(article.getCustomerId());
        given(customerFetcher.bringCustomer(token)).willReturn(authenticationCustomer);
        given(articleFinder.search(article.getId())).willReturn(article);

        //expected
        mockMvc.perform(get("/api/v1/articles/{articleId}", article.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-AUTH", token))
                .andExpect(status().isOk())
                .andExpect(handler().methodName("searchArticle"))
                .andDo(MockMvcResultHandlers.print())
                .andDo(document(
                        "article/search",
                        preprocessRequest(Preprocessors.prettyPrint()),
                        preprocessResponse(Preprocessors.prettyPrint()),
                        pathParameters(
                                parameterWithName("articleId").description("게시글 ID")),
                        requestHeaders(
                                headerWithName("X-AUTH").description("인증 헤더 이름").optional()),
                        responseFields(
                                fieldWithPath("data.articleId").description("게시글 ID"),
                                fieldWithPath("data.title").description("게시글 제목"),
                                fieldWithPath("data.boardType").description("ERROR,FEEDBACK,SUGGESTION,ETC"),
                                fieldWithPath("data.clientType").description("SERVICE_HUB,CUSTOMER_SERVER,HEALTH_CHECKER"),
                                fieldWithPath("data.nickname").description("게시글 작성자 닉네임"),
                                fieldWithPath("data.createdAt").description("게시글 생성일"),
                                fieldWithPath("data.content").description("게시글 내용"),
                                fieldWithPath("data.isWritten").description("게시글 작성자 여부"),
                                fieldWithPath("error").type(JsonFieldType.NULL).description("에러 필드"))));
    }

    @Test
    @DisplayName("[GET] [/api/v1/articles/{articleId}] 게시글 단일 조회 - 익명의 사용자")
    public void searchArticleTestWithAnonymousCustomer() throws Exception {

        // when
        Article article = createArticle();
        given(articleFinder.search(article.getId())).willReturn(article);

        //expected
        mockMvc.perform(get("/api/v1/articles/{articleId}", article.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(handler().methodName("searchArticle"))
                .andDo(MockMvcResultHandlers.print())
                .andDo(document(
                        "article/search",
                        preprocessRequest(Preprocessors.prettyPrint()),
                        preprocessResponse(Preprocessors.prettyPrint()),
                        pathParameters(
                                parameterWithName("articleId").description("게시글 ID")),
                        responseFields(
                                fieldWithPath("data.articleId").description("게시글 ID"),
                                fieldWithPath("data.title").description("게시글 제목"),
                                fieldWithPath("data.boardType").description("ERROR,FEEDBACK,SUGGESTION,ETC"),
                                fieldWithPath("data.clientType").description("SERVICE_HUB,CUSTOMER_SERVER,HEALTH_CHECKER"),
                                fieldWithPath("data.nickname").description("게시글 작성자 닉네임"),
                                fieldWithPath("data.createdAt").description("게시글 생성일"),
                                fieldWithPath("data.content").description("게시글 내용"),
                                fieldWithPath("data.isWritten").description("게시글 작성자 여부"),
                                fieldWithPath("error").type(JsonFieldType.NULL).description("에러 필드"))));
    }


    @Test
    @DisplayName("[put] [/api/v1/articles] 게시글 등록")
    public void postArticlesTest() throws Exception {
        // when
        ArticleRegistrationRequest request = createPostRequest();
        String token = "mockToken";
        AuthenticationCustomer authenticationCustomer = createCustomer(1L);
        given(customerFetcher.bringCustomer(token)).willReturn(authenticationCustomer);
        willDoNothing().given(articleRegister).registration(createArticle());

        // expected
        mockMvc.perform(put("/api/v1/articles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-AUTH", token)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(handler().methodName("postArticles"))
                .andDo(MockMvcResultHandlers.print())
                .andDo(document(
                        "article/post",
                        preprocessRequest(Preprocessors.prettyPrint()),
                        preprocessResponse(Preprocessors.prettyPrint()),
                        requestHeaders(
                                headerWithName("X-AUTH").description("인증 헤더 이름")),
                        requestFields(
                                fieldWithPath("title").type(JsonFieldType.STRING).description("게시글 제목"),
                                fieldWithPath("content").type(JsonFieldType.STRING).description("게시글 내용"),
                                fieldWithPath("boardType").type(JsonFieldType.STRING).description("ERROR,FEEDBACK,SUGGESTION,ETC"),
                                fieldWithPath("clientType").type(JsonFieldType.STRING).description("SERVICE_HUB,CUSTOMER_SERVER,HEALTH_CHECKER")),
                        responseFields(
                                fieldWithPath("data").type(JsonFieldType.STRING).optional().description("응답 상태"),
                                fieldWithPath("error").type(JsonFieldType.NULL).description("에러 필드"))));
    }

    @Test
    @DisplayName("[PATCH] [/api/v1/articles/{articleId}] 게시글 수정")
    public void editArticleTest() throws Exception {
        // when
        Article article = createArticle();
        ArticleEditRequest request = createEditRequest();
        String token = "mockToken";
        AuthenticationCustomer authenticationCustomer = createCustomer(article.getCustomerId());
        given(customerFetcher.bringCustomer(token)).willReturn(authenticationCustomer);
        given(articleEditor.edit(request.toCondition(article.getId()), authenticationCustomer.getId())).willReturn(article);

        // expected
        mockMvc.perform(patch("/api/v1/articles/{articleId}", article.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-AUTH", token)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(handler().methodName("editArticle"))
                .andDo(MockMvcResultHandlers.print())
                .andDo(document(
                        "article/edit",
                        preprocessRequest(Preprocessors.prettyPrint()),
                        preprocessResponse(Preprocessors.prettyPrint()),
                        pathParameters(
                                parameterWithName("articleId").description("게시글 ID")),
                        requestHeaders(
                                headerWithName("X-AUTH").description("인증 헤더 이름")),
                        requestFields(
                                fieldWithPath("title").type(JsonFieldType.STRING).description("게시글 제목"),
                                fieldWithPath("content").type(JsonFieldType.STRING).description("게시글 내용")),
                        responseFields(
                                fieldWithPath("data.articleId").description("게시글 ID"),
                                fieldWithPath("data.title").description("게시글 제목"),
                                fieldWithPath("data.boardType").description("ERROR,FEEDBACK,SUGGESTION,ETC"),
                                fieldWithPath("data.clientType").description("SERVICE_HUB,CUSTOMER_SERVER,HEALTH_CHECKER"),
                                fieldWithPath("data.nickname").description("게시글 작성자 닉네임"),
                                fieldWithPath("data.createdAt").description("게시글 생성일"),
                                fieldWithPath("error").type(JsonFieldType.NULL).description("에러 필드"))));
    }

    @Test
    @DisplayName("[DELETE] [/api/v1/articles/{articleId}] 게시글 삭제")
    public void deleteArticleTest() throws Exception {
        // when
        Article article = createArticle();
        String token = "mockToken";
        AuthenticationCustomer authenticationCustomer = createCustomer(article.getCustomerId());
        given(customerFetcher.bringCustomer(token)).willReturn(authenticationCustomer);
        willDoNothing().given(articleRemover).remove(article.getId(), authenticationCustomer.getId());

        // expected
        mockMvc.perform(delete("/api/v1/articles/{articleId}", article.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-AUTH", token))
                .andExpect(status().isOk())
                .andExpect(handler().methodName("removeArticle"))
                .andDo(MockMvcResultHandlers.print())
                .andDo(document(
                        "article/remove",
                        preprocessRequest(Preprocessors.prettyPrint()),
                        preprocessResponse(Preprocessors.prettyPrint()),
                        pathParameters(
                                parameterWithName("articleId").description("게시글 ID")),
                        requestHeaders(
                                headerWithName("X-AUTH").description("인증 헤더 이름")),
                        responseFields(
                                fieldWithPath("data").type(JsonFieldType.STRING).description("응답 상태"),
                                fieldWithPath("error").type(JsonFieldType.NULL).description("에러 필드"))));
    }

    @Test
    @DisplayName("[DELETE] [/api/v1/articles/{articleId}] 게시글 삭제시 인증 오류")
    public void ErrorTest() throws Exception {
        // when
        Article article = createArticle();
        String token = "mockToken";
        given(customerFetcher.bringCustomer(token)).willReturn(null);

        // expected
        mockMvc.perform(delete("/api/v1/articles/{articleId}", article.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-AUTH", token))
                .andExpect(status().isOk())
                .andExpect(handler().methodName("removeArticle"))
                .andDo(MockMvcResultHandlers.print())
                .andDo(document(
                        "error",
                        preprocessRequest(Preprocessors.prettyPrint()),
                        preprocessResponse(Preprocessors.prettyPrint()),
                        pathParameters(
                                parameterWithName("articleId").description("게시글 ID")),
                        requestHeaders(
                                headerWithName("X-AUTH").description("인증 헤더 이름")),
                        responseFields(
                                fieldWithPath("data").type(JsonFieldType.NULL).description("응답 데이터 필드"),
                                fieldWithPath("error.status").type(JsonFieldType.NUMBER).description("에러 상태 코드"),
                                fieldWithPath("error.message").type(JsonFieldType.STRING).description("에러 메시지"))));
    }

    ArticleEditRequest createEditRequest() {
        return new ArticleEditRequest(
                "안녕하세요",
                "반가워요"
        );
    }

    ArticleRegistrationRequest createPostRequest() {
        return new ArticleRegistrationRequest(
                "안녕하세요",
                "반가워요",
                BoardType.ERROR,
                ClientType.CUSTOMER_SERVER
        );
    }

    ArticleSearchRequest createSearchRequest() {
        return new ArticleSearchRequest(
                "안녕하세요",
                BoardType.ERROR,
                ClientType.CUSTOMER_SERVER,
                "impati", LocalDate.now()
        );
    }

    List<Article> stubArticleResponse(int count) {
        List<Article> articleResponses = new ArrayList<>();
        for (int i = 0; i < count; i++) articleResponses.add(createArticle());
        return articleResponses;
    }

    Page<Article> stubArticleResponseWithPage(int count, Pageable pageable) {
        List<Article> articleResponses = stubArticleResponse(count);
        return new PageImpl<>(articleResponses, pageable, articleResponses.size());
    }

}