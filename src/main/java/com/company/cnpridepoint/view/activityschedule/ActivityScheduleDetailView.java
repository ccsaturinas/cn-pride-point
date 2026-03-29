package com.company.cnpridepoint.view.activityschedule;

import com.company.cnpridepoint.entity.ActivitySchedule;
import com.company.cnpridepoint.repository.ActivityScheduleRepository;
import com.company.cnpridepoint.view.main.MainView;
import com.vaadin.flow.router.Route;
import io.jmix.core.FetchPlan;
import io.jmix.core.SaveContext;
import io.jmix.flowui.view.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Route(value = "activity-schedules/:id", layout = MainView.class)
@ViewController(id = "ActivitySchedule.detail")
@ViewDescriptor(path = "activity-schedule-detail-view.xml")
@EditedEntityContainer("activityScheduleDc")
public class ActivityScheduleDetailView extends StandardDetailView<ActivitySchedule> {

    @Autowired
    private ActivityScheduleRepository repository;

    @Subscribe
    public void onInit(final InitEvent event) {

    }

    @Install(to = "activityScheduleDl", target = Target.DATA_LOADER, subject = "loadFromRepositoryDelegate")
    private Optional<ActivitySchedule> loadDelegate(UUID id, FetchPlan fetchPlan) {
        return repository.findById(id, fetchPlan);
    }

    @Install(target = Target.DATA_CONTEXT)
    private Set<Object> saveDelegate(SaveContext saveContext) {
        return Set.of(repository.save(getEditedEntity()));
    }
}