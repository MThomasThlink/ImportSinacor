
package com.thlink.NNPdf;

import java.io.FileOutputStream;

public class NNResults 
{
    private long tIni, tFim;
    private String importPath;
    private int qtdNegocios, qtdInseridos, qtdErros;    //qtdDuplicados
    private NNArquivo grupo;
    private int codErro;
    
    public String debug1 (boolean pTxt)
    {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("Arquivo: %s. \n", importPath));
        sb.append(String.format("\t%s\t%s\t%-50s\t%s\t%s\t%8s\t%11s", "Nota", "C/V", "Especificação", "Obs", "Qtd", "Preço", "Valor\n"));
        
        if (grupo.getLstDocs() != null && !grupo.getLstDocs().isEmpty())
        {
            for (NNNota doc : grupo.getLstDocs())
                sb.append(doc.toString());
        }
        sb.append("\n");
        if (pTxt)
        {
            try
            {
                FileOutputStream outputStream = new FileOutputStream(importPath.replace(".pdf", ".txt"));
                byte[] strToBytes = sb.toString().getBytes();
                outputStream.write(strToBytes);
                outputStream.close();
            } catch (Exception e)
            {
                System.out.println("Erro ao criar arquivo.");
            }
        }
        return sb.toString();
    }
    public String debug2 (boolean pTxt)
    {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("Negocios: %d. \tInseridos: %d. \tErros: %d.\n\n", qtdNegocios, qtdInseridos, qtdErros));        
        sb.append("\n");
        return sb.toString();
    }
    public long gettIni() {
        return tIni;
    }

    public void settIni(long tIni) {
        this.tIni = tIni;
    }

    public long gettFim() {
        return tFim;
    }

    public void settFim(long tFim) {
        this.tFim = tFim;
    }

    public String getImportPath() {
        return importPath;
    }

    public void setImportPath(String importPath) {
        this.importPath = importPath;
    }

    public int getQtdNegocios() {
        return qtdNegocios;
    }

    public void setQtdNegocios(int qtdINegocios) {
        this.qtdNegocios = qtdINegocios;
    }

    public int getQtdInseridos() {
        return qtdInseridos;
    }

    public void setQtdInseridos(int qtdInseridos) {
        this.qtdInseridos = qtdInseridos;
    }

    public int getQtdErros() {
        return qtdErros;
    }

    public void setQtdErros(int qtdErros) {
        this.qtdErros = qtdErros;
    }

    public NNArquivo getGrupo() {
        return grupo;
    }

    public void setGrupo(NNArquivo grupo) {
        this.grupo = grupo;
    }

    public int getCodErro() {
        return codErro;
    }

    public void setCodErro(int codErro) {
        this.codErro = codErro;
    }


}
