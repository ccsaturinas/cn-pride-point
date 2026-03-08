package com.company.cnpridepoint.view.attendee;

import com.company.cnpridepoint.entity.Attendee;
import com.company.cnpridepoint.entity.Section;
import com.company.cnpridepoint.entity.Status;
import com.company.cnpridepoint.entity.YearLevel;
import com.company.cnpridepoint.view.main.MainView;
import com.vaadin.flow.router.Route;
import io.jmix.flowui.model.CollectionLoader;
import io.jmix.flowui.view.*;

@Route(value = "attendees/:id", layout = MainView.class)
@ViewController(id = "Attendee.detail")
@ViewDescriptor(path = "attendee-detail-view.xml")
@EditedEntityContainer("attendeeDc")
public class AttendeeDetailView extends StandardDetailView<Attendee> {
    @ViewComponent
    private CollectionLoader<YearLevel> yearLevelDl;
    @ViewComponent
    private CollectionLoader<Section> sectionDl;

    @Subscribe
    public void onInit(final InitEvent event) {
        yearLevelDl.setParameter("status", Status.ACTIVE);
        sectionDl.setParameter("status", Status.ACTIVE);

    }
}