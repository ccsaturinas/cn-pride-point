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

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping(path = "/attendance")
public class AttendanceSyncController {

    private final DataManager dataManager;
    private final AttendeeRepository attendeeRepository;
    private final ProgramRepository programRepository;
    private final ActivityRepository activityRepository;
    private final ActivityAttendanceRepository activityAttendanceRepository;
    private final YearLevelRepository yearLevelRepository;
    private final SectionRepository sectionRepository;

    public AttendanceSyncController(DataManager dataManager, AttendeeRepository attendeeRepository, ProgramRepository programRepository, ActivityRepository activityRepository, ActivityAttendanceRepository activityAttendanceRepository, YearLevelRepository yearLevelRepository, SectionRepository sectionRepository) {
        this.dataManager = dataManager;
        this.attendeeRepository = attendeeRepository;
        this.programRepository = programRepository;
        this.activityRepository = activityRepository;
        this.activityAttendanceRepository = activityAttendanceRepository;
        this.yearLevelRepository = yearLevelRepository;
        this.sectionRepository = sectionRepository;
    }

    @PostMapping("/sync")
    public ResponseEntity<?> submitAttendance(@RequestBody List<ActivityAttendance> attendanceList) {
//        List<ActivityAttendance> processedAttendanceList = new ArrayList<>();
//        List<ActivityAttendance> erroredAttendanceList = new ArrayList<>();

        // 1) basic payload validation (prevents NPE)
        boolean invalidRefs = attendanceList.stream().anyMatch(a ->
                a.getProgram() == null || a.getProgram().getId() == null
                        || a.getActivity() == null || a.getActivity().getId() == null
                        || a.getAttendee() == null || a.getAttendee().getId() == null
        );

        if (invalidRefs) {
            return ResponseEntity.badRequest().body("Invalid Reference fields: Null or Empty Program, Activity or Attendee");
        }

        var hasErrors = false;
        for (ActivityAttendance activityAttendance : attendanceList) {
            var program = dataManager.load(Program.class).condition(PropertyCondition.equal("id", activityAttendance.getProgram().getId())).optional().orElse(null);
            var activity = dataManager.load(Activity.class).condition(PropertyCondition.equal("id", activityAttendance.getActivity().getId())).optional().orElse(null);
            var attendee = dataManager.load(Attendee.class).condition(PropertyCondition.equal("id", activityAttendance.getAttendee().getId())).optional().orElse(null);

            if (program == null || activity == null || attendee == null) {
                hasErrors = true;
                var invalidFields = "Reference Not Found: ";
                if (program == null) {
                    invalidFields += "Program ";
                }
                if (activity == null) {
                    invalidFields += " Activity ";
                }
                if (attendee == null) {
                    invalidFields += " Attendee ";
                }
                activityAttendance.setNotes(invalidFields);
            } else {
                //TODO: Persist Attendance Sync
                activityAttendance.setProgram(program);
                activityAttendance.setActivity(activity);
                activityAttendance.setAttendee(attendee);


                /// capture attendee related data
                if (activityAttendance.getAttendeeType() == null) {
                    activityAttendance.setAttendeeType(attendee.getAttendeeType());
                }
                if (activityAttendance.getYearLevel() == null) {
                    activityAttendance.setYearLevel(attendee.getYearLevel());
                }
                if (activityAttendance.getSection() == null) {
                    activityAttendance.setSection(attendee.getSection());
                }

                if (activityAttendance.getId() == null) {
                    activityAttendance.setId(UUID.randomUUID());
                }

                try {
//                    dataManager.save(activityAttendance);
                    hasErrors = true;
                } catch (Exception e) {
                    hasErrors = true;
                    var notes = activityAttendance.getNotes() == null ? "" : activityAttendance.getNotes() + "\n";
                    activityAttendance.setNotes(notes + e.getLocalizedMessage());
                }


            }
        }

        Map<String, Object> body = new HashMap<>();
        body.put("attendanceList", attendanceList);
        body.put("errors", hasErrors);

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

    @GetMapping("/year-levels")
    public ResponseEntity<Page<YearLevel>> getYearLevels(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "50") int size,
            @RequestParam(defaultValue = "id") String sort,
            @RequestParam(defaultValue = "ASC") Sort.Direction dir
    ) {
        PageRequest pageable = PageRequest.of(page, size, Sort.by(dir, sort));
        Page<YearLevel> result = yearLevelRepository.findAll(pageable);

        return ResponseEntity.ok(result);
    }

    @GetMapping("/sections")
    public ResponseEntity<Page<Section>> getSections(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "50") int size,
            @RequestParam(defaultValue = "id") String sort,
            @RequestParam(defaultValue = "ASC") Sort.Direction dir
    ) {
        PageRequest pageable = PageRequest.of(page, size, Sort.by(dir, sort));
        Page<Section> result = sectionRepository.findAll(pageable);

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


    @GetMapping("/activity-attendance-list")
    public ResponseEntity<Page<ActivityAttendance>> getActivityAttendanceList(
            @RequestParam(required = false) UUID programId,
            @RequestParam(required = false) UUID activityId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "50") int size,
            @RequestParam(defaultValue = "id") String sort,
            @RequestParam(defaultValue = "ASC") Sort.Direction dir
    ) {
        PageRequest pageable = PageRequest.of(page, size, Sort.by(dir, sort));
//        Page<ActivityAttendance> result = activityAttendanceRepository.findAll(pageable);

        Page<ActivityAttendance> result;
        if (programId != null && activityId != null) {
            result = activityAttendanceRepository.findByProgram_IdAndActivity_Id(programId, activityId, pageable);
        } else if (programId != null) {
            result = activityAttendanceRepository.findByProgram_Id(programId, pageable);
        } else if (activityId != null) {
            result = activityAttendanceRepository.findByActivity_Id(activityId, pageable);
        } else {
            result = activityAttendanceRepository.findAll(pageable);
        }
        return ResponseEntity.ok(result);
    }

}


