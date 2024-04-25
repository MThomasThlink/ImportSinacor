
package com.thlink.NNPdf.entities;

import com.thlink.NNPdf.NNNegocio;
import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Negocio implements Serializable 
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private Long idNota;
    private String Q, negociacao, C_V, tipoMercado, prazo, especificacao, obs,  D_C;
    private int quantidade;
    private double preçoAjuste, valorAjuste;

    public Negocio ()
    {
        
    }
    public Negocio (NNNegocio pNNN, Nota pNota)
    {
        idNota = pNota.getId();
        negociacao = pNNN.getNegociacao();
        C_V = pNNN.getC_V();
        tipoMercado = pNNN.getTipoMercado();
        especificacao = pNNN.getEspecificacao();
        obs = pNNN.getObs();
        quantidade = pNNN.getQuantidade();
        preçoAjuste = pNNN.getPreçoAjuste();
        valorAjuste = pNNN.getValorAjuste();
        D_C = pNNN.getD_C();
    }
    
    public Negocio (String[] pData)
    {
        int t = pData.length; 
        try
        {
            //System.out.printf("len = %d. \n", t);
            Q = "";
            negociacao = pData[0];
            C_V = pData[1];
            tipoMercado = pData[2];
            prazo = ""; //pData.substring(24, 30);
            especificacao = "";
            int ret, i = 3;
            do
            {
                ret = fimEspec(pData[i]);
                if (ret == -1)
                {
                    if (!pData[i].trim().equals(""))
                        especificacao = especificacao.concat(pData[i]).concat(" ");
                }
                else
                    break;
                i++;
            } while (i < t);
            if (ret == 2)
                obs = pData[i];
            quantidade = Integer.parseInt(pData[t-4].replace(".", ""));
            preçoAjuste = Double.parseDouble(pData[t-3].replace(".", "").replace(",", "."));
            valorAjuste = Double.parseDouble(pData[t-2].replace(".", "").replace(",", "."));
            D_C = pData[t-1];
        } catch (Exception e)
        {
            System.out.println("class ERROR: " + e.toString());
        }
    }

        
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getIdNota() {
        return idNota;
    }

    public void setIdNota(Long idNota) {
        this.idNota = idNota;
    }

    public String getQ() {
        return Q;
    }

    public void setQ(String Q) {
        this.Q = Q;
    }

    public String getNegociacao() {
        return negociacao;
    }

    public void setNegociacao(String negociacao) {
        this.negociacao = negociacao;
    }

    public String getC_V() {
        return C_V;
    }

    public void setC_V(String C_V) {
        this.C_V = C_V;
    }

    public String getTipoMercado() {
        return tipoMercado;
    }

    public void setTipoMercado(String tipoMercado) {
        this.tipoMercado = tipoMercado;
    }

    public String getPrazo() {
        return prazo;
    }

    public void setPrazo(String prazo) {
        this.prazo = prazo;
    }

    public String getEspecificacao() {
        return especificacao;
    }

    public void setEspecificacao(String especificacao) {
        this.especificacao = especificacao;
    }

    public String getObs() {
        return obs;
    }

    public void setObs(String obs) {
        this.obs = obs;
    }

    public String getD_C() {
        return D_C;
    }

    public void setD_C(String D_C) {
        this.D_C = D_C;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public double getPreçoAjuste() {
        return preçoAjuste;
    }

    public void setPreçoAjuste(double preçoAjuste) {
        this.preçoAjuste = preçoAjuste;
    }

    public double getValorAjuste() {
        return valorAjuste;
    }

    public void setValorAjuste(double valorAjuste) {
        this.valorAjuste = valorAjuste;
    }

    private int fimEspec (String pData)
    {
        try
        {
            Integer i = Integer.parseInt(pData.replace(".", "").replace("<", "."));
            return 1;
        } catch (Exception e)
        {
            if (pData.equals("#") || pData.equals("D"))
                return 2;
            return -1;
        }
    }


    @Override
    public String toString ()
    {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("\t\t[%s]\t%-50s\t", C_V, especificacao));
        sb.append(String.format("%s\t%4d\t% 8.2f\t% 10.2f\n", obs != null ? "'".concat(obs).concat("'") : "   ", quantidade, preçoAjuste, valorAjuste));
        return sb.toString();
    }

}
