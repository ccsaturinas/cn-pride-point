package com.company.cnpridepoint.view.program;

import com.company.cnpridepoint.entity.Program;
import com.company.cnpridepoint.entity.Status;
import com.company.cnpridepoint.view.main.MainView;
import com.vaadin.flow.router.Route;
import io.jmix.flowui.view.*;

@Route(value = "programs/:id", layout = MainView.class)
@ViewController(id = "Program.detail")
@ViewDescriptor(path = "program-detail-view.xml")
@EditedEntityContainer("programDc")
public class ProgramDetailView extends StandardDetailView<Program> {
    @Subscribe
    public void onInitEntity(final InitEntityEvent<Program> event) {
        event.getEntity().setStatus(Status.ACTIVE);
    }
}