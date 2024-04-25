package com.thlink.statement;

import com.thlink.NNPdf.NNPers;
import com.thlink.NNPdf.Util;
import com.thlink.PDF.PDFToText;
import com.thlink.statement.entities.Extrato;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class StatementHelper
{
    public String importStatementPDF (String pFile)
    {
        String statement = readStatementPDF(pFile);
        //System.out.println(statement);
        String[] lines = statement.split("\n");
        boolean isStarted = false;
        for (String line : lines)
        {
            if (line.startsWith("Liq Mov Histórico Valor Saldo"))
            {
                isStarted = true;
                continue;
            }
            else if (line.startsWith("Lançamentos futuros"))
            {
                isStarted = false;
                continue;
            }
            if (isStarted)
            {
                System.out.printf("L: " + line); 
                System.out.println(line.length());
                if (line.length() < 10)
                    continue;
                String[] fields = new String[10];
                fields[0] = line.substring(0, 10);
                fields[1] = line.substring(11, 20);
                fields[2] = line.substring(21);
                
                int posLabel1 = line.indexOf("R$");
                if (posLabel1 > 0)
                {
                    fields[2] = line.substring(0, posLabel1);
                    String temp = line.substring(posLabel1).replace("R$", "").replace("\r", "").trim();
                    String[] values = temp.split(" ");
                    Extrato log = new Extrato(fields[0], fields[1], fields[2], values[0], values[2]);

                    System.out.println(log.toString());
                
                }
                
            }
        }
        return "";
    }
    
    public String readStatementPDF (String pFile)
    {
        try
        {
            PDFToText pdf = new PDFToText(pFile, "668");
            if (pdf != null)
                return pdf.getText();
                
        } catch (Exception e)        
        {
            System.out.println("importStatementPDF ERROR: " + e.toString());
        }
        return null;
    }
    public List<Extrato> readStatementFromXLSFolder (String pPasta)
    {
        File dir = new File(pPasta);
        Collection<File> arquivos = new Util().listFileTree(dir, "xlsx");
        
        List<Extrato> extrato = new ArrayList<>();
        
        for (File arquivo : arquivos)
        {
            List<Extrato> local = readStatementFromXLS(arquivo);
            extrato.addAll(local);
        }
        return extrato;
    }
    
    public List<Extrato> readStatementFromXLS (File pFile)
    {
        try
        {
            //Ler tudo
            FileInputStream file = new FileInputStream(pFile);
            Workbook workbook = new XSSFWorkbook(file);
            Sheet sheet = workbook.getSheetAt(0);

            int firstRow = sheet.getFirstRowNum();
            int lastRow = sheet.getLastRowNum();
            List<List<String>> data = new ArrayList<>();
            for (int index = firstRow + 1; index <= lastRow; index++) 
            {
                Row row = sheet.getRow(index);
                List<String> rows = new ArrayList<>();
                for (int cellIndex = row.getFirstCellNum(); cellIndex < row.getLastCellNum(); cellIndex++)
                {
                    Cell cell = row.getCell(cellIndex, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                    rows.add(cell.toString());
                    System.out.printf("\t %s ", cell.toString());
                }
                data.add(rows);
                System.out.printf("\n");
            }

            //Procurar as transações
            boolean isStarted = false;
            List<Extrato> logs = new ArrayList<>();
            for (List<String> rows : data)
            {
                if (rows.get(0).equals("Movimentação") && rows.get(1).equals("Liquidação"))
                {
                    isStarted = true;
                    continue;
                }
                if (isStarted)
                {
                    //System.out.println(rows.toString());
                    if (!rows.get(0).isEmpty() && !rows.get(1).isEmpty())
                    {
                        Extrato log = new Extrato(rows.get(0), rows.get(1), rows.get(2), rows.get(4), rows.get(5));
                        logs.add(log);
                    }
                }
            }
            return logs;
        } catch (Exception e)
        {
            System.out.println("readStatementFromXLS ERROR: " + e.toString());
            return null;
        }
    }

    public boolean addToDataBase (List<Extrato> extrato) 
    {
        if (extrato == null)
            return false;
         if (extrato.isEmpty())
            return false;
         
         if (!connectDB())
         {
             System.out.println("Erro de conexão ao banco.");
             return false;
         }
        int[] a = NNPers.saveExtrato(extrato);
        System.out.printf("OK: [%d]\tDUP: [%d]\t ERROR: [%d]. \n", a[0], a[1], a[2]);
         
        disConnectDB();
        return true;
         
    }
    private boolean connectDB ()
    {
        return NNPers.startupPersistence("SSThomas", "ShareQuotes", "sa17", "sbdpu2001");
    }
    
    private void disConnectDB ()
    {
        NNPers.shutdownPersistence();
    }

    
}