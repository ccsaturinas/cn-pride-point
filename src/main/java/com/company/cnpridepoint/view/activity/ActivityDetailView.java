package com.company.cnpridepoint.view.activity;

import com.company.cnpridepoint.entity.Activity;
import com.company.cnpridepoint.view.main.MainView;
import com.vaadin.flow.router.Route;
import io.jmix.flowui.view.EditedEntityContainer;
import io.jmix.flowui.view.StandardDetailView;
import io.jmix.flowui.view.ViewController;
import io.jmix.flowui.view.ViewDescriptor;

@Route(value = "activities/:id", layout = MainView.class)
@ViewController(id = "Activity.detail")
@ViewDescriptor(path = "activity-detail-view.xml")
@EditedEntityContainer("activityDc")
public class ActivityDetailView extends StandardDetailView<Activity> {
}