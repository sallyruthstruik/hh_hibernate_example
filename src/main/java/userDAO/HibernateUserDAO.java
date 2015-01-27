/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package userDAO;

import java.util.List;
import java.util.concurrent.Callable;
import javax.transaction.Transactional;
import models.Users;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import util.TransactionHelper;

/**
 *
 * @author stas
 */
public class HibernateUserDAO implements UserDAO{
    
    private SessionFactory sessionFactory;
    private TransactionHelper transactionHelper;

    
    private Session session(){
        return sessionFactory.getCurrentSession();  
    }
    
    public HibernateUserDAO(final SessionFactory sessionFactory){
        this.sessionFactory = sessionFactory;
    }
    
    @Override
    public Users getById(final Long id) {
        List<Users> list = session().getNamedQuery("Users.findById")
                .setLong("id", id)
                .list();
        return (list.isEmpty() ? null: list.get(0));
    }
    @Override
    public List<Users> filterByFirstName(final String name) {
        return (List<Users>)session()
            .createCriteria(Users.class)
            .add(Restrictions.eq("firstName", name))
            .list();
    }

    @Override
    public List<Users> filterByLastName(final String name) {
        return (List<Users>)session()
            .getNamedQuery("Users.findByLastName")
            .setString("lastName", name)
            .list();
    }

    @Override
    public void add(final Users user) {
        session().save(user);
    }

    @Override
    public void delete(final Users user) {
        session().delete(user);
    }

    @Override
    public void update(final Users user) {
        session().update(user);
    }
}
