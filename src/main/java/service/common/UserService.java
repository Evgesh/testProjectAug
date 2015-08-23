package service.common;


import entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Date;
import java.util.List;

public interface UserService {
    List<User> getUsers();
    List<User> getUsersByParam(String searchName, Integer searchAge, Date searchDateTo, Date searchDateFrom, int page, int pageSize);
    int getUsersCountByParam(String searchName, Integer searchAge, Date searchDateTo, Date searchDateFrom);
    User getUserById(Integer id);
    User mergeUser(User user);
    void deleteUser(Integer id);
    Page<User> findAllByPage(Pageable pageable);
}
