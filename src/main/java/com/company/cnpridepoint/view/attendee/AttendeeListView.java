package com.company.cnpridepoint.view.attendee;

import com.company.cnpridepoint.app.HelperServiceBean;
import com.company.cnpridepoint.entity.Attendee;
import com.company.cnpridepoint.entity.ProfileData;
import com.company.cnpridepoint.view.main.MainView;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.renderer.Renderer;
import com.vaadin.flow.router.Route;
import io.jmix.core.DataManager;
import io.jmix.flowui.UiComponents;
import io.jmix.flowui.view.*;
import org.springframework.beans.factory.annotation.Autowired;


@Route(value = "attendees", layout = MainView.class)
@ViewController(id = "Attendee.list")
@ViewDescriptor(path = "attendee-list-view.xml")
@LookupComponent("attendeesDataGrid")
@DialogMode(width = "64em")
public class AttendeeListView extends StandardListView<Attendee> {
    @Autowired
    private UiComponents uiComponents;
    @Autowired
    private HelperServiceBean helperServiceBean;
    @Autowired
    private DataManager dataManager;

    @Supply(to = "attendeesDataGrid.profilePic", subject = "renderer")
    private Renderer<Attendee> attendeesDataGridProfilePicRenderer() {
        return new ComponentRenderer<>(attendee -> {
            // TODO: create suitable component
//            Span span = uiComponents.create(Span.class);
//            span.setText(attendee.toString());
//            return span;
            ProfileData profileData = dataManager.create(ProfileData.class);
            profileData.setName(attendee.getDisplayName());
            profileData.setProfilePic(attendee.getProfilePic());
            return helperServiceBean.getProfileComponent(this,profileData);
        });
    }


}