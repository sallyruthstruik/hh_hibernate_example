/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package userDAO;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import javax.sql.DataSource;
import models.Users;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import java.util.Map;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
/**
 *
 * @author stas
 */
public class SpringUserDao implements UserDAO{
    
    private DataSource dataSource;
    private JdbcTemplate jdbcTemplate;
    private NamedParameterJdbcTemplate namedJdbcTemplate;
    private SimpleJdbcInsert simpleJdbcInsert;
    
    private class UserMapper implements RowMapper<Users>{

        @Override
        public Users mapRow(ResultSet rs, int rowNum) throws SQLException {
            Users user = new Users();
            user.setId(rs.getLong("id"));
            user.setFirstName(rs.getString("first_name"));
            user.setLastName(rs.getString("last_name"));
            user.setCreated(rs.getDate("created"));
            user.setBalance(rs.getBigDecimal("balance").toBigInteger());
            return user;
        }
        
    }
    public SpringUserDao(DataSource dataSource){
        this.dataSource = dataSource;
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        this.namedJdbcTemplate = new NamedParameterJdbcTemplate(this.jdbcTemplate);
        this.simpleJdbcInsert = new SimpleJdbcInsert(this.jdbcTemplate)
                .withTableName("users")
                .usingGeneratedKeyColumns("id");
    }
    
    @Override
    public Users getById(Long id) {
        String SQL = "SELECT * FROM users WHERE id = :id";
        
        Map<String, Long> params = new HashMap(); 
        
        params.put("id", id);
        
        return namedJdbcTemplate.queryForObject(SQL, params, new UserMapper());
        
    }

    @Override
    public List<Users> filterByFirstName(String name) {
        
        String SQL = "SELECT * FROM users WHERE first_name = :name";
        
        Map<String, Object> params = new HashMap<>();
        params.put("name", name);
        
        return namedJdbcTemplate
                .query(SQL, params, new UserMapper());
        
    }

    @Override
    public List<Users> filterByLastName(String name) {
        String SQL = "SELECT * FROM users WHERE last_name = :name";
        
        Map<String, Object> params = new HashMap<>();
        params.put("name", name);
        
        return namedJdbcTemplate
                .query(SQL, params, new UserMapper());
    }

    @Override
    public void add(Users user) {
        Map<String, Object> params = getParams(user);
        Number newKey = simpleJdbcInsert.executeAndReturnKey(params);
        user.setId(newKey.longValue());
    }

    @Override
    public void delete(Users user) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    private Map<String, Object> getParams(Users user){
        Map<String, Object> params= new HashMap();
        
        params.put("first_name", user.getFirstName());
        params.put("last_name", user.getLastName());
        params.put("balance", new BigDecimal(user.getBalance()));
        params.put("id", user.getId());
        params.put("created", new Date());
        return params;
    }
    
    @Override
    public void update(final Users user) {
        if(user.getId() == null)
            throw new IllegalArgumentException("User id must present");
        String SQL = "UPDATE users SET first_name=:first_name, last_name=:last_name, balance=:balance WHERE id=:id";
        
        Map<String, Object> params = getParams(user);
        
//        namedJdbcTemplate.execute(SQL, params, new PreparedStatementCallback<Users>() {
//            @Override
//            public Users doInPreparedStatement(PreparedStatement ps) throws SQLException, DataAccessException {
//                System.out.println(ps);
//                return user;
//            }
//        });
        
        namedJdbcTemplate.update(SQL, params);
    }
    
    
}
