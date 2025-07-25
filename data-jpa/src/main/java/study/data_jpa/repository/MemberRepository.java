package study.data_jpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import study.data_jpa.dto.MemberDto;
import study.data_jpa.entity.Member;

import java.util.Collection;
import java.util.List;

public interface MemberRepository extends JpaRepository<Member, Long> {

    List<Member> findByUsernameAndAgeGreaterThan(String username, int age); // 메소드 이름으로 쿼리 생성 : 길어지면 별로

//    @Query(name = "Member.findByUsername")    // 생략 가능
    List<Member> findByUsername(@Param("username") String username);    // Named Query

    @Query("select m from Member m where m.username = :username and m.age = :age")  // @Query, 리포지토리 메소드에 쿼리 정의 : 이형식을 제일 많이 씀
    List<Member> findUser(@Param("username") String username, @Param("age") int age);

    @Query("select m.username from Member m")
    List<String> findUsernameList();

    @Query("select new study.data_jpa.dto.MemberDto(m.id, m.username, t.name) from Member m join m.team t")
    List<MemberDto> findMemberDto();

    @Query("select m from Member m where m.username in :names")
//    List<Member> findByNames(@Param("names") List<String> names);
    List<Member> findByNames(@Param("names") Collection<String> names);
}
