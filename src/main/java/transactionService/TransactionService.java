/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package transactionService;

import java.util.List;
import models.Transactions;

/**
 *
 * @author stas
 */
public interface TransactionService {
    public Transactions getById(Long id);
    public List<Transactions> getAll();
    public List<Transactions> filterByUserId(Long user_id);
    public Long getBalance(long user_id);
}
