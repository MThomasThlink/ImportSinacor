
package com.thlink.NNPdf;

import java.io.File;
import java.util.Collection;

public class App 
{
    public static void main(String[] args)
    {

        importaNotasDaPasta("C:\\projetosII\\Dropbox\\Personal\\Fincancas\\RICO\\Notas");
        //new StatementHelper().importStatementPDF("C:\\projetosII\\Dropbox\\Personal\\Fincancas\\RICO\\Extratos\\2022_01-01 a 31-12.pdf");
        //StatementHelper helper = new StatementHelper();
        //List<Extrato> extrato = helper.readStatementFromXLS(new File("C:\\projetosII\\Dropbox\\Personal\\Fincancas\\RICO\\Extratos\\2022_01-01 a 31-12.xlsx"));
        //List<Extrato> extrato = helper.readStatementFromXLSFolder("C:\\projetosII\\Dropbox\\Personal\\Fincancas\\RICO\\Extratos\\");
        //helper.addToDataBase(extrato);
        
    }

    private static void importaNotasDaPasta (String pPasta) 
    {
        NNPdfImporter imp = new NNPdfImporter();
        File dir = new File(pPasta);
        Collection<File> arquivos = new Util().listFileTree(dir, "pdf");
        if (arquivos.isEmpty())
        {
            System.out.println("Sem arquivos.");
            return;
        }
        if (!imp.connectDB())
        {
            System.out.println("Erro de conexão ao banco.");
            return;
        }
        
        NNResults r;
        for (File arquivo : arquivos)
        {
            String f = arquivo.getAbsolutePath();
            r = imp.goImport(f, "668");
            if (r != null)
            {
                String debug1 = r.debug1(true);
                System.out.println(debug1);
                r = imp.saveDB(r.getGrupo());
                
                String debug2 = r.debug2(true);
                System.out.println(debug2);

                switch(r.getCodErro())
                {
                    case 0:
                        System.out.println("Ok");
                        break;
                    case -1:
                        System.out.println("Arquivo já processado.");
                        break;
                }
            }
            else
                System.out.println("Erro na importação do arquivo.");
        }
        imp.disConnectDB();
        System.out.println("Fim.");
    }
}
