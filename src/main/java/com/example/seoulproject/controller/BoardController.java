package com.example.seoulproject.controller;

import com.example.seoulproject.authentication.AuthenticatedUser;
import com.example.seoulproject.dto.request.board.CreateBoardDto;
import com.example.seoulproject.dto.request.board.UpdateBoardDto;
import com.example.seoulproject.dto.response.ResponseDto;
import com.example.seoulproject.dto.response.board.BoardData;
import com.example.seoulproject.dto.response.board.BoardListData;
import com.example.seoulproject.dto.response.board.BoardWithReactionDto;
import com.example.seoulproject.entity.User;
import com.example.seoulproject.service.BoardService;
import jakarta.annotation.Nullable;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/boards")
public class BoardController {
    private final BoardService boardService;

    // 게시글 전체 조회
    @GetMapping("/all")
    public ResponseEntity<ResponseDto<BoardListData>> getBoardList(@RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "latest") String sort) {
        BoardListData boardListData = boardService.getBoardList(page, sort);
        return new ResponseEntity<>(ResponseDto.res(HttpStatus.OK, "게시글 전체 조회 완료", boardListData), HttpStatus.OK);
    }

    // 게시글 상세 조회 (로그인 안됐을 때)
    @GetMapping("/{boardId}")
    public ResponseEntity<ResponseDto<BoardData>> getBoardById(@Parameter(hidden = true) @Nullable @AuthenticatedUser User user, @PathVariable UUID boardId) {
        BoardData boardDto;
        if (user == null) {
            boardDto = boardService.getBoardById(boardId);
        } else {
            boardDto = boardService.getBoardByIdOnLogin(user, boardId);
        }
        return new ResponseEntity<>(ResponseDto.res(HttpStatus.OK, "게시글 상세 조회 완료", boardDto), HttpStatus.OK);
    }

    // 게시글 작성
    @PostMapping
    public ResponseEntity<ResponseDto<Void>> createBoard(@Parameter(hidden = true) @AuthenticatedUser User user, @Valid @RequestBody CreateBoardDto createBoardDto) {
        boardService.createBoard(user, createBoardDto);
        return new ResponseEntity<>(ResponseDto.res(HttpStatus.CREATED, "게시글 작성 완료"), HttpStatus.CREATED);
    }

    // 게시글 수정
    @PatchMapping("/{boardId}")
    public ResponseEntity<ResponseDto<Void>> updateBoard(@Parameter(hidden = true) @AuthenticatedUser User user,
                                                         @PathVariable UUID boardId,
                                                         @Valid @RequestBody UpdateBoardDto updateBoardDto) {
        boardService.updateBoard(user, boardId, updateBoardDto);
        return new ResponseEntity<>(ResponseDto.res(HttpStatus.OK, "게시글 수정 완료"), HttpStatus.OK);
    }

    // 게시글 삭제
    @DeleteMapping("/{boardId}")
    public ResponseEntity<ResponseDto<Void>> deleteBoard(@Parameter(hidden = true) @AuthenticatedUser User user, @PathVariable UUID boardId) {
        boardService.deleteBoard(user, boardId);
        return new ResponseEntity<>(ResponseDto.res(HttpStatus.OK, "게시글 삭제 완료"), HttpStatus.OK);
    }

    // 인기글 Top3 조회
    @GetMapping("/top")
    public ResponseEntity<ResponseDto<List<BoardWithReactionDto>>> getTop3Boards() {
        List<BoardWithReactionDto> topBoards = boardService.getTop3Boards();
        return new ResponseEntity<>(ResponseDto.res(HttpStatus.OK, "Top 3 인기 게시글 조회 완료", topBoards), HttpStatus.OK);
    }

    // 좋아요 등록
    @PostMapping("/{boardId}/like")
    public ResponseEntity<ResponseDto<Void>> likeBoard(@Parameter(hidden = true) @AuthenticatedUser User user, @PathVariable UUID boardId) {
        boardService.likeBoard(user, boardId);
        return new ResponseEntity<>(ResponseDto.res(HttpStatus.CREATED, "좋아요 등록 완료"), HttpStatus.OK);
    }

    // 좋아요 취소
    @DeleteMapping("/{boardId}/like")
    public ResponseEntity<ResponseDto<Void>> unlikeBoard(@Parameter(hidden = true) @AuthenticatedUser User user, @PathVariable UUID boardId) {
        boardService.unlikeBoard(user, boardId);
        return new ResponseEntity<>(ResponseDto.res(HttpStatus.OK, "좋아요 취소 완료"), HttpStatus.OK);
    }

    // 싫어요 등록
    @PostMapping("/{boardId}/dislike")
    public ResponseEntity<ResponseDto<Void>> dislikeBoard(@Parameter(hidden = true) @AuthenticatedUser User user, @PathVariable UUID boardId) {
        boardService.dislikeBoard(user, boardId);
        return new ResponseEntity<>(ResponseDto.res(HttpStatus.CREATED, "싫어요 등록 완료"), HttpStatus.OK);
    }

    // 싫어요 취소
    @DeleteMapping("/{boardId}/dislike")
    public ResponseEntity<ResponseDto<Void>> undislikeBoard(@Parameter(hidden = true) @AuthenticatedUser User user, @PathVariable UUID boardId) {
        boardService.undislikeBoard(user, boardId);
        return new ResponseEntity<>(ResponseDto.res(HttpStatus.OK, "싫어요 취소 완료"), HttpStatus.OK);
    }
}
