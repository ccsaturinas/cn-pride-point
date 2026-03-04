package com.company.cnpridepoint.view.activityattendance;

import com.company.cnpridepoint.entity.ActivityAttendance;
import com.company.cnpridepoint.view.main.MainView;
import com.vaadin.flow.router.Route;
import io.jmix.flowui.view.*;


@Route(value = "activity-attendances", layout = MainView.class)
@ViewController(id = "ActivityAttendance.list")
@ViewDescriptor(path = "activity-attendance-list-view.xml")
@LookupComponent("activityAttendancesDataGrid")
@DialogMode(width = "64em")
public class ActivityAttendanceListView extends StandardListView<ActivityAttendance> {
}