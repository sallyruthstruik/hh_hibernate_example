
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.junit.BeforeClass;
import org.postgresql.ds.PGSimpleDataSource;
import org.springframework.jdbc.core.JdbcTemplate;
import transactionService.SpringTransactionService;
import userService.SpringUserService;
import userService.UserService;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author stas
 */
public class TestSpringTransactionsService extends TestHibernateTransactionsService{
    
    private static PGSimpleDataSource dataSource;

    public static PGSimpleDataSource getDataSource() {
        return dataSource;
    }
       
    public TestSpringTransactionsService() {
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
        trService = new SpringTransactionService(dataSource);
        userService = new SpringUserService(dataSource);
        
        new JdbcTemplate(dataSource).update("DELETE FROM transactions; DELETE FROM users;");
    }
    
    public static void main(String[] args) {
        setUpClass();
        UserService userService = new SpringUserService(dataSource);
    }  
}
