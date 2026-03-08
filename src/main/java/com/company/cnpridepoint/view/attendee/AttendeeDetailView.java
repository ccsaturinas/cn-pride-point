package com.company.cnpridepoint.view.attendee;

import com.company.cnpridepoint.entity.Attendee;
import com.company.cnpridepoint.entity.Section;
import com.company.cnpridepoint.entity.Status;
import com.company.cnpridepoint.entity.YearLevel;
import com.company.cnpridepoint.view.main.MainView;
import com.vaadin.flow.router.Route;
import io.jmix.core.FileRef;
import io.jmix.core.FileStorageLocator;
import io.jmix.flowui.Notifications;
import io.jmix.flowui.component.upload.FileStorageUploadField;
import io.jmix.flowui.component.upload.receiver.FileTemporaryStorageBuffer;
import io.jmix.flowui.kit.component.upload.event.FileUploadSucceededEvent;
import io.jmix.flowui.model.CollectionLoader;
import io.jmix.flowui.upload.TemporaryStorage;
import io.jmix.flowui.view.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.util.UUID;

@Route(value = "attendees/:id", layout = MainView.class)
@ViewController(id = "Attendee.detail")
@ViewDescriptor(path = "attendee-detail-view.xml")
@EditedEntityContainer("attendeeDc")
public class AttendeeDetailView extends StandardDetailView<Attendee> {
    private static final Logger log = LoggerFactory.getLogger(AttendeeDetailView.class);
    @ViewComponent
    private CollectionLoader<YearLevel> yearLevelsDl;
    @ViewComponent
    private CollectionLoader<Section> sectionsDl;
    @Autowired
    private TemporaryStorage temporaryStorage;
    @Autowired
    private Notifications notifications;
    @Autowired
    private FileStorageLocator fileStorageLocator;
    @ViewComponent
    private FileStorageUploadField profilePicField;

    @Subscribe
    public void onInit(final InitEvent event) {
        yearLevelsDl.setParameter("status", Status.ACTIVE);
        sectionsDl.setParameter("status", Status.ACTIVE);
    }

    @Subscribe("profilePicField")
    public void onProfilePicFieldFileUploadSucceeded(final FileUploadSucceededEvent<FileStorageUploadField> event) {
        if (event.getReceiver() instanceof FileTemporaryStorageBuffer buffer) {
            var fileData = buffer.getFileData();
            if (fileData == null) {
                notifications.create("File Data is empty").withType(Notifications.Type.ERROR).show();
                return;
            }
            UUID fileId = buffer.getFileData().getFileInfo().getId();
            File file = temporaryStorage.getFile(fileId);

            if (file == null) {
                log.error("File is uploaded to temporary storage not found");
                return;
            }
            var fs = fileStorageLocator.getByName("storage-profile");
            FileRef fileRef = temporaryStorage.putFileIntoStorage(fileId, event.getFileName(), fs);
            profilePicField.setValue(fileRef);
            notifications.create("Uploaded file: " + buffer.getFileData().getFileName()).show();
        }
    }


}