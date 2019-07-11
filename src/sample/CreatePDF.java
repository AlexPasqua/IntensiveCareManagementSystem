package sample;


import com.itextpdf.forms.PdfAcroForm;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;


class CreatePDF{
    public static void main(String args[]) throws Exception {
        //load demo patient
        Datastore.read();
        Patient p1 = Datastore.getPatients().get(0);

        //pdf
        String file = "N:/test.pdf";
        PdfDocument pdfDoc = new PdfDocument(new PdfReader(CreatePDF.class.getResourceAsStream("/template.pdf")),new PdfWriter(file));
        PdfAcroForm form = PdfAcroForm.getAcroForm(pdfDoc, true);
        Document doc = new Document(pdfDoc);


        //spazio header
        doc.add(new Paragraph("\n\n\n\n\n\n\n\n\n\n\n\n"));

        //Name
        Paragraph p = new Paragraph("Francesco Fattori");
        p.setFixedPosition(32, 688, 240);
        doc.add(p);
        //Date
        p = new Paragraph("01/01/2019-07/01/2019");
        p.setFixedPosition(270, 688, 240);
        doc.add(p);
        //diagnosis
        p = new Paragraph("Tutto bene dai");
        System.out.println(p.getHeight());
        p.setFixedPosition(32, 615, 240);
        doc.add(p);

        //Creating a table
        Table table = new Table(2).useAllAvailableWidth();
        //Adding cells to the table
        table.addCell(new Cell().add(new Paragraph("Data")));
        table.addCell(new Cell().add(new Paragraph("Data")));

        //Adding Table to document
        doc.add(table);

        //Closing the document
        doc.close();
        System.out.println("Table created successfully..");
    }
}