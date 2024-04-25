package com.thlink.statement.entities;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.Transient;

@Entity
public class Extrato implements Serializable
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Temporal(javax.persistence.TemporalType.DATE)
    private Date dataLiquidacao;
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date dataLancamento;
    private String transacao;
    private Double valor, saldo;

    @Transient
    private int transacaoSize = 100;

        
    public Extrato() {
    }

    public Extrato (String strLiq, String strLanc, String strTrans, String strValor, String strSaldo)
    {
        try
        {
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MMMM-yyyy");
            dataLiquidacao = sdf.parse(strLiq);
            dataLancamento = sdf.parse(strLanc);
            transacao = strTrans;
            strValor = strValor.replace("R$ ", "");//.replace(".", "").replace(",", ".");
            valor = Double.valueOf(strValor);
            strSaldo = strSaldo.replace("R$ ", "");//.replace(".", "").replace(",", ".");
            saldo = Double.valueOf(strSaldo);
        } catch (Exception e)
        {
            System.out.println("Erro no construtor: " + e.toString());
        }
    }
    
    @Override
    public String toString ()
    {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        StringBuilder sb = new StringBuilder();
        sb.append(sdf.format(dataLiquidacao)).append("\t");
        sb.append(sdf.format(dataLancamento)).append("\t");
        String repeated = String.join("", Collections.nCopies(transacaoSize - transacao.length(), " "));
        sb.append(transacao).append(repeated).append("\t");
        sb.append(String.format("%10.2f", valor)).append("\t");
        sb.append(String.format("%10.2f", saldo)).append("\t");
        return sb.toString();
    }
    
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getDataLiquidacao() {
        return dataLiquidacao;
    }

    public void setDataLiquidacao(Date dataLiquidacao) {
        this.dataLiquidacao = dataLiquidacao;
    }

    public Date getDataLancamento() {
        return dataLancamento;
    }

    public void setDataLancamento(Date dataLancamento) {
        this.dataLancamento = dataLancamento;
    }

    public String getTransacao() {
        return transacao;
    }

    public void setTransacao(String transacao) {
        this.transacao = transacao;
    }

    public Double getValor() {
        return valor;
    }

    public void setValor(Double valor) {
        this.valor = valor;
    }

    public Double getSaldo() {
        return saldo;
    }

    public void setSaldo(Double saldo) {
        this.saldo = saldo;
    }
    
}
