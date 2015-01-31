/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package transactionService;

import java.util.List;
import javax.sql.DataSource;
import models.Transactions;
import transactionsDAO.transactionsDAO;
import transactionsDAO.SpringTransactionsDAO;

/**
 *
 * @author stas
 */
public class SpringTransactionService implements TransactionService{
    
    private transactionsDAO trDao;
    private DataSource dataSource;
    
    public SpringTransactionService(DataSource dataSource){
        this.dataSource = dataSource;
        this.trDao = new SpringTransactionsDAO(this.dataSource);
    }
    
    @Override
    public Transactions getById(Long id) {
        return trDao.getById(id);
    }

    @Override
    public List<Transactions> getAll() {
        return trDao.getAll();
    }

    @Override
    public List<Transactions> filterByUserId(Long user_id) {
        return trDao.filterByUserId(user_id);
    }

    @Override
    public Long getBalance(long user_id) {
        return trDao.getBalance(user_id);
    }
    
}
