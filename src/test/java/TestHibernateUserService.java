/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.math.BigInteger;
import models.Transactions;
import models.Users;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import userService.HibernateUserService;
import userService.UserService;

/**
 *
 * @author stas
 */
public class TestHibernateUserService extends HibernateBaseTest{
    
    UserService userService;
    
    public TestHibernateUserService() {
    }

    @Override
    public void setUp() {
        super.setUp(); //To change body of generated methods, choose Tools | Templates.
        userService = new HibernateUserService(sessionFactory);
    }
    
    @Test
    public void testUserRegistration(){
        Users user = userService.register("Stas", "Kaledin");
        
        assertNotNull(user.getId());
        assertEquals(user.getBalance(), BigInteger.ZERO);
    }
    
    @Test
    public void testGetUserByIdNameAndLastName(){
        testUserRegistration();
        
        Users userByFirstName = userService.filterByFirstName("Stas").get(0);
        Users userByLastName = userService.filterByLastName("Kaledin").get(0);
        Users userById = userService.getById(userByFirstName.getId());
        assertEquals(userByFirstName, userByLastName);
        assertEquals(userById, userByLastName);
        
    }
    
    @Test
    public void testUpdateUserAfterRegistration(){
        testUserRegistration(); 
        
        Users user = userService.filterByFirstName("Stas").get(0);
        userService.update(user.getId(), "Bla", "BlaBla");
        
        Users changed = userService.getById(user.getId());
        
        assertEquals(changed.getFirstName(), "Bla");
        assertEquals(changed.getLastName(), "BlaBla");
    }
    
    @Test
    public void testCreateReplenishment(){
        testUserRegistration();
        
        Users user = userService.filterByFirstName("Stas").get(0);
        
        Transactions tr = userService.createReplenishment(user, BigInteger.valueOf(1000));
        
        Users changed = userService.getById(user.getId());
        assertEquals(changed.getBalance(), BigInteger.valueOf(1000));
        assertEquals(tr.getAmount(), BigInteger.valueOf(1000));
        assertEquals(tr.getUser(), changed);
        
    }
    
    @Test
    public void testMoneyMovement(){
        Users user1 = userService.register("User 1", "User 1");
        Users user2 = userService.register("User 2", "User 2");
        
        userService.createReplenishment(user1, BigInteger.valueOf(1000));
        
        userService.createMoneyMovement(user1, user2, BigInteger.valueOf(100));
        
        assertEquals(userService.getById(user1.getId()).getBalance(), BigInteger.valueOf(900));
        assertEquals(userService.getById(user2.getId()).getBalance(), BigInteger.valueOf(100));   
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testMovementRaisesExceptionIfAmountSimilar(){
        Users user1 = userService.register("User 1", "User 1");
        Users user2 = userService.register("User 2", "User 2");
        
        userService.createReplenishment(user1, BigInteger.valueOf(1000));
        userService.createMoneyMovement(user1, user2, BigInteger.valueOf(1000));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testMovementRaisesExceptionIfAmountTooLarge(){
        Users user1 = userService.register("User 1", "User 1");
        Users user2 = userService.register("User 2", "User 2");
        
        userService.createReplenishment(user1, BigInteger.valueOf(1000));
        userService.createMoneyMovement(user1, user2, BigInteger.valueOf(10000));
    }
    
}
