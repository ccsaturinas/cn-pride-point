package com.company.cnpridepoint.view.program;

import com.company.cnpridepoint.entity.Program;
import com.company.cnpridepoint.view.main.MainView;
import com.vaadin.flow.router.Route;
import io.jmix.flowui.view.EditedEntityContainer;
import io.jmix.flowui.view.StandardDetailView;
import io.jmix.flowui.view.ViewController;
import io.jmix.flowui.view.ViewDescriptor;

@Route(value = "programs/:id", layout = MainView.class)
@ViewController(id = "Program.detail")
@ViewDescriptor(path = "program-detail-view.xml")
@EditedEntityContainer("programDc")
public class ProgramDetailView extends StandardDetailView<Program> {
}