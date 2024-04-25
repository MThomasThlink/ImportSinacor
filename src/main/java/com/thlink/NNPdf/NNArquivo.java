
package com.thlink.NNPdf;

import java.util.List;

public class NNArquivo 
{
    private String fileName;
    private List<NNNota> lstDocs;

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public List<NNNota> getLstDocs() {
        return lstDocs;
    }

    public void setLstDocs(List<NNNota> lstDocs) {
        this.lstDocs = lstDocs;
    }
    
    
}
