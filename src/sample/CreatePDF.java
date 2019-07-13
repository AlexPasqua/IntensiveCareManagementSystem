package sample;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class CreatePDF{

    private Patient patient;
    private Date dateFrom;
    private Date dateTo;

    public CreatePDF(Patient patient, Date dateFrom, Date dateTo){
        this.patient = patient;
        this.dateFrom = dateFrom;
        this.dateTo = dateTo;
    }

    public void createPDF(String path) throws Exception {
        //pdf
        PdfDocument pdfDoc = new PdfDocument(new PdfReader(CreatePDF.class.getResourceAsStream("/template.pdf")),new PdfWriter(path));
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
        doc.add(new Paragraph("\n\n\n\n\n\n\n"));


        //Creating a table
        Table table = new Table(2).useAllAvailableWidth();
        //Adding cells to the table



        //Adding Table to document
        doc.add(table);

        //Closing the document
        doc.close();
        System.out.println("Table created successfully..");
    }

    private void addHeader(Table table, PdfDocument pdfDoc){
        table.addCell(new Cell().add(new Paragraph("Paziente").setBold()).setBorder(Border.NO_BORDER));
        table.addCell(new Cell().add(new Paragraph("Intervallo Date").setBold()).setBorder(Border.NO_BORDER));
        Cell cell = new Cell().add(new Paragraph(patient.getFullName() + "\n" + patient.getCodFis() + "\n" + patient.getBirthTown() + "\n"));
        cell.setBorder(Border.NO_BORDER);
        cell.setMaxWidth((pdfDoc.getPage(1).getPageSizeWithRotation().getWidth()/2)-30);
        table.addCell(cell);

        String date = format(dateFrom) + "-" + format(dateTo);
        Cell cell2 = new Cell().add(new Paragraph(date));
        cell2.setBorder(Border.NO_BORDER);
        cell2.setMaxWidth((pdfDoc.getPage(1).getPageSizeWithRotation().getWidth()/2)-30);
        table.addCell(cell2);
    }

    private int[] getMaxMinHeartBeat(ArrayList<HeartBeat> beats){
        int[] toreturn = {1000,0};
        for (HeartBeat beat: beats){
            if (beat.getHeartBeat() < toreturn[0])
                toreturn[0] = beat.getHeartBeat();
            if (beat.getHeartBeat() > toreturn[1])
                toreturn[1] = beat.getHeartBeat();
        }
        return toreturn;
    }

    private String format(Date date){
        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        String textdate = formatter.format(date);
        return textdate;
    }
}