package com.company.cnpridepoint;

import com.company.cnpridepoint.entity.*;
import io.jmix.core.DataManager;
import io.jmix.core.querycondition.PropertyCondition;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(path = "/attendance")
public class AttendanceSyncController {

    private final DataManager dataManager;
    private final AttendeeRepository attendeeRepository;
    private final ProgramRepository programRepository;
    private final ActivityRepository activityRepository;

    public AttendanceSyncController(DataManager dataManager, AttendeeRepository attendeeRepository, ProgramRepository programRepository, ActivityRepository activityRepository) {
        this.dataManager = dataManager;
        this.attendeeRepository = attendeeRepository;
        this.programRepository = programRepository;
        this.activityRepository = activityRepository;
    }

//    @PostMapping("/sync")
//    public ResponseEntity<List<ActivityAttendance>> submitAttendance(@RequestBody List<ActivityAttendance> attendanceList) {
//
//        // 1) basic payload validation (prevents NPE)
//        boolean invalidRefs = attendanceList.stream().anyMatch(a ->
//                a.getProgram() == null || a.getProgram().getId() == null
//                        || a.getActivity() == null || a.getActivity().getId() == null
//                        || a.getAttendee() == null || a.getAttendee().getId() == null
//        );
//
//        if (invalidRefs) {
//            return ResponseEntity.badRequest().body("Invalid fields");
//        }
//
//        // 2) collect ids
//        Set<UUID> programIds = attendanceList.stream()
//                .map(a -> a.getProgram().getId())
//                .collect(Collectors.toSet());
//
//        Set<UUID> activityIds = attendanceList.stream()
//                .map(a -> a.getActivity().getId())
//                .collect(Collectors.toSet());
//
//        Set<UUID> attendeeIds = attendanceList.stream()
//                .map(a -> a.getAttendee().getId())
//                .collect(Collectors.toSet());
//
//        // 3) load in bulk
//        var programs = dataManager.load(Program.class).ids(programIds).list();
//        var activities = dataManager.load(Activity.class).ids(activityIds).list();
//        var attendees = dataManager.load(Attendee.class).ids(attendeeIds).list();
//
//        Set<UUID> existingProgramIds = programs.stream().map(Program::getId).collect(Collectors.toSet());
//        Set<UUID> existingActivityIds = activities.stream().map(Activity::getId).collect(Collectors.toSet());
//        Set<UUID> existingAttendeeIds = attendees.stream().map(Attendee::getId).collect(Collectors.toSet());
//
//        boolean someMissing =
//                !existingProgramIds.containsAll(programIds)
//                        || !existingActivityIds.containsAll(activityIds)
//                        || !existingAttendeeIds.containsAll(attendeeIds);
//
//        if (someMissing) {
//            return ResponseEntity.badRequest().body("Invalid fields");
//        }
//
//        return ResponseEntity
//                .status(HttpStatus.OK)
//                .header(HttpHeaders.CACHE_CONTROL, "max-age=31536000")
//                .body(attendanceList);
//
//        return ResponseEntity
//                .status(HttpStatus.OK)
//                .header(HttpHeaders.CACHE_CONTROL, "max-age=31536000")
//                .body(attendanceList);
//    }

    @PostMapping("/sync")
    public ResponseEntity<?> submitAttendance(@RequestBody List<ActivityAttendance> attendanceList) {
        List<ActivityAttendance> processedAttendanceList = new ArrayList<>();
        List<ActivityAttendance> erroredAttendanceList = new ArrayList<>();

        // 1) basic payload validation (prevents NPE)
        boolean invalidRefs = attendanceList.stream().anyMatch(a ->
                a.getProgram() == null || a.getProgram().getId() == null
                        || a.getActivity() == null || a.getActivity().getId() == null
                        || a.getAttendee() == null || a.getAttendee().getId() == null
        );

        if (invalidRefs) {
            return ResponseEntity.badRequest().body("Invalid fields");
        }
        for (ActivityAttendance activityAttendance : attendanceList) {
            var program = dataManager.load(Program.class).condition(PropertyCondition.equal("id", activityAttendance.getProgram().getId())).optional().orElse(null);
            var activity = dataManager.load(Activity.class).condition(PropertyCondition.equal("id", activityAttendance.getActivity().getId())).optional().orElse(null);
            var attendee = dataManager.load(Attendee.class).condition(PropertyCondition.equal("id", activityAttendance.getAttendee().getId())).optional().orElse(null);

            if (program == null || activity == null || attendee == null) {
                activityAttendance.setNotes("Invalid fields");
            } else {
                //TODO: Persist Attendance Sync
                if (attendee.getAttendeeType() != null) {
                    activityAttendance.setAttendeeType(attendee.getAttendeeType());
                }
            }
        }

        Map<String, Object> body = new HashMap<>();
        body.put("attendanceList", attendanceList);
        body.put("processedAttendanceList", processedAttendanceList);
        body.put("erroredAttendanceList", erroredAttendanceList);

        return ResponseEntity
                .status(HttpStatus.OK)
                .header(HttpHeaders.CACHE_CONTROL, "max-age=31536000")
                .body(body);
    }

    @GetMapping("/programs")
    public ResponseEntity<Page<Program>> getPrograms(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "50") int size,
            @RequestParam(defaultValue = "id") String sort,
            @RequestParam(defaultValue = "ASC") Sort.Direction dir
    ) {
        PageRequest pageable = PageRequest.of(page, size, Sort.by(dir, sort));
        Page<Program> result = programRepository.findByStatus(Status.ACTIVE, pageable);

        return ResponseEntity.ok(result);
    }

    @GetMapping("/activities")
    public ResponseEntity<Page<Activity>> getActivities(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "50") int size,
            @RequestParam(defaultValue = "id") String sort,
            @RequestParam(defaultValue = "ASC") Sort.Direction dir
    ) {
        PageRequest pageable = PageRequest.of(page, size, Sort.by(dir, sort));
        Page<Activity> result = activityRepository.findByStatus(Status.ACTIVE, pageable);

        return ResponseEntity.ok(result);
    }

    @GetMapping("/attendees")
    public ResponseEntity<Page<Attendee>> getAttendees(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "50") int size,
            @RequestParam(defaultValue = "id") String sort,
            @RequestParam(defaultValue = "ASC") Sort.Direction dir
    ) {
//        var skip = limit ? (page - 1) * limit : undefined;
//        var skip = (page - 1) * size;
//
//        List<Attendee> listAttendee = dataManager.load(Attendee.class)
//                .all().maxResults(size).firstResult(skip).list();

        PageRequest pageable = PageRequest.of(page, size, Sort.by(dir, sort));
        Page<Attendee> result = attendeeRepository.findAll(pageable);

        return ResponseEntity.ok(result);
    }

}


