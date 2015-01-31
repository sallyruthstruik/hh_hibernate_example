/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package transactionsDAO;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import javax.sql.DataSource;
import models.Transactions;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import java.util.Map;
/**
 *
 * @author stas
 */
import org.springframework.jdbc.core.RowMapper;
import userDAO.SpringUserDao;
import userDAO.UserDAO;
public class SpringTransactionsDAO implements transactionsDAO{
    
    private DataSource dataSource;
    private JdbcTemplate jdbcTemplate;
    private NamedParameterJdbcTemplate namedJdbcTemplate;
    private SimpleJdbcInsert jdbcInsert;
    
    private UserDAO userDao;
    
    public SpringTransactionsDAO(DataSource dataSource){
        this.dataSource = dataSource;
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        this.namedJdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate);
        this.jdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("transactions")
                .usingGeneratedKeyColumns("id");
        this.userDao = new SpringUserDao(dataSource);
    }
    
    private Map<String, Object> getParameters(Transactions t){
        Map<String, Object> params = new HashMap();
        
        Long user_id = null;
        if(t.getUser() != null)
            user_id = t.getUser().getId();
        
        params.put("amount", new BigDecimal(t.getAmount()));
        params.put("coment", t.getComment());
        params.put("created", new Date());
        params.put("user_id", user_id);
        params.put("id", t.getId());
        
        return params;
    }
    
    private class TransactionsMapper implements RowMapper<Transactions>{
        @Override
        public Transactions mapRow(ResultSet rs, int rowNum) throws SQLException {
            Transactions tr = new Transactions();
            
            tr.setAmount(rs.getBigDecimal("amount").toBigInteger());
            tr.setComment(rs.getString("comment"));
            tr.setUser(userDao.getById(rs.getLong("user_id")));
            
            return tr;
        }
        
    }
    
    @Override
    public void add(Transactions t) {
        long id = jdbcInsert.executeAndReturnKey(getParameters(t)).longValue();
        t.setId(id);
    }

    @Override
    public void update(Transactions t) {
        if(t.getId() == null)
            throw new IllegalArgumentException("Id must present");
        
        String SQL = "UPDATE transactions SET amount=:amount, comment=:comment, user_id=:user_id WHERE id=:id";
        
        namedJdbcTemplate.update(SQL, getParameters(t));
    }

    @Override
    public void delete(Transactions t) {
        String SQL = "DELETE FROM transactions WHERE id=:id";
        
        namedJdbcTemplate.update(SQL, getParameters(t));
    }

    @Override
    public Transactions getById(long id) {
        String SQL = "SELECT * FROM transactions WHERE id=:id";
        
        Map<String, Object> params = new HashMap();
        params.put("id", id);
        
        return namedJdbcTemplate.queryForObject(SQL, params, new TransactionsMapper());
    }

    @Override
    public List<Transactions> getAll() {
        return namedJdbcTemplate.query("SELECT * FROM transactions;", new HashMap<String, Object>(), new TransactionsMapper());
    }

    @Override
    public List<Transactions> filterByUserId(long user_id) {
        String SQL = "SELECT * FROM transactions WHERE user_id=:user_id";
        Map<String, Object> params = new HashMap();
        params.put("user_id", user_id);
        return namedJdbcTemplate.query(SQL, params, new TransactionsMapper());
    }

    @Override
    public Long getBalance(long user_id) {
        String SQL = "SELECT SUM(amount) FROM transactions WHERE user_id=:user_id";
        Map<String, Object> params = new HashMap();
        params.put("user_id", user_id);
        return namedJdbcTemplate.queryForLong(SQL, params);
    }
}
