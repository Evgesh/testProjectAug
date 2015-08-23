package controller;


import entity.User;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Execution;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zkplus.databind.AnnotateDataBinder;
import org.zkoss.zkplus.spring.SpringUtil;
import org.zkoss.zul.Button;
import org.zkoss.zul.Window;
import service.common.UserService;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("rawtypes")
public class UserEditorController extends GenericForwardComposer {
    private static final long serialVersionUID = 90813416671L;

    @WireVariable
    private UserService userService;

    private User selectedUser;
    private String recordMode;
    private AnnotateDataBinder binder;
    protected Window userEditor;
    protected Button save;
    protected Button cancel;
    private Window parentWindow;

    @SuppressWarnings("unchecked")
    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        userService = (UserService) SpringUtil.getBean("userService");
        this.self.setAttribute("controller", this, false);

        final Execution execution = Executions.getCurrent();
        setSelectedUser((User) execution.getArg().get("selectedRecord"));
        setRecordMode((String) execution.getArg().get("recordMode"));
        setParentWindow((Window) execution.getArg().get("parentWindow"));
        if (recordMode.equals("NEW")) {
            this.selectedUser = new User();
            this.selectedUser.setCreatedDate(new Date());
        }
    }

    public void onCreate(Event event) {
        this.binder = (AnnotateDataBinder) event.getTarget().getAttribute("binder", true);
    }

    public void onClick$save(Event event) {
        Map<String, Object> args = new HashMap<String, Object>();
        binder.saveAll();
        userService.mergeUser(this.selectedUser);
        userEditor.detach();
        args.put("selectedRecord", getSelectedUser());
        args.put("recordMode", this.recordMode);
        Events.sendEvent(new Event("onSaved", parentWindow, args));
    }

    public void onClick$cancel(Event event) {
        userEditor.detach();
    }

    public User getSelectedUser() {
        return selectedUser;
    }

    public void setSelectedUser(User selectedUser) {
        this.selectedUser = selectedUser;
    }

    public String getRecordMode() {
        return recordMode;
    }

    public void setRecordMode(String recordMode) {
        this.recordMode = recordMode;
    }

    public Window getParentWindow() {
        return parentWindow;
    }

    public void setParentWindow(Window parentWindow) {
        this.parentWindow = parentWindow;
    }

    public AnnotateDataBinder getBinder() {
        return binder;
    }

    public void setBinder(AnnotateDataBinder binder) {
        this.binder = binder;
    }
}
