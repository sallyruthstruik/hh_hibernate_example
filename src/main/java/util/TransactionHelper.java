/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import java.util.concurrent.Callable;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

/**
 *
 * @author stas
 */
public class TransactionHelper {
    
    private static TransactionHelper INSTANCE;
    private SessionFactory sessionFactory;
    
    public static TransactionHelper getInstance() {
        if(INSTANCE == null)
            INSTANCE = new TransactionHelper();
        return INSTANCE;
    }
    
    private Session session(){
        return sessionFactory.getCurrentSession();  
    }
    
    private TransactionHelper(){
        this.sessionFactory = HibernateUtil.getSessionFactory();
    }
    
    public void inTransaction(Runnable lambdaFunc){
        Session session = this.session();
        Transaction transaction = session.beginTransaction();
        try{
            lambdaFunc.run();
            transaction.commit();
        }catch(Exception e){
            transaction.rollback();
            System.err.println("Unexpected exception in transaction "+e);
            throw e;
        }
    }
    public <V> V inTransaction(Callable<V> lambdaFunc){
        Transaction transaction = session().beginTransaction();
        try{
            V out = lambdaFunc.call();
            transaction.commit();
            return out;
        }catch(RuntimeException e){
            transaction.rollback();
            System.err.println("Error in transaction");
            throw e;
        }catch(Exception e){
            transaction.rollback();
            System.err.println("Error in transaction");
            throw new RuntimeException(e);
        }
    }
    
}
