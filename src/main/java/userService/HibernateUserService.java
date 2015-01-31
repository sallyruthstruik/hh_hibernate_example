/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package userService;

import java.math.BigInteger;
import java.util.List;
import java.util.concurrent.Callable;
import models.Transactions;
import models.Users;
import org.hibernate.SessionFactory;
import transactionsDAO.transactionsDAO;
import transactionsDAO.HibernateTransactionsDAO;
import userDAO.HibernateUserDAO;
import userDAO.UserDAO;
import util.TransactionHelper;

/**
 *
 * @author stas
 */
public class HibernateUserService implements UserService{
    
    UserDAO userDao;
    transactionsDAO trDao;
    SessionFactory sessionFactory;
    TransactionHelper trHelper;
    
    public HibernateUserService(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
        userDao = new HibernateUserDAO(sessionFactory);
        trDao = new HibernateTransactionsDAO(sessionFactory);
        trHelper = TransactionHelper.getInstance();
    }
    
    
    @Override
    public Users register(final String first_name, final String last_name) {
        if(first_name == null || last_name == null)
            throw new IllegalArgumentException();
        return trHelper.inTransaction(new Callable<Users>() {
            @Override
            public Users call() throws Exception {
                Users user = new Users(null, first_name, last_name);
                userDao.add(user);
                return user;
            }
        });
        
    }

    @Override
    public Users getById(final long id) {
        return trHelper.inTransaction(new Callable<Users>() {

            @Override
            public Users call() throws Exception {
                return userDao.getById(id);
            }
        });
    }

    @Override
    public List<Users> filterByFirstName(final String name) {
        return trHelper.inTransaction(new Callable<List<Users>>() {

            @Override
            public List<Users> call() throws Exception {
                return userDao.filterByFirstName(name);
            }
        });
    }

    @Override
    public List<Users> filterByLastName(final String name) {
        return trHelper.inTransaction(new Callable<List<Users>>() {

            @Override
            public List<Users> call() throws Exception {
                return userDao.filterByLastName(name);
            }
        });
    }
    
    @Override
    public Users update(final java.lang.Long user_id, final String new_first_name, final String new_last_name) {
        return trHelper.inTransaction(new Callable<Users>() {
            @Override
            public Users call() throws Exception {
                Users user = userDao.getById(user_id);
                if(user == null)
                    throw new IllegalArgumentException("No users with id "+user_id);
                user.setFirstName(new_first_name);
                user.setLastName(new_last_name);
                userDao.update(user);
                return user;
            }
        });
        
        
    }
    
    /*
    Выполняет транзакцию от юзера 1 к юзеру 2.
    Схема: снимаем деньги с баланса первого юзера, создаем транзакцию на снятие первому юзеру, начисляем деньги второму юзеру, создаем транзакцию на пополнение.
    
    @param amount положительная сумма для переведения
    */
    @Override
    public void createMoneyMovement(final Users from_user, final Users to_user, final BigInteger amount) {
        if(amount.compareTo(BigInteger.ZERO)<=0)
            throw new IllegalArgumentException("amount must be > 0");
        
        trHelper.inTransaction(new Runnable() {
            @Override
            public void run() {
                if(from_user.getBalance().compareTo(amount)<=0)
                    throw new IllegalArgumentException("У юзера "+from_user+" недостаточно денег на счете");
                
                from_user.setBalance(from_user.getBalance().subtract(amount));
                
                Transactions outcoming = new Transactions(null, amount.not());
                Transactions incoming = new Transactions(null, amount);
                
                to_user.setBalance(to_user.getBalance().add(amount));
                
                outcoming.setUser(from_user);
                incoming.setUser(to_user);
                
                userDao.update(from_user);
                userDao.update(to_user);
                trDao.add(outcoming);
                trDao.add(incoming);
            }
        });
    }
    
    /*
    Имитация пополнения юзером баланса
    */
    @Override
    public Transactions createReplenishment(final Users user, final BigInteger amount) {
        if(amount.compareTo(BigInteger.ZERO)<=0)
            throw new IllegalArgumentException("amount must be > 0");
        
        return trHelper.inTransaction(new Callable<Transactions>() {

            @Override
            public Transactions call() throws Exception {
                user.setBalance(user.getBalance().add(amount));
                
                Transactions incoming = new Transactions(null, amount);
                incoming.setUser(user);
                userDao.add(user);
                trDao.add(incoming);
                return incoming;
            }
        });
    }
    
}
