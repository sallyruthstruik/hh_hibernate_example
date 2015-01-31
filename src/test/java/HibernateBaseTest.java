/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import util.HibernateUtil;

/**
 *
 * @author stas
 */
public class HibernateBaseTest {
    
    protected static SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
    
    protected static Session session;
    
    public HibernateBaseTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
        
    }
    
    @AfterClass
    public static void tearDownClass() {
        
    }
    
    @Before
    public void setUp() {
        System.out.println("SetUp");
        session = sessionFactory.openSession();
        session.beginTransaction();
        session.createQuery("DELETE FROM Transactions").executeUpdate();
        session.createQuery("DELETE FROM Users").executeUpdate();
        session.getTransaction().commit();        
    }
    
    @After
    public void tearDown() {
        System.out.println("Tear down");
    }
    
    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    // @Test
    // public void hello() {}
}
