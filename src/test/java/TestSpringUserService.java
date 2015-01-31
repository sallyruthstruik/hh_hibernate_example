/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import javax.sql.DataSource;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.jboss.logging.Logger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.postgresql.ds.PGSimpleDataSource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.StatementCreatorUtils;
import userService.SpringUserService;
import userService.UserService;

/**
 *
 * @author stas
 */
public class TestSpringUserService extends TestHibernateUserService{
    
    private static PGSimpleDataSource dataSource;

    public static PGSimpleDataSource getDataSource() {
        return dataSource;
    }
       
    public TestSpringUserService() {
    }
    
    @BeforeClass
    public static void setUpClass(){
        BasicConfigurator.configure();
//        LogManager.getLogger(JdbcTemplate.class).setLevel(Level.DEBUG);
//        LogManager.getLogger(StatementCreatorUtils.class).setLevel(Level.DEBUG);
        
        LogManager.getRootLogger().setLevel(Level.WARN);
        LogManager.getLogger("org.springframework.jdbc.core").setLevel(Level.DEBUG);
        
        dataSource = new PGSimpleDataSource();
        dataSource.setDatabaseName("hybernate_example_test");
        dataSource.setUser("postgres");
        dataSource.setPassword("1");
    }

    @Override
    public void setUp() {
        userService = new SpringUserService(dataSource);
    }
    
    public static void main(String[] args) {
        setUpClass();
        UserService userService = new SpringUserService(dataSource);
    }
}
