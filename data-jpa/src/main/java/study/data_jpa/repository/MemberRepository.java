package study.data_jpa.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import study.data_jpa.dto.MemberDto;
import study.data_jpa.entity.Member;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

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

    // 반환 타입
    List<Member> findListByUsername(String username);   // 컬렉션
    Member findMemberByUsername(String username);   // 단건
    Optional<Member> findOptionalByUsername(String username);   // 단건 Optional

    @Query(value = "select m from Member m",
          countQuery = "select count(m.username) from Member m")    //  count 쿼리를 다음과 같이 분리할 수 있음 left 조인같은거하면 count 할때 굳이 join 이필요없다 . 그런거 최적화할때 사용
    Page<Member> findByAge(int age, Pageable pageable);

    @Modifying(clearAutomatically = true)   // em.flush , clear 영속성 콘텍스트 초기화
    @Query("update Member m set m.age = m.age +1 where m.age >= :age")
    int bulkAgePlus(@Param("age") int age);
}
