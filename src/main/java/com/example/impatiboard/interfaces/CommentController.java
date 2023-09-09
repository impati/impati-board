package com.example.impatiboard.interfaces;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.impatiboard.interfaces.argument.AuthenticationCustomer;
import com.example.impatiboard.interfaces.argument.Customer;
import com.example.impatiboard.interfaces.request.CommentEditRequest;
import com.example.impatiboard.interfaces.request.CommentRegistrationRequest;
import com.example.impatiboard.interfaces.response.CommentResponse;
import com.example.impatiboard.services.comment.CommentManager;
import com.example.impatiboard.services.comment.dto.CommentResult;
import com.example.impatiboard.utils.ApiResult;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/comments")
public class CommentController {

	private final CommentManager commentManager;

	@GetMapping("/{articleId}")
	public ApiResult<List<CommentResponse>> searchCommentInArticle(
		@PathVariable final Long articleId,
		final Customer customer
	) {
		return ApiResult.succeed(commentManager.search(articleId, getCustomerId(customer))
			.stream()
			.map(CommentResponse::from)
			.toList());
	}

	@PostMapping("/{articleId}")
	public ApiResult<String> registerComment(
		@PathVariable final Long articleId,
		@RequestBody final CommentRegistrationRequest request,
		final AuthenticationCustomer customer
	) {
		commentManager.register(articleId, customer.getId(), request.content());

		return ApiResult.succeed();
	}

	@PutMapping("/comment/{commentId}")
	public ApiResult<CommentResponse> editComment(
		@PathVariable final Long commentId,
		@RequestBody final CommentEditRequest request,
		final AuthenticationCustomer customer
	) {
		final CommentResult response = commentManager.edit(commentId, customer.getId(), request.content());

		return ApiResult.succeed(CommentResponse.from(response));
	}

	@DeleteMapping("/comment/{commentId}")
	public ApiResult<String> deleteComment(
		@PathVariable final Long commentId,
		final AuthenticationCustomer customer
	) {
		commentManager.remove(commentId, customer.getId());

		return ApiResult.succeed();
	}

	private Long getCustomerId(final Customer customer) {
		if (customer.isAuthenticated()) {
			return ((AuthenticationCustomer)customer).getId();
		}

		return null;
	}
}
