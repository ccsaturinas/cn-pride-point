package com.company.cnpridepoint.app;

import com.company.cnpridepoint.entity.ProfileData;
import com.company.cnpridepoint.view.profiledata.ProfileDataDetailView;
import io.jmix.core.FileRef;
import io.jmix.core.FileStorage;
import io.jmix.core.FileStorageException;
import io.jmix.core.FileStorageLocator;
import io.jmix.flowui.DialogWindows;
import io.jmix.flowui.UiComponents;
import io.jmix.flowui.component.image.JmixImage;
import io.jmix.flowui.view.View;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UncheckedIOException;

@Component
public class HelperServiceBean {

    private static final Logger log = LoggerFactory.getLogger(HelperServiceBean.class);
    private final FileStorageLocator fileStorageLocator;
    private final UiComponents uiComponents;
    @Autowired
    private DialogWindows dialogWindows;

    public HelperServiceBean(FileStorageLocator fileStorageLocator, UiComponents uiComponents) {
        this.fileStorageLocator = fileStorageLocator;
        this.uiComponents = uiComponents;
    }

    public Boolean fileExist(FileRef fileRef) {
        if (fileRef == null) return false;
        try {
            // Locate the appropriate file storage
            FileStorage fileStorage = fileStorageLocator.getByName(fileRef.getStorageName());

            // Check if the file exists
            return fileStorage.fileExists(fileRef);
        } catch (FileStorageException e) {
            // Handle the exception if file storage fails
            log.error("Error checking file existence: {}", e.getMessage());
            return false;
        }

    }

    public JmixImage<?> getProfileComponent(View<?> origin, ProfileData profileData) {
        if (fileExist(profileData.getProfilePic())) {
            FileRef fileRef = profileData.getProfilePic();
            var fileStorage = fileStorageLocator.getByName(fileRef.getStorageName());

            JmixImage<?> image = uiComponents.create(JmixImage.class);

            image.setSrc(downloadEvent -> {
                try (InputStream is = fileStorage.openStream(fileRef)) {
                    downloadEvent.setContentType("image/jpeg"); // or correct mime type
                    downloadEvent.setFileName(fileRef.getFileName());
                    try (OutputStream os = downloadEvent.getOutputStream()) {
                        is.transferTo(os);
                    }
                } catch (IOException e) {
                    throw new UncheckedIOException(e);
                }
            });
            image.setHeight("2em");
            image.setWidth("2em");
            image.addThemeName("cover");
            image.addSingleClickListener((imageClickEvent) -> {
                var dialog = dialogWindows.detail(origin, ProfileData.class)
                        .withViewClass(ProfileDataDetailView.class)
                        .editEntity(profileData).build();
                dialog.open();
            });
            return image;
        }

        return null;
    }
}