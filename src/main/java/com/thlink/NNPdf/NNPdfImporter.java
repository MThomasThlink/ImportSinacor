
package com.thlink.NNPdf;

import com.thlink.NNPdf.entities.Arquivo;
import com.thlink.NNPdf.entities.Negocio;
import com.thlink.NNPdf.entities.Nota;
import com.thlink.PDF.PDFToText;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.log4j.Logger;

public class NNPdfImporter 
{
    private static final Logger logger = Logger.getLogger(NNPdfImporter.class);

    private PDFToText pdf2Text;
    
    public NNResults goImport (String pImportPath, String pSenha)
    {
        try
        {
            long tIni = Calendar.getInstance().getTimeInMillis();
            pdf2Text = new PDFToText(pImportPath, pSenha);
            NNArquivo grupo = find(pdf2Text.getText());
    
            NNResults r = new NNResults();

            grupo.setFileName(pImportPath);
            long tFim = Calendar.getInstance().getTimeInMillis();
            r.setGrupo(grupo);
            r.setImportPath(pImportPath);
            r.settIni(tIni);
            r.settFim(tFim);
            return r;
        } catch (Exception e)
        {
            logger.error("goImport ERROR: " + e.toString());
            return null;
        }
    }

    public boolean connectDB ()
    {
        return NNPers.startupPersistence("SSThomas", "ShareQuotes", "sa17", "sbdpu2001");
    }
    
    public void disConnectDB ()
    {
        NNPers.shutdownPersistence();
    }
    
    public NNResults saveDB (NNArquivo pNNA)
    {
        logger.info("Salvando dados ...");
        NNPers.beginTrans();
        NNResults n = new NNResults();
        try
        {
            Arquivo a = NNPers.getArquivoByNome(pNNA.getFileName());
            if (a != null)
            {
                n.setCodErro(-1);
                return n;
            }
            a = new Arquivo(pNNA);
            a = NNPers.saveArquivo(a);
            if (a == null)
            {
                n.setCodErro(-2);
                return n;
            }
            int qtdNegocios = 0, qtdInseridos = 0, qtdErros = 0;    //qtdDuplicados = 0,
            for (NNNota nnNota : pNNA.getLstDocs())
            {
                Nota nota = new Nota(nnNota, a);
                nota = NNPers.saveNota(nota);
                if (nota != null)
                {
                    for (NNNegocio nnNeg : nnNota.getListItems())
                    {
                        Negocio negocio = new Negocio(nnNeg, nota);
                        Boolean b = NNPers.saveNegocio(negocio);
                        qtdNegocios++;
                        System.out.print(".");
                        if (b == null)
                            qtdErros++;
                        else if (b)
                            qtdInseridos++;
                        /*else
                            qtdDuplicados++;*/
                    }
                }
            }
            n.setQtdNegocios(qtdNegocios);
            n.setQtdInseridos(qtdInseridos);
          //n.setQtdDuplicados(qtdDuplicados);
          NNPers.commitTrans();
            n.setQtdErros(qtdErros);
            logger.info("Dados salvos!");
            return n;
        } catch (Exception e)
        {
            logger.error("saveDB ERROR: " + e.toString());
            return null;
        }
    }
    
    public NNArquivo find (String text) 
    {
        //System.out.println(text);
            String [] documentLines = text.split("\r\n|\r|\n");		
            NNArquivo grupo = new NNArquivo();
            NNNota doc = null;
            List<NNNota> lstDocs = new ArrayList<>();
            List<NNNegocio> negocios = new ArrayList<>();
            boolean isItem = false;
            for (int i = 0; i < documentLines.length; i++) 
            {
                //System.out.println(documentLines[i]);
                //if (1 == 1) continue;
                
                if (documentLines[i].contains("NOTA DE NEGOCIAÇÃO")) 
                {
                    isItem = false;
                }
                if (documentLines[i].equals("Folha")) 
                    doc.setNoNota(Integer.parseInt(documentLines[i-1]));
                if (documentLines[i].equals("Rico Investimentos - Grupo XP") || documentLines[i].equals("RICO CORRETORA DE TITULOS E VALORES MOBILIARIOS S.A."))
                {
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                    try
                    {
                        doc.setDataPregao(sdf.parse(documentLines[i-1]));
                    } catch (Exception e)
                    {
                        doc.setDataPregao(Calendar.getInstance().getTime());
                    }
                }

                if(isItem)
                {
                    String[] parts = documentLines[i].split(" ");	
                    if (!documentLines[i].startsWith("Q Negociação C/V"))
                    {
                        byte[] b = documentLines[i].getBytes();
                        NNNegocio item = new NNNegocio(parts);
                        //System.out.printf("{%02d}[% 4d][%03d] '%s'. \n", parts.length, i, documentLines[i].length(), documentLines[i]);
                        if (item.getEspecificacao() != null)
                            negocios.add(item);
                    }
                }
                else if (documentLines[i].contains("Negócios realizados")) 
                {
                    if (doc != null)
                    {
                        doc.setListItems(negocios);
                        lstDocs.add(doc);
                    }
                    isItem = true;
                    doc = new NNNota();
                    negocios = new ArrayList<>();
                }
                else if (documentLines[i].contains("Resumo dos Negócios")) 
                {
                    isItem = false;

                }
                

                else if(documentLines[i].contains("Resumo Financeiro")) 
                {
                }
            }
            
            if (doc != null)
            {
                doc.setListItems(negocios);
                lstDocs.add(doc);
            }
            grupo.setLstDocs(lstDocs);
            return grupo;
	}
	
	private String tokenizeBov(String line) 
        {
            Pattern pattern = Pattern.compile("\\d+([\\.\\,]\\d+)+");
            Matcher matcher = pattern.matcher(line);
            matcher.find();
            return matcher.group();
	}
	
	private Double extract(String text) 
        {
            String [] textSplitted = text.replace(".", "").replace(",", ".").split("\\|") ;
            Double amount = Double.parseDouble(textSplitted[0].trim());
            if(textSplitted.length >= 2) {
                    amount = (textSplitted[1].trim()).equalsIgnoreCase("D") ? amount * (-1) : amount;
            }
            return amount;
	}

}
