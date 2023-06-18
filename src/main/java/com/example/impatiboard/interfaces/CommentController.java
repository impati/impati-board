package com.example.impatiboard.interfaces;

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
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/comments")
public class CommentController {

    private final CommentManager commentManager;

    @GetMapping("/{articleId}")
    public ApiResult<List<CommentResponse>> searchCommentInArticle(@PathVariable Long articleId,
                                                                   Customer customer) {
        return ApiResult.succeed(commentManager.search(articleId, getCustomerId(customer))
                .stream()
                .map(CommentResponse::from)
                .toList());
    }

    @PostMapping("/{articleId}")
    public ApiResult<String> registerComment(@PathVariable Long articleId,
                                             @RequestBody CommentRegistrationRequest request,
                                             AuthenticationCustomer customer) {
        commentManager.register(articleId, customer.getId(), request.content());
        return ApiResult.succeed();
    }

    @PutMapping("/comment/{commentId}")
    public ApiResult<CommentResponse> editComment(@PathVariable Long commentId,
                                                  @RequestBody CommentEditRequest request,
                                                  AuthenticationCustomer customer) {
        CommentResult response = commentManager.edit(commentId, customer.getId(), request.content());
        return ApiResult.succeed(CommentResponse.from(response));
    }

    @DeleteMapping("/comment/{commentId}")
    public ApiResult<String> deleteComment(@PathVariable Long commentId,
                                           AuthenticationCustomer customer) {
        commentManager.remove(commentId, customer.getId());
        return ApiResult.succeed();
    }

    private Long getCustomerId(Customer customer) {
        if (customer.isAuthenticated()) return ((AuthenticationCustomer) customer).getId();
        return null;
    }


}
