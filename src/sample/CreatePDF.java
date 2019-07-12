package sample;


import com.itextpdf.forms.PdfAcroForm;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;


class CreatePDF{
    public static void main(String args[]) throws Exception {
        //load demo patient
        Datastore.read();
        Patient p1 = Datastore.getPatients().get(0);

        //pdf
        String file = "C:\\Users\\Franc\\Downloads\\test\\test.pdf";
        PdfDocument pdfDoc = new PdfDocument(new PdfReader(CreatePDF.class.getResourceAsStream("/template.pdf")),new PdfWriter(file));
        Document doc = new Document(pdfDoc);


        //spazio header
        doc.add(new Paragraph("\n\n\n"));
        Table firstTab = new Table(2).useAllAvailableWidth();
        addHeader(firstTab, pdfDoc, p1, "01/01/2019-07/01/2019");
        doc.add(firstTab);

        Paragraph titleDiagnosis = new Paragraph("Diagnosi Iniziale:\n").setBold();
        Paragraph diagnosis = new Paragraph(p1.getDiagnosis());
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

    private static void addHeader(Table table, PdfDocument pdfDoc, Patient p, String date ){
        table.addCell(new Cell().add(new Paragraph("Paziente").setBold()).setBorder(Border.NO_BORDER));
        table.addCell(new Cell().add(new Paragraph("Intervallo Date").setBold()).setBorder(Border.NO_BORDER));
        Cell cell = new Cell().add(new Paragraph(p.getFullName() + "\n" + p.getCodFis() + "\n" + p.getBirthTown() + "\n"));
        cell.setBorder(Border.NO_BORDER);
        cell.setMaxWidth((pdfDoc.getPage(1).getPageSizeWithRotation().getWidth()/2)-30);
        table.addCell(cell);
        Cell cell2 = new Cell().add(new Paragraph(date));
        cell2.setBorder(Border.NO_BORDER);
        cell2.setMaxWidth((pdfDoc.getPage(1).getPageSizeWithRotation().getWidth()/2)-30);
        table.addCell(cell2);
    }
}