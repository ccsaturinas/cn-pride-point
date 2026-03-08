package com.company.cnpridepoint.view.profiledata;

import com.company.cnpridepoint.entity.ProfileData;
import com.company.cnpridepoint.view.main.MainView;
import com.vaadin.flow.router.Route;
import io.jmix.core.Copier;
import io.jmix.core.EntityStates;
import io.jmix.core.LoadContext;
import io.jmix.core.SaveContext;
import io.jmix.core.entity.EntityValues;
import io.jmix.flowui.view.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Set;

@Route(value = "profile-datas/:id", layout = MainView.class)
@ViewController(id = "ProfileData.detail")
@ViewDescriptor(path = "profile-data-detail-view.xml")
@EditedEntityContainer("profileDataDc")
public class ProfileDataDetailView extends StandardDetailView<ProfileData> {

    @Autowired
    private Copier copier;
    @Autowired
    private EntityStates entityStates;

    @Install(to = "profileDataDl", target = Target.DATA_LOADER)
    private ProfileData loadDelegate(final LoadContext<ProfileData> loadContext) {
        Object id = loadContext.getId();
        // Here you can load the entity by id from an external storage.
        // Set the loaded entity to the not-new state using EntityStates.setNew(entity, false).
        return null;
    }

    @Install(target = Target.DATA_CONTEXT)
    private Set<Object> saveDelegate(final SaveContext saveContext) {
        ProfileData entity = getEditedEntity();
        // Make a copy and save it. Copying isolates the view from possible changes of the saved entity.
        ProfileData saved = save(copier.copy(entity));
        // If the new entity ID is assigned by the storage, set the ID to the original entity instance
        // to let the framework match the saved instance with the original one.
        if (EntityValues.getId(entity) == null) {
            EntityValues.setId(entity, EntityValues.getId(saved));
        }
        // Set the returned entity to the not-new state.
        entityStates.setNew(saved, false);
        return Set.of(saved);
    }

    private ProfileData save(ProfileData entity) {
        // Here you can save the entity to an external storage and return the saved instance.
        return entity;
    }
}
