package com.company.cnpridepoint.view.section;

import com.company.cnpridepoint.entity.Section;
import com.company.cnpridepoint.view.main.MainView;
import com.vaadin.flow.router.Route;
import io.jmix.flowui.view.*;


@Route(value = "sections", layout = MainView.class)
@ViewController(id = "Section_.list")
@ViewDescriptor(path = "section-list-view.xml")
@LookupComponent("sectionsDataGrid")
@DialogMode(width = "64em")
public class SectionListView extends StandardListView<Section> {
}