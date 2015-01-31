/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package userService;

import java.math.BigInteger;
import java.util.List;
import models.Transactions;
import models.Users;

/**
 *
 * @author stas
 */
public interface UserService {

    public Users register(String first_name, String last_name);
    public Users getById(long id);
    public Users update(java.lang.Long user_id, String new_first_name, String new_last_name);
    
    public List<Users> filterByFirstName(String name);
    public List<Users> filterByLastName(String name);
   
    /*
    * Метод используется для перечисления денег от одного юзера другому.
    * Балансы юзеров изменяются автоматически
    */
    public void createMoneyMovement(Users user1, Users user2, BigInteger amount);
    public Transactions createReplenishment(Users user, BigInteger amount);
}
