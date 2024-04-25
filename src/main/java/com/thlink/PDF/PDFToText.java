package com.thlink.PDF;

import java.io.File;
import java.io.IOException;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

public class PDFToText {
	
	private PDDocument document;

	private File input;
	
	public PDFToText() {}
	
	public PDFToText(String filePath, String password) throws IOException {
		input = new File(filePath); 
		document = PDDocument.load(input, password);
	}
	
	public String getText() throws IOException {
		
                /*PDFTextStripperByArea stripper = new PDFTextStripperByArea();
                stripper.setSortByPosition(true);
                List<String> s = stripper.getRegions();
                String sep = stripper.getWordSeparator();
                PDFTextStripper Tstripper = new PDFTextStripper();
                String result = Tstripper.getText(document);*/
                
                PDFTextStripper stripper = new PDFTextStripper();
		int lastPage = document.getNumberOfPages();
		stripper.setStartPage(1);
		stripper.setEndPage(lastPage); 
		String result = stripper.getText(document);
		if (document != null) 
                {
                    document.close();
		}
		return result;
	}

}
