/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package userService;

import java.math.BigInteger;
import java.util.List;
import javax.sql.DataSource;
import models.Transactions;
import models.Users;
import org.springframework.transaction.annotation.Transactional;
import transactionsDAO.transactionsDAO;
import transactionsDAO.SpringTransactionsDAO;
import userDAO.SpringUserDao;
import userDAO.UserDAO;

/**
 *
 * @author stas
 */
public class SpringUserService implements UserService{
    
    DataSource dataSource;
    UserDAO userDao;
    transactionsDAO trDao;
    
    public SpringUserService(DataSource dataSource){
        this.dataSource = dataSource;
        this.userDao = new SpringUserDao(dataSource);
        this.trDao = new SpringTransactionsDAO(dataSource);
    }
    
    @Override
    public Users register(String first_name, String last_name) {
        Users user = new Users(null, first_name, last_name);
        userDao.add(user);
        return user;
    }

    @Override
    public Users getById(long id) {
        return userDao.getById(id);
    }

    @Override
    @Transactional
    public Users update(Long user_id, String new_first_name, String new_last_name) {
        if(user_id == null)
            throw new IllegalArgumentException("User id must present");
        
        Users user = getById(user_id);
        
        if(user == null)
            throw new IllegalArgumentException("No user with id "+user_id);
        
        user.setFirstName(new_first_name);
        user.setLastName(new_last_name);
        
        userDao.update(user);
        
        return user;
    }

    @Override
    public List<Users> filterByFirstName(String name) {
        return userDao.filterByFirstName(name);
    }

    @Override
    public List<Users> filterByLastName(String name) {
        return userDao.filterByLastName(name);
    }

    @Override
    @Transactional
    public void createMoneyMovement(Users user1, Users user2, BigInteger amount) {
        if(user1 == null || user2 == null || user1.getId() == null || user2.getId() == null)
            throw new IllegalArgumentException("Both of users must present and has not null id");
        if(amount.compareTo(BigInteger.ZERO)<=0)
            throw new IllegalArgumentException("amount must be greater than 0");
        if(user1.getBalance().compareTo(amount) <= 0)
            throw new IllegalArgumentException("Not enough money");
        
        
        user1.setBalance(user1.getBalance().subtract(amount));
        userDao.update(user1);
        
        Transactions out = new Transactions(null, amount.not());
        out.setUser(user1);
        trDao.add(out);
        
        Transactions in = new Transactions(null, amount);
        in.setUser(user2);
        trDao.add(in);
        
        user2.setBalance(user2.getBalance().add(amount));
        userDao.update(user2);
    }

    @Override
    public Transactions createReplenishment(Users user, BigInteger amount) {
        if(user == null || user.getId() == null)
            throw new IllegalArgumentException("User must present and has id");
        if(amount.compareTo(BigInteger.ZERO)<=0)
            throw new IllegalArgumentException("Amount must be >=0");
        
        user.setBalance(user.getBalance().add(amount));
        userDao.update(user);
        
        Transactions tr = new Transactions(null, amount);
        tr.setUser(user);
        trDao.add(tr);
        
        return tr;
    }
    
}
