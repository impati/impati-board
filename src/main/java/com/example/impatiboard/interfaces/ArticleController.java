package com.example.impatiboard.interfaces;

import javax.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.impatiboard.interfaces.argument.AuthenticationCustomer;
import com.example.impatiboard.interfaces.argument.Customer;
import com.example.impatiboard.interfaces.request.ArticleEditRequest;
import com.example.impatiboard.interfaces.request.ArticleRegistrationRequest;
import com.example.impatiboard.interfaces.request.ArticleSearchRequest;
import com.example.impatiboard.interfaces.response.ArticleResponse;
import com.example.impatiboard.interfaces.response.ArticleSpecificResponse;
import com.example.impatiboard.services.article.ArticleEditor;
import com.example.impatiboard.services.article.ArticleFinder;
import com.example.impatiboard.services.article.ArticleRegister;
import com.example.impatiboard.services.article.ArticleRemover;
import com.example.impatiboard.utils.ApiResult;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/articles")
public class ArticleController {

	private final ArticleFinder articleFinder;
	private final ArticleRegister articleRegister;
	private final ArticleEditor articleEditor;
	private final ArticleRemover articleRemover;

	@PostMapping
	public ApiResult<Page<ArticleResponse>> searchArticles(
		@RequestBody final ArticleSearchRequest request,
		@PageableDefault final Pageable pageable
	) {
		final Page<ArticleResponse> response = articleFinder
			.search(request.toCondition(pageable))
			.map(ArticleResponse::from);

		return ApiResult.succeed(response);
	}

	@GetMapping("/{articleId}")
	public ApiResult<ArticleSpecificResponse> searchArticle(
		final @PathVariable Long articleId,
		final Customer customer
	) {
		final ArticleSpecificResponse response = ArticleSpecificResponse.of(articleFinder.search(articleId), customer);

		return ApiResult.succeed(response);
	}

	@PutMapping
	public ApiResult<String> postArticles(
		@Valid @RequestBody final ArticleRegistrationRequest request,
		final AuthenticationCustomer authenticationCustomer
	) {
		articleRegister.registration(
			request.toEntity(
				authenticationCustomer.getId(),
				authenticationCustomer.getNickname()
			)
		);

		return ApiResult.succeed();
	}

	@PatchMapping("/{articleId}")
	public ApiResult<ArticleResponse> editArticle(
		@PathVariable final Long articleId,
		@Valid @RequestBody final ArticleEditRequest request,
		final AuthenticationCustomer authenticationCustomer
	) {
		final ArticleResponse response = ArticleResponse.from(
			articleEditor.edit(request.toCondition(articleId), authenticationCustomer.getId())
		);

		return ApiResult.succeed(response);
	}

	@DeleteMapping("/{articleId}")
	public ApiResult<String> removeArticle(
		@PathVariable final Long articleId,
		final AuthenticationCustomer authenticationCustomer
	) {
		articleRemover.remove(articleId, authenticationCustomer.getId());

		return ApiResult.succeed();
	}
}
