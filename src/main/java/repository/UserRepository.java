package repository;

import entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.Date;

public interface UserRepository extends PagingAndSortingRepository<User, Integer> {

    @Query("select u from User u where (lower(u.name) like :searchName or :searchName is null) and (u.age = :searchAge or :searchAge is null) " +
            " and ((u.createdDate between :searchDateFrom and :searchDateTo) or (:searchDateFrom is null or :searchDateTo is null))")
    Page<User> findByParam(@Param("searchName") String searchName,
                                              @Param("searchAge") Integer searchAge,
                                              @Param("searchDateFrom") Date dateFrom,
                                              @Param("searchDateTo") Date dateTo,
                                              Pageable pageable);

    @Query("select count(u.id) from User u where (lower(u.name) like :searchName or :searchName is null) and (u.age = :searchAge or :searchAge is null) " +
            " and ((u.createdDate between :searchDateFrom and :searchDateTo) or (:searchDateFrom is null or :searchDateTo is null))")
    int usersCountByParam(@Param("searchName") String searchName,
                           @Param("searchAge") Integer searchAge,
                           @Param("searchDateFrom") Date dateFrom,
                           @Param("searchDateTo") Date dateTo);
}
