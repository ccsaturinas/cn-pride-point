package com.company.cnpridepoint.view.activity;

import com.company.cnpridepoint.entity.Activity;
import com.company.cnpridepoint.view.main.MainView;
import com.vaadin.flow.router.Route;
import io.jmix.flowui.view.*;


@Route(value = "activities", layout = MainView.class)
@ViewController(id = "Activity.list")
@ViewDescriptor(path = "activity-list-view.xml")
@LookupComponent("activitiesDataGrid")
@DialogMode(width = "64em")
public class ActivityListView extends StandardListView<Activity> {
}