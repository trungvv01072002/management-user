package com.example.managementuser.repositories;

import com.example.managementuser.dtos.domains.UserAndRole;
import com.example.managementuser.dtos.domains.UserAndRoleI;
import com.example.managementuser.dtos.res.UserRes;
import com.example.managementuser.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findUserByUserNameOrEmail(String userName, String email);
    boolean existsByEmailOrUserName(String email, String userName);

    Optional<User> findUserByUserName(String userName);

//    @Query("SELECT new com.example.managementuser.dtos.domains.UserAndRole(u.id, u.fullName, u.email, u.userName, r.name) FROM users u JOIN Role r ON u.id = r.userId WHERE u.userName = :userName")
//    UserAndRole findUserByUserName(@Param("userName") String userName);

    @Query("select u from users u left join fetch u.roles where u.userName = :userName")
    Optional<User> findUserByUserNameV2(@Param("userName") String userName);


    @Query(value = "SELECT u.id as id, u.user_name as userName, u.email as email, u.full_name as fullName, r.name as name FROM users u JOIN roles r ON u.id = r.user_id WHERE u.user_name = :userName", nativeQuery = true)
    UserAndRoleI findUserByUserNameV1(@Param("userName") String userName);

    @Query("SELECT u FROM users u  WHERE (:fullName is null or u.fullName like %:fullName%) Or (:email is null or u.email like %:email%) Or (:userName is null or u.userName like %:userName%)")
    Page<User> findAllUserByFilter(@Param("userName") String userName ,@Param("fullName") String fullName, @Param("email") String email, Pageable pageable);
}
