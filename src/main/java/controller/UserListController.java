package controller;


import entity.User;
import org.apache.commons.lang3.time.DateUtils;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zkplus.databind.AnnotateDataBinder;
import org.zkoss.zkplus.databind.BindingListModelList;
import org.zkoss.zkplus.spring.SpringUtil;
import org.zkoss.zul.*;
import org.zkoss.zul.event.PagingEvent;
import service.common.UserService;

import java.util.*;
import java.util.Calendar;

@SuppressWarnings("rawtypes")
public class UserListController extends GenericForwardComposer {

    private static final long serialVersionUID = 908134130931L;
    private static final int PAGE_SIZE = 15;

    @WireVariable
    private UserService userService;

    protected Listbox userListBox;
    protected Window usersList;

    private AnnotateDataBinder binder;
    private BindingListModelList<User> userslist;
    private User selectedUser;
    private String searchName;
    private Integer searchAge;
    private Date searchDateFrom;
    private Date searchDateTo;
    private int totalSize = 1;
    private int activePage = 0;

    public UserListController() {
        super();
    }

    public UserService getUserService() {
        if (userService == null) {
            userService = (UserService) SpringUtil.getBean("UserService");
        }
        return userService;
    }

    public int getTotalSize() {
        Date dateTo = DateUtils.addMilliseconds(DateUtils.addDays(getSearchDateTo(), 1), -1);
        totalSize = getUserService().getUsersCountByParam(getSearchName(), getSearchAge(), getSearchDateFrom(), dateTo);
        return totalSize;
    }

    public void setTotalSize(int totalSize) {
        this.totalSize = totalSize;
    }

    public int getActivePage() {
        return activePage;
    }

    public void setActivePage(int activePage) {
        this.activePage = activePage;
    }

    public int getPageSize() {
        return PAGE_SIZE;
    }

    public Date getSearchDateFrom() {
        if (searchDateFrom == null) {
            searchDateFrom = new Date();
        }
        return DateUtils.truncate(searchDateFrom, Calendar.DAY_OF_MONTH);
    }

    public void setSearchDateFrom(Date searchDateFrom) {
        this.searchDateFrom = searchDateFrom;
    }

    public Date getSearchDateTo() {
        if (searchDateTo == null) {
            searchDateTo = new Date();
        }
        return DateUtils.truncate(searchDateTo, Calendar.DAY_OF_MONTH);
    }

    public void setSearchDateTo(Date searchDateTo) {
        this.searchDateTo = searchDateTo;
    }

    public String getSearchName() {
        return searchName;
    }

    public void setSearchName(String searchName) {
        this.searchName = searchName;
    }

    public Integer getSearchAge() {
        return searchAge;
    }

    public void setSearchAge(Integer searchAge) {
        this.searchAge = searchAge;
    }

    public BindingListModelList<User> getUserslist() {
        return userslist;
    }

    public void setUserslist(BindingListModelList<User> userslist) {
        this.userslist = userslist;
    }

    public User getSelectedUser() {
        return selectedUser;
    }

    public void setSelectedUser(User selectedUser) {
        this.selectedUser = selectedUser;
    }

    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        this.self.setAttribute("controller", this, false);
    }

    public void onCreate$usersList(Event event) throws Exception {
        this.binder = (AnnotateDataBinder) event.getTarget().getAttribute("binder", true);
        setSelectedUser(null);
        updateListbox();
        this.binder.loadAll();
    }

    public void updateListbox() {
        Date dateTo = DateUtils.addMilliseconds(DateUtils.addDays(getSearchDateTo(), 1), -1);
        List<User> users = getUserService().getUsersByParam(getSearchName(), getSearchAge(), getSearchDateFrom(), dateTo, getActivePage(), PAGE_SIZE);
        userslist = new BindingListModelList<User>(users, true);
        setUserslist(userslist);
        this.userListBox.setModel(userslist);
    }

    @SuppressWarnings("unchecked")
    public void onSaved(Event event) {
        updateListbox();
    }

    public void onClick$btnNew(Event event) {
        final Map<String, Object> map = new HashMap<String, Object>();
        map.put("selectedRecord", null);
        map.put("recordMode", "NEW");
        map.put("parentWindow", usersList);
        Executions.createComponents("userEditor.zul", null, map);
    }

    public void onClick$btnSearch(Event event) {
        this.binder = (AnnotateDataBinder) event.getTarget().getAttribute("binder", true);
        setSelectedUser(null);
        updateListbox();
        this.binder.loadAll();
    }

    public void onEdit$userListBox(ForwardEvent evt) {
        Event origin = Events.getRealOrigin(evt);
        Button btn = (Button) origin.getTarget();
        Listitem litem = (Listitem) btn.getParent().getParent();
        User curUser = (User) litem.getValue();
        setSelectedUser(curUser);
        final HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("selectedRecord", curUser);
        map.put("recordMode", "EDIT");
        map.put("parentWindow", usersList);
        Executions.createComponents("userEditor.zul", null, map);
    }

    public void onDelete$userListBox(ForwardEvent evt) {
        Event origin = Events.getRealOrigin(evt);
        Button btn = (Button) origin.getTarget();
        Listitem litem = (Listitem) btn.getParent().getParent();
        User curUser = (User) litem.getValue();
        setSelectedUser(curUser);
        Messagebox.show("Вы действительно хотите удалить запись пользователя?", "Подтвердите действие",
                Messagebox.OK | Messagebox.CANCEL, Messagebox.QUESTION,
                new org.zkoss.zk.ui.event.EventListener() {
                    public void onEvent(Event e) {
                        if (Messagebox.ON_OK.equals(e.getName())) {
                            getUserService().deleteUser(selectedUser.getId());
                            updateListbox();
                        }
                    }
                });
    }

    public void onPaging$userPaging(ForwardEvent evt) {
        PagingEvent event = (PagingEvent)evt.getOrigin();
        setActivePage(event.getActivePage());
        updateListbox();
    }
}
