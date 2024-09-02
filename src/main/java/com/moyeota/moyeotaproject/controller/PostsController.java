package com.moyeota.moyeotaproject.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.moyeota.moyeotaproject.config.exception.ApiException;
import com.moyeota.moyeotaproject.config.exception.ErrorCode;
import com.moyeota.moyeotaproject.config.response.ResponseDto;
import com.moyeota.moyeotaproject.config.response.ResponseUtil;
import com.moyeota.moyeotaproject.domain.posts.Category;
import com.moyeota.moyeotaproject.domain.posts.PostsStatus;
import com.moyeota.moyeotaproject.domain.posts.SameGender;
import com.moyeota.moyeotaproject.domain.posts.Vehicle;
import com.moyeota.moyeotaproject.dto.postsdto.MembersLocationResponseDto;
import com.moyeota.moyeotaproject.dto.postsdto.PostsResponseDto;
import com.moyeota.moyeotaproject.dto.postsdto.PostsMemberDto;
import com.moyeota.moyeotaproject.dto.postsdto.PostsSaveRequestDto;
import com.moyeota.moyeotaproject.dto.postsdto.PostsUpdateRequestDto;
import com.moyeota.moyeotaproject.service.ChatRoomService;
import com.moyeota.moyeotaproject.service.PostsService;
import com.moyeota.moyeotaproject.service.UsersService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;

@Api(tags = "Posts", description = "Post Controller")
@ApiResponses({
	@ApiResponse(code = 200, message = "API 정상 작동"),
	@ApiResponse(code = 400, message = "BAD REQUEST"),
	@ApiResponse(code = 404, message = "NOT FOUND"),
	@ApiResponse(code = 500, message = "INTERNAL SERVER ERROR")
})
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/posts")
public class PostsController {

	private final PostsService postsService;
	private final UsersService usersService;
	private final ChatRoomService chatRoomService;

	@ApiOperation(value = "모집글 작성", notes = "특정 회원이 모집글을 작성하는 API(jwt토큰 필요)")
	@PostMapping("")
	public ResponseDto<Long> save(HttpServletRequest request, @Valid @RequestBody PostsSaveRequestDto requestDto) {
		Long roomId = chatRoomService.createRoom(requestDto.getTitle(), requestDto.getRoomId());
		Long postId = postsService.save(request.getHeader("Authorization"), requestDto, roomId);
		return ResponseUtil.SUCCESS("모집글 저장에 성공하였습니다.", postId);
	}

	@ApiOperation(value = "모집글 수정", notes = "특정 회원이 모집글을 수정하는 API(jwt토큰 필요)")
	@PutMapping("/{postId}")
	public ResponseDto<Long> update(HttpServletRequest request,
		@ApiParam(value = "모집글 인덱스 번호") @PathVariable Long postId,
		@RequestBody PostsUpdateRequestDto requestDto) {
		return ResponseUtil.SUCCESS("모집글 수정에 성공하였습니다.",
			postsService.update(request.getHeader("Authorization"), postId, requestDto));
	}

	@ApiOperation(value = "모집글 삭제", notes = "특정 회원이 모집글을 삭제하는 API(jwt토큰 필요)")
	@DeleteMapping("/{postId}")
	public ResponseDto<Long> delete(HttpServletRequest request,
		@ApiParam(value = "모집글 인덱스 번호") @PathVariable Long postId) {
		postsService.delete(request.getHeader("Authorization"), postId);
		return ResponseUtil.SUCCESS("모집글 삭제에 성공하였습니다.", postId);
	}

	@ApiOperation(value = "모집글 마감", notes = "특정 회원이 모집글을 마감하는 API(jwt토큰 필요)")
	@PostMapping("/{postId}/complete")
	public ResponseDto<Long> complete(HttpServletRequest request,
		@ApiParam(value = "모집글 인덱스 번호") @PathVariable Long postId) {
		usersService.getUserByToken(request.getHeader("Authorization"));
		if (postsService.findById(postId).getStatus() == PostsStatus.COMPLETE) {
			throw new ApiException(ErrorCode.POSTS_ALREADY_FINISH);
		}
		postsService.complete(postId);
		return ResponseUtil.SUCCESS("모집글 공고가 마감되었습니다.", postId);
	}

	@ApiOperation(value = "특정 모집글 조회", notes = "특정 모집글을 조회하는 API")
	@GetMapping("/{postId}")
	public ResponseDto<PostsResponseDto> findById(
		@ApiParam(value = "모집글 인덱스 번호") @PathVariable Long postId) {
		return ResponseUtil.SUCCESS("모집글 조회에 성공하였습니다.", postsService.findById(postId));
	}

	@ApiOperation(value = "모집글 전체 조회", notes = "모집글을 최신순으로 전체 조회하는 API")
	@GetMapping("")
	public ResponseDto<List<PostsResponseDto>> findAllOrderDesc() {
		return ResponseUtil.SUCCESS("모집글 조회에 성공하였습니다.", postsService.findAllDesc());
	}

	@ApiOperation(value = "특정 회원 모집글 전체 조회", notes = "특정 회원의 모집글을 전체 조회하는 API")
	@GetMapping("/users/{userId}")
	public ResponseDto<List<PostsResponseDto>> findByIdDesc(
		@ApiParam(value = "유저 인덱스 번호") @PathVariable Long userId) {
		return ResponseUtil.SUCCESS("모집글 조회에 성공하였습니다.", postsService.findByIdDesc(userId));
	}

	@ApiOperation(value = "카테고리별 모집글 전체 조회", notes = "특정 카테고리의 모집글 전체 최신순으로 조회하는 API")
	@GetMapping("/search")
	public ResponseDto<Slice<PostsResponseDto>> findAllByCategoryDesc(@RequestParam("category") Category category,
		@ApiParam(value = "페이지 번호(0부터 시작)") @RequestParam("page") int page) {
		Pageable pageable = PageRequest.of(page, 3, Sort.by("id").descending());
		return ResponseUtil.SUCCESS("모집글 조회에 성공하였습니다.", postsService.findAllByCategory(category, pageable));
	}

	@ApiOperation(value = "모집글 파티원 목록 조회", notes = "특정 모집글 파티원 목록을 조회하는 API(본인 포함)")
	@GetMapping("/{postId}/members")
	public ResponseDto<List<PostsMemberDto>> findPostsMembers(
		@ApiParam(value = "모집글 인덱스") @PathVariable Long postId) {
		return ResponseUtil.SUCCESS("모집글 파티원 조회에 성공하였습니다.", postsService.findPostsMembers(postId));
	}

	@ApiOperation(value = "택시 이용 종료시 더치페이 금액 계산")
	@PostMapping("/calculation/{postId}")
	public ResponseDto<Long> calcPrice(@ApiParam(value = "모집글 인덱스 번호") @PathVariable Long postId) {
		return ResponseUtil.SUCCESS("더치페이 금액 계산에 성공하였습니다.", postsService.calcPrice(postId));
	}

	@ApiOperation(value = "파티원 내린 지점(이동경로 데이터 중 가장 마지막 좌표)")
	@GetMapping("/{postId}/members/location")
	public ResponseDto<List<MembersLocationResponseDto>> findMembersLocation(@ApiParam(value = "모집글 인덱스 번호") @PathVariable Long postId) {
		return ResponseUtil.SUCCESS("파티원 내린 지점 조회에 성공하였습니다.", postsService.findMembersLocation(postId));
	}

}
