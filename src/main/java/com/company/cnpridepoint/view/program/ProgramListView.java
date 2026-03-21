package com.company.cnpridepoint.view.program;

import com.company.cnpridepoint.entity.Program;
import com.company.cnpridepoint.view.main.MainView;
import com.vaadin.flow.router.Route;
import io.jmix.flowui.view.*;


@Route(value = "programs", layout = MainView.class)
@ViewController(id = "Program.list")
@ViewDescriptor(path = "program-list-view.xml")
@LookupComponent("programsDataGrid")
@DialogMode(width = "64em")
public class ProgramListView extends StandardListView<Program> {
}