package com.company.cnpridepoint.view.yearlevel;

import com.company.cnpridepoint.entity.YearLevel;
import com.company.cnpridepoint.view.main.MainView;
import com.vaadin.flow.router.Route;
import io.jmix.flowui.view.EditedEntityContainer;
import io.jmix.flowui.view.StandardDetailView;
import io.jmix.flowui.view.ViewController;
import io.jmix.flowui.view.ViewDescriptor;

@Route(value = "year-levels/:id", layout = MainView.class)
@ViewController(id = "YearLevel.detail")
@ViewDescriptor(path = "year-level-detail-view.xml")
@EditedEntityContainer("yearLevelDc")
public class YearLevelDetailView extends StandardDetailView<YearLevel> {
}