package com.company.cnpridepoint.view.attendee;

import com.company.cnpridepoint.app.HelperServiceBean;
import com.company.cnpridepoint.entity.*;
import com.company.cnpridepoint.view.main.MainView;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.renderer.Renderer;
import com.vaadin.flow.router.Route;
import io.jmix.core.DataManager;
import io.jmix.flowui.Notifications;
import io.jmix.flowui.component.upload.FileUploadField;
import io.jmix.flowui.kit.component.upload.event.FileUploadSucceededEvent;
import io.jmix.flowui.view.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.ByteArrayInputStream;
import java.time.LocalDate;
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

    @Supply(to = "attendeesDataGrid.profilePic", subject = "renderer")
    private Renderer<Attendee> attendeesDataGridProfilePicRenderer() {
        return new ComponentRenderer<>(attendee -> {
            ProfileData profileData = dataManager.create(ProfileData.class);
            profileData.setName(attendee.getDisplayName());
            profileData.setProfilePic(attendee.getProfilePic());
            return helperServiceBean.getProfileComponent(this,profileData);
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

        List<YearLevel> yrLvls = dataManager.load(YearLevel.class).all().list();
        List<Section> sections = dataManager.load(Section.class).all().list();

        try (Workbook workbook = new XSSFWorkbook(new ByteArrayInputStream(fileContent))) {

            Sheet sheet = workbook.getSheetAt(0);



            for (Row row : sheet) {
                if (row.getRowNum() == 0) continue; // skip header
                for (Cell cell : row) {
//                    CellReference cellRef = new CellReference(row.getRowNum(), cell.getColumnIndex());
//                    System.out.print(cellRef.formatAsString());
//                    System.out.print(" - ");
                    // get the text that appears in the cell by getting the cell value and applying any data formats (Date, 0.00, 1.23e9, $1.23, etc)
//                    String text = formatter.formatCellValue(cell);
//                    System.out.println(text);
                    // Alternatively, get the value and format it yourself
                    var cellType = cell.getCellType();
                    switch (cellType) {
                        case STRING:
                            System.out.println(cell.getRichStringCellValue().getString());
                            break;
                        case NUMERIC:
                            if (DateUtil.isCellDateFormatted(cell)) {
                                System.out.println(cell.getDateCellValue());
                            } else {
                                System.out.println(cell.getNumericCellValue());
                            }
                            break;
                        case BOOLEAN:
                            System.out.println(cell.getBooleanCellValue());
                            break;
                        case FORMULA:
                            System.out.println(cell.getCellFormula());
                            break;
                        case BLANK:
                            System.out.println();
                            break;
                        default:
                            System.out.println();
                    }
                }



//                Number code = row.getCell(0) == null ? null :  row.getCell(0).getNumericCellValue();
//                String lastName = row.getCell(1) == null ? null :  row.getCell(1).getStringCellValue();
//                String firstName = row.getCell(2) == null ? null :  row.getCell(2).getStringCellValue();
//                String middleName = row.getCell(3) == null ? null :   row.getCell(3).getStringCellValue();
//                String birthDate =row.getCell(4) == null ? null :   row.getCell(4).getStringCellValue();
//                String gender = row.getCell(5) == null ? null :  row.getCell(5).getStringCellValue();
//                String attendeeType = row.getCell(6) == null ? null :  row.getCell(6).getStringCellValue();
//                String shirt =row.getCell(7) == null ? null :   row.getCell(7).getStringCellValue();
//                String status = row.getCell(8) == null ? null :  row.getCell(8).getStringCellValue();
//                String yearLevelName =row.getCell(9) == null ? null :   row.getCell(9).getStringCellValue();
//                String sectionName = row.getCell(10) == null ? null :  row.getCell(10).getStringCellValue();
//
//                YearLevel yrLvl = yrLvls.stream()
//                        .filter(y -> y.getName().equalsIgnoreCase(yearLevelName))
//                        .findFirst()
//                        .orElse(null);
//
//                Section section = sections.stream()
//                        .filter(y -> y.getName().equalsIgnoreCase(sectionName))
//                        .findFirst()
//                        .orElse(null);
//
//                Attendee attendee = dataManager.create(Attendee.class);
//                attendee.setCode(code == null ? null : code.toString());
//                attendee.setLastName(lastName);
//                attendee.setFirstName(firstName);
//                attendee.setMiddleName(middleName);
//                if (birthDate != null) {
//                    attendee.setBirthdate(LocalDate.parse(birthDate));
//                }
//                attendee.setGender(Gender.fromId(gender));
//                attendee.setAttendeeType(AttendeeType.fromId(attendeeType));
//                attendee.setShirtSize(ShirtSize.fromId(shirt));
//                attendee.setStatus(Status.fromId(status));
//                attendee.setYearLevel(yrLvl);
//                attendee.setSection(section);
//
//                dataManager.save(attendee);
            }

        } catch (Exception e) {
            log.error(e.getMessage(), e);
            notifications.create(e.getMessage()).withType(Notifications.Type.ERROR).show();
        }
    }




}