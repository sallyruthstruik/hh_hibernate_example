/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package transactionsDAO;

import java.math.BigInteger;
import java.util.List;
import java.util.concurrent.Callable;
import models.Transactions;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import util.TransactionHelper;

/**
 *
 * @author stas
 */
public class HibernateTransactionsDAO implements transactionsDAO{
    
    SessionFactory sessionFactory;
    
    public HibernateTransactionsDAO(SessionFactory sessionFactory){
        this.sessionFactory = sessionFactory;
    }
   
    
    Session session(){
        return this.sessionFactory.getCurrentSession();
    }
    
    @Override
    public void add(final Transactions t) {
        session().save(t);
    }

    @Override
    public void update(final Transactions t) {
        session().update(t);
    }

    @Override
    public void delete(final Transactions t) {
        session().delete(t);
    }

    @Override
    public Transactions getById(final long id) {
        List<Transactions> list = (List<Transactions>)session()
                .getNamedQuery("Transactions.findById")
                .setLong("id", id)
                .list();
        return (list.isEmpty()?null:list.get(0));
    }

    @Override
    public List<Transactions> filterByUserId(final long user_id) {
        List<Transactions> list = (List<Transactions>)session()
                .createCriteria(Transactions.class)
                .add(Restrictions.eq("user.id", user_id))
                .list();
        return list;
    }

    @Override
    public List<Transactions> getAll() {
        return session()
                .createCriteria(Transactions.class)
                .list();
    }

    @Override
    public Long getBalance(final long user_id) {
        List<BigInteger> list = session()
                .createCriteria(Transactions.class)
                .setProjection(Projections.projectionList()
                        .add(Projections.sum("amount"))
                )
                .add(Restrictions.eq("user.id", user_id))
                .list();
        return list.isEmpty()?null:list.get(0).longValue();
    }
    
}
