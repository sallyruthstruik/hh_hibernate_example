/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package transactionsDAO;

import java.util.List;
import models.Transactions;

/**
 *
 * @author stas
 */
public interface transactionsDAO {

    public void add(Transactions t);
    public void update(Transactions t);
    public void delete(Transactions t);
    
    public Transactions getById(long id);
    public List<Transactions> getAll();
    public List<Transactions> filterByUserId(long user_id);
    public Long getBalance(long user_id);
    
}
