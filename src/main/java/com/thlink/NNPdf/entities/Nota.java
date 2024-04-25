
package com.thlink.NNPdf.entities;

import com.thlink.NNPdf.NNNota;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Temporal;

@Entity
public class Nota implements Serializable 
{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private Long idArquivo;
    private Integer noNota;
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date dataPregao;

    public Nota ()
    {
        
    }
    public Nota (NNNota pNNN, Arquivo pA)
    {
        idArquivo = pA.getId();
        noNota = pNNN.getNoNota();
        dataPregao = pNNN.getDataPregao();
    }
    
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getIdArquivo() {
        return idArquivo;
    }

    public void setIdArquivo(Long idArquivo) {
        this.idArquivo = idArquivo;
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

    
}
