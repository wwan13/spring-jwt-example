package com.wwan13.springjwtexample.repository;

import com.wwan13.springjwtexample.entity.Member;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    @EntityGraph(attributePaths = "authorities") // 쿼리를 수행할 때 Eager 조회로 authorities 정보를 가져옴
    Optional<Member> findOneWithAuthoritiesByUsername(String username);
}
