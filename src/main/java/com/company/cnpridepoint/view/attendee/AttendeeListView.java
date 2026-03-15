package com.company.cnpridepoint.view.attendee;

import com.company.cnpridepoint.app.HelperServiceBean;
import com.company.cnpridepoint.entity.*;
import com.company.cnpridepoint.view.main.MainView;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.renderer.Renderer;
import com.vaadin.flow.router.Route;
import io.jmix.core.DataManager;
import io.jmix.core.querycondition.PropertyCondition;
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

import javax.annotation.Nullable;
import java.io.ByteArrayInputStream;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;


@Route(value = "attendees", layout = MainView.class)
@ViewController(id = "Attendee.list")
@ViewDescriptor(path = "attendee-list-view.xml")
@LookupComponent("attendeesDataGrid")
@DialogMode(width = "64em")
public class AttendeeListView extends StandardListView<Attendee> {
    private static final Logger log = LoggerFactory.getLogger(AttendeeListView.class);
    @Autowired
    private HelperServiceBean helperServiceBean;
    @Autowired
    private DataManager dataManager;
    @Autowired
    private Notifications notifications;
    @ViewComponent
    private CollectionLoader<Attendee> attendeesDl;

    @Supply(to = "attendeesDataGrid.profilePic", subject = "renderer")
    private Renderer<Attendee> attendeesDataGridProfilePicRenderer() {
        return new ComponentRenderer<>(attendee -> {
            ProfileData profileData = dataManager.create(ProfileData.class);
            profileData.setName(attendee.getDisplayName());
            profileData.setProfilePic(attendee.getProfilePic());
            return helperServiceBean.getProfileComponent(this, profileData);
        });
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
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        List<YearLevel> yrLvls = dataManager.load(YearLevel.class).all().list();
        List<Section> sections = dataManager.load(Section.class).all().list();

        try (Workbook workbook = new XSSFWorkbook(new ByteArrayInputStream(fileContent))) {

            Sheet sheet = workbook.getSheetAt(0);

            List<String> errors = new ArrayList<>();


            for (Row row : sheet) {
                if (row.getRowNum() == 0) continue; // skip header
                Attendee attendee = dataManager.create(Attendee.class);


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
                        case STRING:
                            cellStringValue = cell.getStringCellValue();
                            break;
                        case NUMERIC:
                            if (DateUtil.isCellDateFormatted(cell)) {
                                // Convert java.util.Date to LocalDate
                                LocalDate date = cell.getDateCellValue()
                                        .toInstant()
                                        .atZone(ZoneId.systemDefault())
                                        .toLocalDate();

                                cellStringValue = date.format(dateFormatter);
                            } else {
                                DataFormatter formatter = new DataFormatter();
                                cellStringValue = formatter.formatCellValue(cell);
                            }
                            break;
                        case BOOLEAN:
                            cellStringValue = String.valueOf(cell.getBooleanCellValue());
                            break;
                        case FORMULA:
                            cellStringValue = cell.getStringCellValue();
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
                                if (cellStringValue.isEmpty()) {
                                    break rowLoop;
                                }
                                Attendee dbAttendee = findAttendee(cellStringValue);
                                if (dbAttendee == null) {
                                    attendee.setCode(cellStringValue);
                                } else {
                                    attendee = dbAttendee;
                                }
                            case 1:
                                attendee.setLastName(cellStringValue);
                                break;
                            case 2:
                                attendee.setFirstName(cellStringValue);
                                break;
                            case 3:
                                attendee.setMiddleName(cellStringValue);
                                break;
                            case 4:
                                attendee.setShirtSize(ShirtSize.fromId(cellStringValue));
                                break;
                            case 5:
                                if (!cellStringValue.isEmpty()) {
                                    attendee.setBirthdate(LocalDate.parse(cellStringValue, dateFormatter));
                                }
                                break;
                            case 6:
                                attendee.setGender(Gender.fromId(cellStringValue));
                                break;
                            case 7:
                                attendee.setAttendeeType(AttendeeType.fromId(cellStringValue));
                                break;
                            case 8:
                                attendee.setStatus(Status.fromId(cellStringValue));
                                break;
                            case 9:
                                if (!cellStringValue.isEmpty()) {
                                    String finalCellStringValue = cellStringValue;
                                    YearLevel matchedYearLevel = yrLvls.stream()
                                            .filter(yl -> yl.getName().equals(finalCellStringValue))
                                            .findFirst().orElse(null);
                                    attendee.setYearLevel(matchedYearLevel);
                                }
                                break;
                            case 10:
                                if (!cellStringValue.isEmpty()) {
                                    String finalCellStringValue = cellStringValue;
                                    Section matchedSection = sections.stream()
                                            .filter(yl -> yl.getName().equals(finalCellStringValue))
                                            .findFirst().orElse(null);
                                    attendee.setSection(matchedSection);
                                }
                                break;
                            default:
                                System.out.println();
                        }
                    } catch (Exception e) {
                        errors.add("Row " + (row.getRowNum() + 1) + ": " + cellRef.formatAsString() + 1 + ": " + e.getMessage());

                    }
                }


                if (attendee.getCode() == null) {
                    errors.add("Row " + (row.getRowNum() + 1) + ": " + "Missing code field");
                } else {
                    dataManager.save(attendee);
                    attendeesDl.load();
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

    private @Nullable Attendee findAttendee(String value) {
        return dataManager.load(Attendee.class)
                .condition(PropertyCondition.equal("code", value))
                .optional().orElse(null);
    }


}