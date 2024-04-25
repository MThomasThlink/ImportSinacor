
package com.thlink.NNPdf;

import com.thlink.NNPdf.entities.Arquivo;
import com.thlink.NNPdf.entities.Negocio;
import com.thlink.NNPdf.entities.Nota;
import com.thlink.statement.entities.Extrato;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import org.apache.log4j.Logger;

public class NNPers 
{

    private static final Logger logger = Logger.getLogger(NNPers.class);
    private static EntityManagerFactory emf;
    private static EntityManager gINS;
    
    public static boolean startupPersistence (String pBancoServidor, String pBancoNome, String pBancoLogin, String pBancoSenha) 
    {
        //Logger.getLogger("org.hibernate").setLevel(Level.OFF);
        //Logger.getLogger("org.jboss.logging").setLevel(Level.OFF);
        logger.info("Iniciando conexao ao banco SQLServer. ");
        try
        {
            Map properties = new HashMap();
            properties.put("javax.persistence.jdbc.driver", "net.sourceforge.jtds.jdbc.Driver");
            properties.put("hibernate.dialect", "org.hibernate.dialect.SQLServerDialect");
            properties.put("hibernate.show_sql", "false");
          //properties.put("hibernate.hbm2ddl.auto", "create");
            properties.put("javax.persistence.jdbc.url", "jdbc:jtds:sqlserver://" + pBancoServidor + "/" + pBancoNome);
            
            properties.put("javax.persistence.jdbc.user", pBancoLogin);
            properties.put("javax.persistence.jdbc.password", pBancoSenha);
            
          /*logger.info("URL:      " + properties.get("javax.persistence.jdbc.url"));
            logger.info("User:     " + properties.get("javax.persistence.jdbc.user"));
            logger.info("Password: " + properties.get("javax.persistence.jdbc.password"));*/
            
            emf = Persistence.createEntityManagerFactory("NN.PU", properties);
            logger.info("Conexao ok");
            if (emf.isOpen())
            {
                return true;
            }
            return false;
        } catch (PersistenceException e)
        {
            logger.error("startupPersistence ERROR = " + e.toString());
            return false;
        }
    }

    public static void shutdownPersistence() 
    {
        if (emf != null && emf.isOpen())
            emf.close();
    }
    
  /*public static boolean iniciaTransacao ()
    {
        if (!transactionActive)
        {
            logger.info("iniciaTransacao");
            emINS = emf.createEntityManager();   
            emINS.getTransaction().begin();
            transactionActive = true;
            return true;
        }
        else
        {
            logger.error("Já havia uma transação pendente!");
            return true;
        }
    }
    public static boolean finalizaTransacao ()
    {
        logger.info("finalizaTransacao");
        if (emINS.getTransaction().getRollbackOnly())
            return cancelaTransacao();
        if (transactionActive)
        {
            emINS.getTransaction().commit();
            transactionActive = false;
        }
        if (emINS != null)
            emINS.close();
        return true;
    }
    public static boolean cancelaTransacao ()
    {
        logger.info("cancelaTransacao");
        if (transactionActive)
        {
            emINS.getTransaction().rollback();
            transactionActive = false;
        }
        if (emINS != null)
            emINS.close();
        return true;
    }*/
    
    public static Arquivo getArquivoByNome (String pNome)
    {
        Query q;
        try 
        {
            q = gINS.createQuery("SELECT l FROM Arquivo l WHERE l.fileName = :pNome");
            q.setParameter("pNome", pNome);
            q.setMaxResults(1);
            Arquivo x = (Arquivo)q.getSingleResult();
            gINS.close();
            return x;
        } catch (Exception e) {
            if (!e.toString().contains("No entity found for query"))
                logger.error("getArquivoByNome ERROR = " + e.toString());
            return null;
        }        
    }
    
    public static Arquivo saveArquivo (Arquivo pA)
    {
        try
        {
            gINS.persist(pA);
            gINS.refresh(pA);
            return pA;
        } catch (Exception e)
        {
            logger.error("saveArquivo ERROR: " + e.toString());
            return null;
        }
    }

    public static Nota saveNota (Nota pA)
    {
        try
        {
            gINS.persist(pA);
            gINS.refresh(pA);
            return pA;
        } catch (Exception e)
        {
            logger.error("saveNota ERROR: " + e.toString());
            return null;
        }
    }
    public static Boolean saveNegocio (Negocio pA)
    {

        try
        {
            gINS.persist(pA);
            gINS.refresh(pA);
            return true;
        } catch (Exception e)
        {
            if (e.toString().contains("org.hibernate.exception.ConstraintViolationException"))
            {
                return false;
            }
            logger.error("saveNegocio ERROR: " + e.toString());
            return null;
        }
    }
    public static int[] saveExtrato (List<Extrato> logs)
    {
        int ctdError = 0, ctdDup = 0, ctdOk = 0;
        EntityManager emINS = emf.createEntityManager();
        try
        {
            for (Extrato log : logs)
            {
                Boolean b = NNPers.saveExtrato(log, emINS);
                if (b == null)
                    ctdError++;
                else if (!b)
                    ctdDup++;
                else
                    ctdOk++;
            }
            return new int[] { ctdOk, ctdDup, ctdError };
        } catch (Exception e)
        {
            System.out.println("saveExtrato ERROR: " + e.toString());
            return new int[] { ctdOk, ctdDup, ctdError };
        }
        finally
        {
        }
    }
    
    public static Boolean saveExtrato (Extrato log, EntityManager pEM)
    {
        try
        {
            pEM.persist(log);
            //pEM.refresh(log);
            return true;
        } catch (Exception e)
        {
            if (e.toString().contains("org.hibernate.exception.ConstraintViolationException"))
            {
                return false;
            }
            System.out.println("saveExtrato ERROR: " + e.toString());
            return null;
        }
    }
    public static boolean beginTrans ()
    {
        try
        {
            gINS = emf.createEntityManager();
            gINS.getTransaction().begin();
            return true;
        } catch (Exception e)
        {
            System.out.println("beginTrans ERROR: " + e.toString());
            return false;
        }
    }
    
    public static boolean commitTrans ()
    {
        try
        {
            if (gINS != null && gINS.isOpen())
            {
                gINS.getTransaction().commit();
                gINS.close();
                return true;
            }
            
        } catch (Exception e)
        {
            System.out.println("commitTrans ERROR: " + e.toString());
            if (gINS.getTransaction().isActive())
                gINS.getTransaction().rollback();
            gINS.close();
        }
        return false;
    }
}
