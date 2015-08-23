package service;


import entity.User;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import repository.UserRepository;
import service.common.UserService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

@Service("UserService")
@Repository
@Transactional
public class UserServiceImpl implements UserService {
    private static final Logger LOGGER = Logger.getLogger(UserServiceImpl.class);

    @Autowired
    private UserRepository userRepository;

    @Transactional(readOnly = true)
    public List<User> getUsers() {
        LOGGER.debug("getUsers() started");
        return new ArrayList<User>((Collection<? extends User>) userRepository.findAll());
    }

    public List<User> getUsers(String searchName, Integer searchAge, Date searchDate) {
        return null;
    }

    @Transactional(readOnly = true)
    public User getUserById(Integer id) {
        LOGGER.debug("getUserById() started");
        return userRepository.findOne(id);
    }

    @Transactional
    public User mergeUser(User user) {
        LOGGER.debug("mergeUser() started");
        return userRepository.save(user);
    }

    @Transactional
    public void deleteUser(Integer id) {
        LOGGER.debug("deleteUser() started");
        userRepository.delete(id);
        LOGGER.debug("deleteUser() done");
    }

    @Transactional(readOnly = true)
    public Page<User> findAllByPage(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    @Transactional(readOnly = true)
    public List<User> getUsersByParam(String searchName, Integer searchAge, Date searchDateFrom, Date searchDateTo, int page, int pageSize) {
        Pageable pageable = new PageRequest(page, pageSize);
        String template = null;
        if (searchName != null) {
            template = "%" + searchName + "%";
        }
        Page<User> result = userRepository.findByParam(template, searchAge, searchDateFrom, searchDateTo, pageable);
        return result.getContent();
    }

    public int getUsersCountByParam(String searchName, Integer searchAge, Date searchDateTo, Date searchDateFrom) {
        String template = null;
        if (searchName != null) {
            template = "%" + searchName + "%";
        }
        return userRepository.usersCountByParam(template, searchAge, searchDateTo, searchDateFrom);
    }
}
