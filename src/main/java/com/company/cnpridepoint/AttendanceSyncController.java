package com.company.cnpridepoint;

import com.company.cnpridepoint.entity.*;
import com.company.cnpridepoint.repository.ActivityScheduleRepository;
import io.jmix.core.DataManager;
import io.jmix.core.querycondition.PropertyCondition;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

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
    private final ActivityScheduleRepository activityScheduleRepository;

    public AttendanceSyncController(DataManager dataManager, AttendeeRepository attendeeRepository, ProgramRepository programRepository, ActivityRepository activityRepository, ActivityAttendanceRepository activityAttendanceRepository, YearLevelRepository yearLevelRepository, SectionRepository sectionRepository, ActivityScheduleRepository activityScheduleRepository) {
        this.dataManager = dataManager;
        this.attendeeRepository = attendeeRepository;
        this.programRepository = programRepository;
        this.activityRepository = activityRepository;
        this.activityAttendanceRepository = activityAttendanceRepository;
        this.yearLevelRepository = yearLevelRepository;
        this.sectionRepository = sectionRepository;
        this.activityScheduleRepository = activityScheduleRepository;
    }

    @PostMapping("/sync")
    public ResponseEntity<?> submitAttendance(@RequestBody List<ActivityAttendance> attendanceList) {
        var hasErrors = false;
        for (ActivityAttendance activityAttendance : attendanceList) {
            var notes = activityAttendance.getNotes() == null ? "" : activityAttendance.getNotes() + "\n";

            if (activityAttendance.getActivitySchedule() == null || activityAttendance.getActivitySchedule().getId() == null) {
                hasErrors = true;
                notes += "NULL Reference field: Activity Schedule\n";
            }

            if (activityAttendance.getAttendee() == null || activityAttendance.getAttendee().getId() == null) {
                hasErrors = true;
                notes += "NULL Reference field: Attendee\n";
            }

            if (hasErrors) {
                activityAttendance.setNotes(notes);
                continue;
            }


            var getActivityScheduleID = activityAttendance.getActivitySchedule().getId();
            var getAttendeeID = activityAttendance.getAttendee().getId();
            var activitySchedule = dataManager.load(ActivitySchedule.class).condition(PropertyCondition.equal("id", getActivityScheduleID)).optional().orElse(null);
            var attendee = dataManager.load(Attendee.class).condition(PropertyCondition.equal("id", getAttendeeID)).optional().orElse(null);

            if (activitySchedule == null || attendee == null) {
                hasErrors = true;
                var invalidFields = "Reference Not Found: ";
                if (activitySchedule == null) {
                    invalidFields += " Activity Schedule:  " + getActivityScheduleID;
                }
                if (attendee == null) {
                    invalidFields += " Attendee:  " + getAttendeeID;
                }
                activityAttendance.setNotes(invalidFields);
            } else {
                //TODO: Persist Attendance Sync
                activityAttendance.setActivitySchedule(activitySchedule);
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

                try {
                    activityAttendance.setId(UUID.randomUUID());
                    dataManager.save(activityAttendance);

                } catch (Exception e) {
                    activityAttendance.setId(null);
                    hasErrors = true;
                    activityAttendance.setNotes(notes + e.getLocalizedMessage());
                }


            }
        }

        Map<String, Object> body = new HashMap<>();
        List<ActivityAttendanceDto> activityAttendanceList = new ArrayList<>();
        for (ActivityAttendance activityAttendance : attendanceList) {
            activityAttendanceList.add(ActivityAttendanceDto.fromEntityActivityAttendance(activityAttendance));
        }
        body.put("attendanceList", activityAttendanceList);
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
            @RequestParam(defaultValue = "startDate") String sort,
            @RequestParam(defaultValue = "DESC") Sort.Direction dir
    ) {
        PageRequest pageable = PageRequest.of(page, size, Sort.by(dir, sort));
//        Page<Program> result = programRepository.findByStatus(Status.ACTIVE, pageable);
        Page<Program> result = programRepository.findAll(pageable);

        return ResponseEntity.ok(result);
    }

    @GetMapping("/activities")
    public ResponseEntity<Page<Activity>> getActivities(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "50") int size,
            @RequestParam(defaultValue = "startDate") String sort,
            @RequestParam(defaultValue = "ASC") Sort.Direction dir
    ) {
        PageRequest pageable = PageRequest.of(page, size, Sort.by(dir, sort));
//        Page<Activity> result = activityRepository.findByStatus(Status.ACTIVE, pageable);
        Page<Activity> result = activityRepository.findAll(pageable);

        return ResponseEntity.ok(result);
    }

    @GetMapping("/activity-schedules")
    public ResponseEntity<Page<ActivityScheduleDto>> getActivitySchedules(
            @RequestParam(required = false) UUID programId,
            @RequestParam(required = false) UUID activityId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "50") int size,
            @RequestParam(defaultValue = "startDate") String sort,
            @RequestParam(defaultValue = "ASC") Sort.Direction dir
    ) {
        PageRequest pageable = PageRequest.of(page, size, Sort.by(dir, sort));
        Page<ActivitySchedule> result;
        if (programId != null && activityId != null) {
            result = activityScheduleRepository.findByProgram_IdAndActivity_Id(programId, activityId, pageable);
        } else if (programId != null) {
            result = activityScheduleRepository.findByProgram_Id(programId, pageable);
        } else if (activityId != null) {
            result = activityScheduleRepository.findByActivity_Id(activityId, pageable);
        } else {
            result = activityScheduleRepository.findAll(pageable);
        }

        Page<ActivityScheduleDto> dtoPage = result.map(ActivityScheduleDto::fromEntityActivitySchedule);

        return ResponseEntity.ok(dtoPage);
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
    public ResponseEntity<Page<ActivityAttendanceDto>> getActivityAttendanceList(
            @RequestParam(required = false) UUID activityScheduleId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "50") int size,
            @RequestParam(defaultValue = "id") String sort,
            @RequestParam(defaultValue = "ASC") Sort.Direction dir
    ) {
        PageRequest pageable = PageRequest.of(page, size, Sort.by(dir, sort));
//        Page<ActivityAttendance> result = activityAttendanceRepository.findAll(pageable);

        Page<ActivityAttendance> result;
        if (activityScheduleId != null) {
            result = activityAttendanceRepository.findByActivitySchedule_Id(activityScheduleId, pageable);
        } else {
            result = activityAttendanceRepository.findAll(pageable);
        }

        Page<ActivityAttendanceDto> dtoPage = result.map(ActivityAttendanceDto::fromEntityActivityAttendance);
        return ResponseEntity.ok(dtoPage);
    }

}


