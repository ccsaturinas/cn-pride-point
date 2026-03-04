package com.company.cnpridepoint.view.yearlevel;

import com.company.cnpridepoint.entity.YearLevel;
import com.company.cnpridepoint.view.main.MainView;
import com.vaadin.flow.router.Route;
import io.jmix.flowui.view.*;


@Route(value = "year-levels", layout = MainView.class)
@ViewController(id = "YearLevel.list")
@ViewDescriptor(path = "year-level-list-view.xml")
@LookupComponent("yearLevelsDataGrid")
@DialogMode(width = "64em")
public class YearLevelListView extends StandardListView<YearLevel> {
}