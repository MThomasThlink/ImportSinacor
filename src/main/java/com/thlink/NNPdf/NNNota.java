
package com.thlink.NNPdf;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class NNNota 
{
    private Integer noNota;
    private Date dataPregao;
    List<NNNegocio> listItems;

    @Override
    public String toString ()
    {
        StringBuilder sb = new StringBuilder();
        if (dataPregao != null)
            sb.append(String.format("\tNo: %d\tData: %s\n", noNota, new SimpleDateFormat("dd/MM/yyyy").format(dataPregao)));
        else
            sb.append(String.format("\tNo: %d\tData: %s\n", noNota, "SEM DATA"));
        if (listItems != null && !listItems.isEmpty())
        {
            for (NNNegocio item : listItems)
                sb.append(item.toString());
        }
        sb.append("\n");
        return sb.toString();
    }
    
    public Integer getNoNota() {
        return noNota;
    }

    public void setNoNota(Integer noNota) {
        this.noNota = noNota;
    }

    public Date getDataPregao() {
        return dataPregao;
    }

    public void setDataPregao(Date dataPregao) {
        this.dataPregao = dataPregao;
    }

    public List<NNNegocio> getListItems() {
        return listItems;
    }

    public void setListItems(List<NNNegocio> listItems) {
        this.listItems = listItems;
    }

    
}
