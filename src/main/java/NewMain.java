
import models.Users;
import org.hibernate.Hibernate;
import org.hibernate.SessionFactory;
import userDAO.HibernateUserDAO;
import userDAO.UserDAO;
import util.HibernateUtil;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author stas
 */
public class NewMain {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
        
        sessionFactory.openSession();
        
        UserDAO userDao = new HibernateUserDAO(sessionFactory);
        
        userDao.add(new Users(null, "Stas", "Kaledin"));
        
        for(Users user : userDao.filterByFirstName("Stas")){
            System.out.println(user);
        }
        
        sessionFactory.getCurrentSession().close();
        sessionFactory.close();
    }
    
}
