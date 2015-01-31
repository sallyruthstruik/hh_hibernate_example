/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package userDAO;

import java.util.List;
import models.Users;

/**
 *
 * @author stas
 */
public interface UserDAO {
    
    Users getById(java.lang.Long id);
    List<Users> filterByFirstName(String name);
    List<Users> filterByLastName(String name);
    
    
    void add(Users user);
    void delete(Users user);
    void update(Users user);
}

