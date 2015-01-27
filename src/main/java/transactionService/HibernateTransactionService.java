/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package transactionService;

import java.util.List;
import java.util.concurrent.Callable;
import models.Transactions;
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
public class HibernateTransactionService implements TransactionService{
    
    TransactionHelper trHelper;
    UserDAO userDao;
    transactionsDAO trDao;
    
    public HibernateTransactionService(SessionFactory sessionFactory){
        userDao = new HibernateUserDAO(sessionFactory);
        trDao = new HibernateTransactionsDAO(sessionFactory);
        trHelper = TransactionHelper.getInstance();
    }
    
    @Override
    public Transactions getById(final Long id) {
        return trHelper.inTransaction(new Callable<Transactions>() {
            @Override
            public Transactions call() throws Exception {
                return trDao.getById(id);
            }
        });
    }

    @Override
    public List<Transactions> getAll() {
        return trHelper.inTransaction(new Callable<List<Transactions>>() {
            @Override
            public List<Transactions> call() throws Exception {
                return trDao.getAll();
            }
        });
    }

    @Override
    public List<Transactions> filterByUserId(final Long user_id) {
        return trHelper.inTransaction(new Callable<List<Transactions>>() {
            @Override
            public List<Transactions> call() throws Exception {
                return trDao.filterByUserId(user_id);
            }
        });
    }

    @Override
    public Long getBalance(final long user_id) {
        return trHelper.inTransaction(new Callable<Long>() {
            @Override
            public Long call() throws Exception {
                return trDao.getBalance(user_id);
            }
        });
    }
}
