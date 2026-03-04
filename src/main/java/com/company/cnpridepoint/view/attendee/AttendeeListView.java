package com.company.cnpridepoint.view.attendee;

import com.company.cnpridepoint.entity.Attendee;
import com.company.cnpridepoint.view.main.MainView;
import com.vaadin.flow.router.Route;
import io.jmix.flowui.view.*;


@Route(value = "attendees", layout = MainView.class)
@ViewController(id = "Attendee.list")
@ViewDescriptor(path = "attendee-list-view.xml")
@LookupComponent("attendeesDataGrid")
@DialogMode(width = "64em")
public class AttendeeListView extends StandardListView<Attendee> {
}