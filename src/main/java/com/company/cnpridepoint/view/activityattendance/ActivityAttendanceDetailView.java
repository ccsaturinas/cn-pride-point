package com.company.cnpridepoint.view.activityattendance;

import com.company.cnpridepoint.entity.*;
import com.company.cnpridepoint.view.main.MainView;
import com.vaadin.flow.router.Route;
import io.jmix.flowui.model.CollectionLoader;
import io.jmix.flowui.view.*;

@Route(value = "activity-attendances/:id", layout = MainView.class)
@ViewController(id = "ActivityAttendance.detail")
@ViewDescriptor(path = "activity-attendance-detail-view.xml")
@EditedEntityContainer("activityAttendanceDc")
public class ActivityAttendanceDetailView extends StandardDetailView<ActivityAttendance> {
    @ViewComponent
    private CollectionLoader<Program> programsDl;
    @ViewComponent
    private CollectionLoader<Activity> activitiesDl;
    @ViewComponent
    private CollectionLoader<YearLevel> yearLevelsDl;
    @ViewComponent
    private CollectionLoader<Section> sectionsDl;

    @Subscribe
    public void onInit(final InitEvent event) {
        programsDl.setParameter("status", Status.ACTIVE);
        activitiesDl.setParameter("status", Status.ACTIVE);
        yearLevelsDl.setParameter("status", Status.ACTIVE);
        sectionsDl.setParameter("status", Status.ACTIVE);
    }
}