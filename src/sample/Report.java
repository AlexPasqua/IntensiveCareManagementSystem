package sample;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.TextAlignment;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;


public class Report {
    private Patient patient;
    private Date dateFrom;
    private Date dateTo;

    public Report(Patient patient, Date dateFrom, Date dateTo){
        this.patient = patient;
        this.dateFrom = dateFrom;
        this.dateTo = dateTo;
    }

    public Patient getPatient() { return patient; }

    public ArrayList<HeartBeat> getHeartBeats(){
        ArrayList<HeartBeat> heartbeats = new ArrayList<>();
        for (HeartBeat beat : patient.getHeartBeats()){
            if (beat.getTimestamp().after(dateFrom)){
                if (beat.getTimestamp().before(dateTo)) {
                    heartbeats.add(beat);
                } else break;
            }
        }
        return heartbeats;
    }

    public ArrayList<Temperature> getTemperatures(){
        ArrayList<Temperature> temperatures = new ArrayList<>();
        for (Temperature temp : patient.getTemperatures()){
            if (temp.getTimestamp().after(dateFrom)){
                if (temp.getTimestamp().before(dateTo)) {
                    temperatures.add(temp);
                } else break;
            }
        }
        return temperatures;
    }

    public ArrayList<Pressure> getPressures(){
        ArrayList<Pressure> pressures = new ArrayList<>();
        for (Pressure pressure : patient.getPressures()){
            if (pressure.getTimestamp().after(dateFrom)){
                if (pressure.getTimestamp().before(dateTo))
                    pressures.add(pressure);
                else break;
            }
        }
        return pressures;
    }

    public boolean createPDF(String path) {
        try{
            PdfDocument pdfDoc = new PdfDocument(new PdfReader(Report.class.getResourceAsStream("/template.pdf")),new PdfWriter(path));
            Document doc = new Document(pdfDoc);

            //spazio header
            doc.add(new Paragraph("\n\n\n"));
            Table firstTab = new Table(2).useAllAvailableWidth();
            addHeader(firstTab, pdfDoc);
            doc.add(firstTab);

            Paragraph titleDiagnosis = new Paragraph("Diagnosi Iniziale:\n").setBold();
            Paragraph diagnosis = new Paragraph(patient.getDiagnosis());
            doc.add(titleDiagnosis);
            doc.add(diagnosis);
            doc.add(new Paragraph("\n\n\nReport Giornaliero").setBold().setTextAlignment(TextAlignment.CENTER));

            //Creating a table
            Table table = new Table(5).useAllAvailableWidth();
            //Adding cells to the table
            table.addCell(new Cell().add(new Paragraph("Data").setBold()));
            table.addCell(new Cell().add(new Paragraph("Freq Cardiaca").setBold()));
            table.addCell(new Cell().add(new Paragraph("Temperatura").setBold()));
            table.addCell(new Cell().add(new Paragraph("Sistolica").setBold()));
            table.addCell(new Cell().add(new Paragraph("Diastolica ").setBold()));

            TreeMap<String, HeartBeat[]> hbs = getMaxMinHeartBeat();
            TreeMap<String, Temperature[]> temps = getMaxMinTemperature();
            TreeMap<String, Pressure[]> pressuresS = getMaxMinPressure(0);
            TreeMap<String, Pressure[]> pressuresD = getMaxMinPressure(1);

            for(LocalDate date : getReportDays()){
                String key = format(date);
                table.addCell(new Cell().add(new Paragraph(key).setFontSize(10)));
                if (hbs.containsKey(key))
                    table.addCell(new Cell().add(new Paragraph("MIN: " + hbs.get(key)[0].getHeartBeat() + "\nMAX: " + hbs.get(key)[1].getHeartBeat()).setFontSize(10)));
                else
                    table.addCell(new Cell().add(new Paragraph("N/A")));

                if (temps.containsKey(key))
                    table.addCell(new Cell().add(new Paragraph("MIN: " + temps.get(key)[0].getTemperature() + "\nMAX: " + temps.get(key)[1].getTemperature()).setFontSize(10)));
                else
                    table.addCell(new Cell().add(new Paragraph("N/A")));

                if (pressuresS.containsKey(key))
                    table.addCell(new Cell().add(new Paragraph("MIN: " + pressuresS.get(key)[0].formatted() + "\nMAX: " + pressuresS.get(key)[1].formatted()).setFontSize(10)));
                else
                    table.addCell(new Cell().add(new Paragraph("N/A")));

                if (pressuresD.containsKey(key))
                    table.addCell(new Cell().add(new Paragraph("MIN: " + pressuresD.get(key)[0].formatted() + "\nMAX: " + pressuresD.get(key)[1].formatted()).setFontSize(10)));
                else
                    table.addCell(new Cell().add(new Paragraph("N/A")));
            }

            //Adding Table to document
            doc.add(table);

            //Closing the document
            doc.close();
            return true;
        }
        catch (IOException e){
            System.out.println("Error creating PDF: " + e.getMessage());
            return false;
        }
    }

    private void addHeader(Table table, PdfDocument pdfDoc){
        table.addCell(new Cell().add(new Paragraph("Paziente").setBold()).setBorder(Border.NO_BORDER));
        table.addCell(new Cell().add(new Paragraph("Intervallo Date").setBold()).setBorder(Border.NO_BORDER));
        Cell cell = new Cell().add(new Paragraph(patient.getFullName() + "\n" + patient.getCodFis() + "\n" + patient.getBirthTown() + ", " + format(patient.getBirthDate()) + "\n"));
        cell.setBorder(Border.NO_BORDER);
        cell.setMaxWidth((pdfDoc.getPage(1).getPageSizeWithRotation().getWidth()/2)-30);
        table.addCell(cell);

        String date = format(dateFrom) + "-" + format(dateTo);
        Cell cell2 = new Cell().add(new Paragraph(date));
        cell2.setBorder(Border.NO_BORDER);
        cell2.setMaxWidth((pdfDoc.getPage(1).getPageSizeWithRotation().getWidth()/2)-30);
        table.addCell(cell2);
    }

    public TreeMap<String, HeartBeat[]> getMaxMinHeartBeat(){
        TreeMap<String, HeartBeat[]> maxmin= new TreeMap();
        HeartBeat[] empty = {new HeartBeat(1000),new HeartBeat(0)};

        for (HeartBeat beat: getHeartBeats()){
            maxmin.putIfAbsent(format(beat.getTimestamp()), empty);
            if (beat.getHeartBeat() < maxmin.get(format(beat.getTimestamp()))[0].getHeartBeat())
                maxmin.get(format(beat.getTimestamp()))[0] = beat;
            if (beat.getHeartBeat() > maxmin.get(format(beat.getTimestamp()))[1].getHeartBeat())
                maxmin.get(format(beat.getTimestamp()))[1] = beat;
        }
        return maxmin;
    }

    public TreeMap<String, Temperature[]> getMaxMinTemperature(){
        TreeMap<String, Temperature[]> maxmin= new TreeMap();
        Temperature[] empty = {new Temperature(1000),new Temperature(0)};

        for (Temperature temp: getTemperatures()){
            maxmin.putIfAbsent(format(temp.getTimestamp()), empty);
            if (temp.getTemperature() < maxmin.get(format(temp.getTimestamp()))[0].getTemperature())
                maxmin.get(format(temp.getTimestamp()))[0] = temp;
            if (temp.getTemperature() > maxmin.get(format(temp.getTimestamp()))[1].getTemperature())
                maxmin.get(format(temp.getTimestamp()))[1] = temp;
        }
        return maxmin;
    }

    private TreeMap<String, Pressure[]> getMaxMinPressure(int type){
        //type 0=sistolica, type 1=diastolica
        TreeMap<String, Pressure[]> maxmin= new TreeMap();
        Pressure[] empty = {new Pressure(1000, 1000),new Pressure(0, 0)};

        for (Pressure pressure: getPressures()){
            maxmin.putIfAbsent(format(pressure.getTimestamp()), empty);
            if (pressure.getPressure()[type] < maxmin.get(format(pressure.getTimestamp()))[0].getPressure()[type])
                maxmin.get(format(pressure.getTimestamp()))[0] = pressure;
            if (pressure.getPressure()[type] > maxmin.get(format(pressure.getTimestamp()))[1].getPressure()[type])
                maxmin.get(format(pressure.getTimestamp()))[1] = pressure;
        }
        return maxmin;
    }

    public TreeMap<String, ArrayList<Pressure>> getDailyPressure(){
        TreeMap<String, ArrayList<Pressure>> days = new TreeMap();

        for (Pressure pressure: getPressures()){
            days.putIfAbsent(format(pressure.getTimestamp()), new ArrayList<>());
            days.get(format(pressure.getTimestamp())).add(pressure);
        }
        return days;
    }

    private String format(Date date){
        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        String textdate = formatter.format(date);
        return textdate;
    }
    private String format(LocalDate date){
        Date parsedDate = Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant());
        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        String textdate = formatter.format(parsedDate);
        return textdate;
    }

    private LocalDate dateToLocalDate(Date date){
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    private List<LocalDate> getReportDays(){
        List<LocalDate> list =  dateToLocalDate(dateFrom).datesUntil(dateToLocalDate(dateTo)).collect(Collectors.toList());
        list.add(dateToLocalDate(dateTo));
        return list;
    }
}