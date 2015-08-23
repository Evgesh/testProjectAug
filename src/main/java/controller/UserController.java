package controller;

import entity.User;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import service.common.UserService;

import java.util.List;

@Controller
@RequestMapping("/users")
public class UserController {
    private static final Logger LOGGER = Logger.getLogger(UserController.class);

    @Qualifier("UserService")
    @Autowired
    private UserService userService;

    @RequestMapping(value = "/users", method = RequestMethod.GET)
    public String getUsers(Model model) {
        LOGGER.debug("getUsers() started");
        List<User> users = userService.getUsers();
        model.addAttribute("users", users);
        LOGGER.debug("getUsers() done");
        return "users/list";
    }

    @RequestMapping(value = "/users/add", method = RequestMethod.GET)
    public String getAdd(Model model) {
        LOGGER.debug("getAdd() started");
        model.addAttribute("userData", new User());
        LOGGER.debug("getAdd() done");
        return "addPage";
    }

    @RequestMapping(value = "/users/add", method = RequestMethod.POST)
    public String add(@ModelAttribute("userData") User user) {
        LOGGER.debug("getAdd() started");
        userService.mergeUser(user);
        LOGGER.debug("getAdd() done");
        return "addedPage";
    }

    @RequestMapping(value = "/users/delete", method = RequestMethod.GET)
    public String delete(@RequestParam(value = "id", required = true) Integer id, Model model) {
        LOGGER.debug("delete() started");
        userService.deleteUser(id);
        model.addAttribute("id", id);
        LOGGER.debug("delete() done");
        return "deletedPage";
    }

    @RequestMapping(value = "/users/edit", method = RequestMethod.GET)
    public String getEdit(@RequestParam(value = "id", required = true) Integer id, Model model) {
        LOGGER.debug("getEdit() started");
        model.addAttribute("userData", userService.getUserById(id));
        LOGGER.debug("getEdit() done");
        return "editPage";
    }

//    @RequestMapping(value = "/users/edit", method = RequestMethod.POST)
//    public String saveEdit(@ModelAttribute("userData") User user,
//                           @RequestParam(value = "id", required = true) Integer id,
//                           Model model) {
//        LOGGER.debug("saveEdit() started");
//        user.setId(id);
//        userService.editUser(user);
//        model.addAttribute("id", id);
//        LOGGER.debug("saveEdit() done");
//        return "editedPage";
//    }
}
