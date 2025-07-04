package jpabook.jpashop.api;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.service.MemberService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class MemberApiController {

    private final MemberService memberService;

    // ===== 회원등록
    /*
    * 요청 값으로 Member 엔티티를 직접 받는다.
    * 문제점
    * - 엔티티에 member.name 에 @NotEmpty 같은 검증로직이 들어간다.
    * - 다양한 api를 위한 요청 요구사항을 담기 힘들다.(어떤것 not empty가 필요하고 필요없는 상황)
    * - 엔티티 변경 시 API 스펙이 변경된다.
    * ==> DTO 사용
    * */
    @PostMapping("/api/v1/members")
    public CreateMemberResponse saveMemberV1(@RequestBody @Valid Member member) {

        Long id = memberService.join(member);
        return new CreateMemberResponse(id);
    }


    // 요청 값으로 Member 엔티티 대신에 별도의 DTO를 받는다.
    @PostMapping("/api/v2/members")
    public CreateMemberResponse saveMemberV2(@RequestBody @Valid CreateMemberRequest request) {
        Member member = new Member();
        member.setName(request.getName());

        Long id = memberService.join(member);

        return new CreateMemberResponse(id);
    }

    // ===== 회원 수정
    @PutMapping("/api/v2/members/{id}")
    public UpdateMemberResponse updateMemberV2(
            @PathVariable("id") Long id,
            @RequestBody UpdateMemberRequest request) {
        memberService.update(id, request.getName());
        return new UpdateMemberResponse(id, request.getName());
    }

    // ===== 회원 조회
    /*
    * 문제점
    * - 엔티티가 직접 외부에 노출된다. ex) orders, password
    * - 응답 스펙을 맞추기 위한 로직 추가됨.
    * - - 추가로 컬렉션을 직접 반환하면 항후 API 스펙을 변경하기 어렵다.(별도의 Result 클래스 생성으로 해결)
    * */
    /*
   [{}, {}] 형태로 반환되서 count 가 추가되면 아예 구조를 바꿔야한다.
    * */
    @GetMapping("/api/v1/members")
    public List<Member> membersV1() {
        return memberService.findMembers();
    }

    @GetMapping("/api/v2/members")
    public Result memberV2() {
        List<Member> findMembers = memberService.findMembers();
        // 엔티티 -> dto
        List<MemberDto> collect = findMembers.stream()
                .map(m -> new MemberDto(m.getName()))
                .collect(Collectors.toList());

        return new Result(collect, 5);
    }

    @Data
    @AllArgsConstructor
    static class Result<T> {
        private T data;
        private int count;
    }

    @Data
    @AllArgsConstructor
    static class MemberDto {
        private String name;
    }

    @Data
    static class CreateMemberRequest {
        @NotEmpty
        private String name;
    }

    @Data
    static class CreateMemberResponse {
        private Long id;

        public CreateMemberResponse(Long id) {
            this.id = id;
        }
    }

    @Data
    static class UpdateMemberRequest {
        private String name;
    }

    @Data
    @AllArgsConstructor
    static class UpdateMemberResponse {
        private Long id;
        private String name;
    }
}
