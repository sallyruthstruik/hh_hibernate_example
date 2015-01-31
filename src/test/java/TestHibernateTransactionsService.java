/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.util.List;
import java.math.BigInteger;
import models.Transactions;
import models.Users;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import transactionService.HibernateTransactionService;
import transactionService.TransactionService;
import userService.HibernateUserService;
import userService.UserService;

/**
 *
 * @author stas
 */
public class TestHibernateTransactionsService extends HibernateBaseTest{
    
    TransactionService trService;
    UserService userService;
    
    @Override
    public void setUp() {
        super.setUp(); //To change body of generated methods, choose Tools | Templates.
        trService = new HibernateTransactionService(sessionFactory);
        userService = new HibernateUserService(sessionFactory);
    }
    
    public Users createUserWithBalance(String name, String lname){
        Users user = userService.register(name, lname);
        userService.createReplenishment(user, BigInteger.valueOf(1000));
        return user;
    }
    
    @Test
    public void testGetFilterMethods(){
        Users user1 = createUserWithBalance("User1", "Lname 1");
        Users user2 = createUserWithBalance("User2", "Lname 2");
        
        List<Transactions> all = trService.getAll();
        assertEquals(all.size(), 2);
        assertEquals(all.get(0).getUser(), user1);
        assertEquals(all.get(1).getUser(), user2);
        
        List<Transactions> for_user1 = trService.filterByUserId(user1.getId());
        assertEquals(all.size(), 2);
        assertEquals(all.get(0).getUser(), user1);
        
        assertEquals(user1.getBalance().longValue(), trService.getBalance(user1.getId()), 0.001);
        
    }
    
}
