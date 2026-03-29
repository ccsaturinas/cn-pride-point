package com.company.cnpridepoint.view.activityschedule;

import com.company.cnpridepoint.entity.Activity;
import com.company.cnpridepoint.entity.ActivitySchedule;
import com.company.cnpridepoint.entity.Program;
import com.company.cnpridepoint.entity.Status;
import com.company.cnpridepoint.repository.ActivityScheduleRepository;
import com.company.cnpridepoint.view.main.MainView;
import com.vaadin.flow.router.Route;
import io.jmix.core.DataManager;
import io.jmix.core.repository.JmixDataRepositoryContext;
import io.jmix.flowui.Notifications;
import io.jmix.flowui.component.upload.FileUploadField;
import io.jmix.flowui.kit.component.upload.event.FileUploadSucceededEvent;
import io.jmix.flowui.model.CollectionLoader;
import io.jmix.flowui.view.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;

import java.io.ByteArrayInputStream;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Route(value = "activity-schedules", layout = MainView.class)
@ViewController(id = "ActivitySchedule.list")
@ViewDescriptor(path = "activity-schedule-list-view.xml")
@LookupComponent("activitySchedulesDataGrid")
@DialogMode(width = "64em")
public class ActivityScheduleListView extends StandardListView<ActivitySchedule> {
    private static final Logger log = LoggerFactory.getLogger(ActivityScheduleListView.class);

    @Autowired
    private ActivityScheduleRepository repository;
    @Autowired
    private DataManager dataManager;
    @ViewComponent
    private CollectionLoader<ActivitySchedule> activitySchedulesDl;
    @Autowired
    private Notifications notifications;

    @Install(to = "activitySchedulesDl", target = Target.DATA_LOADER, subject = "loadFromRepositoryDelegate")
    private List<ActivitySchedule> loadDelegate(Pageable pageable, JmixDataRepositoryContext context) {
        return repository.findAllSlice(pageable, context).getContent();
    }

    @Install(to = "activitySchedulesDataGrid.removeAction", subject = "delegate")
    private void activitySchedulesDataGridRemoveDelegate(final Collection<ActivitySchedule> collection) {
        repository.deleteAll(collection);
    }

    @Install(to = "pagination", subject = "totalCountByRepositoryDelegate")
    private Long paginationTotalCountByRepositoryDelegate(final JmixDataRepositoryContext context) {
        return repository.count(context);
    }


    @Subscribe("importExcel")
    public void onImportExcelFileUploadSucceeded(final FileUploadSucceededEvent<FileUploadField> event) {

        // Access the uploaded file information:
//        String fileName = event.getFileName();
        byte[] fileContent = event.getSource().getValue();


        if (fileContent == null) {
            return;
        }

        // define the format
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        List<Program> programList = dataManager.load(Program.class).all().list();
        List<Activity> activityList = dataManager.load(Activity.class).all().list();

        try (Workbook workbook = new XSSFWorkbook(new ByteArrayInputStream(fileContent))) {

            Sheet sheet = workbook.getSheetAt(0);

            List<String> errors = new ArrayList<>();


            for (Row row : sheet) {
                if (row.getRowNum() == 0) continue; // skip header
                ActivitySchedule activitySchedule = dataManager.create(ActivitySchedule.class);


                rowLoop:
                // labeled loop to break
                for (Cell cell : row) {
                    CellReference cellRef = new CellReference(row.getRowNum(), cell.getColumnIndex());
//                    System.out.print(cellRef.formatAsString());
//                    System.out.print(" - ");
                    // get the text that appears in the cell by getting the cell value and applying any data formats (Date, 0.00, 1.23e9, $1.23, etc.)
//                    String text = formatter.formatCellValue(cell);
//                    System.out.println(text);
                    // Alternatively, get the value and format it yourself
                    var cellType = cell.getCellType();
                    var cellStringValue = "";
                    switch (cellType) {
                        case STRING, FORMULA:
                            cellStringValue = cell.getStringCellValue();
                            break;
                        case NUMERIC:
                            if (DateUtil.isCellDateFormatted(cell)) {
                                // Convert java.util.Date to LocalDate
                                LocalDateTime date = cell.getDateCellValue()
                                        .toInstant()
                                        .atZone(ZoneId.systemDefault())
                                        .toLocalDateTime();

                                cellStringValue = date.format(dateTimeFormatter);
                            } else {
                                DataFormatter formatter = new DataFormatter();
                                cellStringValue = formatter.formatCellValue(cell);
                            }
                            break;
                        case BOOLEAN:
                            cellStringValue = String.valueOf(cell.getBooleanCellValue());
                            break;
                        case BLANK:
                            break;
                        default:
                            System.out.println();
                    }


                    try {
                        var columnIndex = cell.getColumnIndex();
                        switch (columnIndex) {
                            case 0:
                                if (!cellStringValue.isEmpty()) {
                                    String finalCellStringValue = cellStringValue;
                                    Program matchedProgram = programList.stream()
                                            .filter(entry -> entry.getName().equals(finalCellStringValue))
                                            .findFirst().orElse(null);
                                    activitySchedule.setProgram(matchedProgram);
                                }
                                break;
                            case 1:
                                if (!cellStringValue.isEmpty()) {
                                    String finalCellStringValue = cellStringValue;
                                    Activity matchedActivity = activityList.stream()
                                            .filter(entry -> entry.getName().equals(finalCellStringValue))
                                            .findFirst().orElse(null);
                                    activitySchedule.setActivity(matchedActivity);
                                }
                                break;
                            case 2:
                                if (!cellStringValue.isEmpty()) {
                                    activitySchedule.setStartDate(LocalDateTime.parse(cellStringValue, dateTimeFormatter));
                                }
                                break;
                            case 3:
                                if (!cellStringValue.isEmpty()) {
                                    activitySchedule.setEndDate(LocalDateTime.parse(cellStringValue, dateTimeFormatter));
                                }
                                break;
                            case 4:
                                if (!cellStringValue.isEmpty()) {
                                    activitySchedule.setStatus(Status.fromId(cellStringValue));
                                }
                                break;
                            case 5:
                                if (!cellStringValue.isEmpty()) {
                                    activitySchedule.setNotes(cellStringValue);
                                }
                                break;
                            default:
                                System.out.println();
                        }
                    } catch (Exception e) {
                        errors.add("Row " + (row.getRowNum() + 1) + ": " + cellRef.formatAsString() + 1 + ": " + e.getMessage());

                    }
                }


                if (activitySchedule.getProgram() == null || activitySchedule.getActivity() == null) {
                    errors.add("Row " + (row.getRowNum() + 1) + ": " + "Missing Program or Activity field");
                } else {
                    dataManager.save(activitySchedule);
                    activitySchedulesDl.load();
                }
            }

            if (!errors.isEmpty()) {
                for (String error : errors) {
                    notifications.create(error).withType(Notifications.Type.ERROR).show();
                }
            }

        } catch (Exception e) {
            log.error(e.getMessage(), e);
            notifications.create(e.getMessage()).withType(Notifications.Type.ERROR).show();
        }
        notifications.create("Update Completed").withType(Notifications.Type.SUCCESS).show();
    }
}