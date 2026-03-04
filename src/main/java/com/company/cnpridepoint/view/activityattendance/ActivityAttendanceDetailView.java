package com.company.cnpridepoint.view.activityattendance;

import com.company.cnpridepoint.entity.ActivityAttendance;
import com.company.cnpridepoint.view.main.MainView;
import com.vaadin.flow.router.Route;
import io.jmix.flowui.view.EditedEntityContainer;
import io.jmix.flowui.view.StandardDetailView;
import io.jmix.flowui.view.ViewController;
import io.jmix.flowui.view.ViewDescriptor;

@Route(value = "activity-attendances/:id", layout = MainView.class)
@ViewController(id = "ActivityAttendance.detail")
@ViewDescriptor(path = "activity-attendance-detail-view.xml")
@EditedEntityContainer("activityAttendanceDc")
public class ActivityAttendanceDetailView extends StandardDetailView<ActivityAttendance> {
}