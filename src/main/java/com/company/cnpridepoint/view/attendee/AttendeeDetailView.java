package com.company.cnpridepoint.view.attendee;

import com.company.cnpridepoint.entity.Attendee;
import com.company.cnpridepoint.view.main.MainView;
import com.vaadin.flow.router.Route;
import io.jmix.flowui.view.EditedEntityContainer;
import io.jmix.flowui.view.StandardDetailView;
import io.jmix.flowui.view.ViewController;
import io.jmix.flowui.view.ViewDescriptor;

@Route(value = "attendees/:id", layout = MainView.class)
@ViewController(id = "Attendee.detail")
@ViewDescriptor(path = "attendee-detail-view.xml")
@EditedEntityContainer("attendeeDc")
public class AttendeeDetailView extends StandardDetailView<Attendee> {
}