
package com.thlink.NNPdf;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class NNNegocio 
{
    private String Q, negociacao, C_V, tipoMercado, prazo, especificacao, obs,  D_C;
    private int quantidade;
    private double preçoAjuste, valorAjuste;

    @Override
    public String toString ()
    {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("\t\t[%s]\t%-50s\t", C_V, especificacao));
        sb.append(String.format("%s\t%4d\t% 8.2f\t% 10.2f\n", obs != null ? "'".concat(obs).concat("'") : "   ", quantidade, preçoAjuste, valorAjuste));
        return sb.toString();
    }
    
    private String tokenizeX (String line, String pat) 
    {
        Pattern pattern = Pattern.compile(pat);
        Matcher matcher = pattern.matcher(line);
        matcher.find();
        return matcher.group();
    }
    private int fimEspec (String pData)
    {
        try
        {
            Integer i = Integer.parseInt(pData.replace(".", "").replace("<", "."));
            return 1;
        } catch (Exception e)
        {
            if (pData.equals("#") || pData.equals("D") || pData.equals("D#") || pData.equals("d") || pData.equals("d#"))
                return 2;
            return -1;
        }
    }
    
    public NNNegocio (String[] pData)
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
                    if (i == 3 || pData[i].length() > 3)
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
            
}
