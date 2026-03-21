package com.company.cnpridepoint.view.user;

import com.company.cnpridepoint.entity.User;
import com.company.cnpridepoint.security.FullViewRole;
import com.company.cnpridepoint.security.MobileUserRole;
import com.company.cnpridepoint.security.UiMinimalRole;
import com.company.cnpridepoint.view.main.MainView;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.router.Route;
import io.jmix.core.EntityStates;
import io.jmix.core.UnconstrainedDataManager;
import io.jmix.flowui.Notifications;
import io.jmix.flowui.component.textfield.TypedTextField;
import io.jmix.flowui.view.*;
import io.jmix.security.role.assignment.RoleAssignmentRoleType;
import io.jmix.securitydata.entity.RoleAssignmentEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Objects;
import java.util.TimeZone;

@Route(value = "users/:id", layout = MainView.class)
@ViewController(id = "User.detail")
@ViewDescriptor(path = "user-detail-view.xml")
@EditedEntityContainer("userDc")
public class UserDetailView extends StandardDetailView<User> {

    @ViewComponent
    private TypedTextField<String> usernameField;
    @ViewComponent
    private PasswordField passwordField;
    @ViewComponent
    private PasswordField confirmPasswordField;
    @ViewComponent
    private ComboBox<String> timeZoneField;
    @ViewComponent
    private MessageBundle messageBundle;
    @Autowired
    private Notifications notifications;

    @Autowired
    private EntityStates entityStates;
    @Autowired
    private PasswordEncoder passwordEncoder;

    private boolean newEntity;
    @Autowired
    private UnconstrainedDataManager unconstrainedDataManager;

    @Subscribe
    public void onInit(final InitEvent event) {
        timeZoneField.setItems(List.of(TimeZone.getAvailableIDs()));
    }

    @Subscribe
    public void onInitEntity(final InitEntityEvent<User> event) {
        usernameField.setReadOnly(false);
        passwordField.setVisible(true);
        confirmPasswordField.setVisible(true);
    }

    @Subscribe
    public void onReady(final ReadyEvent event) {
        if (entityStates.isNew(getEditedEntity())) {
            usernameField.focus();
        }
    }

    @Subscribe
    public void onValidation(final ValidationEvent event) {
        if (entityStates.isNew(getEditedEntity())
                && !Objects.equals(passwordField.getValue(), confirmPasswordField.getValue())) {
            event.getErrors().add(messageBundle.getMessage("passwordsDoNotMatch"));
        }
    }

    @Subscribe
    public void onBeforeSave(final BeforeSaveEvent event) {
        if (entityStates.isNew(getEditedEntity())) {
            newEntity = true;
            getEditedEntity().setPassword(passwordEncoder.encode(passwordField.getValue()));

            // Default Role : UiMinimalRole
            RoleAssignmentEntity roleAssignmentUiMinimalRole = unconstrainedDataManager.create(RoleAssignmentEntity.class);
            roleAssignmentUiMinimalRole.setUsername(getEditedEntity().getUsername());
            roleAssignmentUiMinimalRole.setRoleCode(UiMinimalRole.CODE);
            roleAssignmentUiMinimalRole.setRoleType(RoleAssignmentRoleType.RESOURCE);
            unconstrainedDataManager.save(roleAssignmentUiMinimalRole);

            // Default Role : FullViewRole
            RoleAssignmentEntity roleAssignmentFullViewRole = unconstrainedDataManager.create(RoleAssignmentEntity.class);
            roleAssignmentFullViewRole.setUsername(getEditedEntity().getUsername());
            roleAssignmentFullViewRole.setRoleCode(FullViewRole.CODE);
            roleAssignmentFullViewRole.setRoleType(RoleAssignmentRoleType.RESOURCE);
            unconstrainedDataManager.save(roleAssignmentFullViewRole);

            // Default Role : MobileUserRole
            RoleAssignmentEntity roleAssignmentMobileUserRole = unconstrainedDataManager.create(RoleAssignmentEntity.class);
            roleAssignmentMobileUserRole.setUsername(getEditedEntity().getUsername());
            roleAssignmentMobileUserRole.setRoleCode(MobileUserRole.CODE);
            roleAssignmentMobileUserRole.setRoleType(RoleAssignmentRoleType.RESOURCE);
            unconstrainedDataManager.save(roleAssignmentMobileUserRole);

        }
    }

    @Subscribe
    public void onAfterSave(final AfterSaveEvent event) {
        if (newEntity) {
            notifications.create(messageBundle.getMessage("noAssignedRolesNotification"))
                    .withThemeVariant(NotificationVariant.LUMO_WARNING)
                    .withPosition(Notification.Position.TOP_END)
                    .show();

            newEntity = false;
        }
    }
}