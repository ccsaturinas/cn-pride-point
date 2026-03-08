package com.company.cnpridepoint.view.activity;

import com.company.cnpridepoint.entity.Activity;
import com.company.cnpridepoint.entity.Status;
import com.company.cnpridepoint.view.main.MainView;
import com.vaadin.flow.router.Route;
import io.jmix.flowui.view.*;

@Route(value = "activities/:id", layout = MainView.class)
@ViewController(id = "Activity.detail")
@ViewDescriptor(path = "activity-detail-view.xml")
@EditedEntityContainer("activityDc")
public class ActivityDetailView extends StandardDetailView<Activity> {
    @Subscribe
    public void onInitEntity(final InitEntityEvent<Activity> event) {
        event.getEntity().setStatus(Status.ACTIVE);

    }
}