package jp.technopro.engneering.kyushu.softgr2.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import jp.technopro.engneering.kyushu.softgr2.bean.PersonBean;

public class PersonDAO {
    private static final String SELECT = "select * from list";
    private static final String CONDITIONAL_SELECT
                       = "select * from list where id like ? and name like ? and email like ?";
    private static final String NAME_SELECT = "select * from list where name=?";
    private static final String INSERT = "insert into item values(?, ?, ?, ?)";
    private static final String UPDATE
                                         = "update list set id=?, name=?, password=? email=? where id=?";
    private static final String DELETE = "delete from list where id=?";

    private DataSource source;

    public PersonDAO() throws NamingException {
        InitialContext context = new InitialContext();
        source = (DataSource) context.lookup("java:comp/env/jdbc/datasource");
    }

    //一覧取得
    public List<PersonBean> getPersonListAll() throws SQLException {

        List<PersonBean> itemList = new ArrayList<PersonBean> ();
        Connection connection = source.getConnection();

        try {
            PreparedStatement statement = connection.prepareStatement(SELECT);
            ResultSet result = statement.executeQuery();

            while (result.next()) {
            	PersonBean item = new PersonBean();
                item.setId(result.getString("id"));
                item.setName(result.getString("name"));
                item.setPassword(result.getString("password"));
                item.setEmail(result.getString("email"));
                itemList.add(item);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                connection.close();
            }
        }
        return itemList;
    }

    //条件指定で一覧取得
    public List<PersonBean> getPersonListConditionally(String id, String name, String email)
                                                                               throws SQLException {

        List<PersonBean> itemList = new ArrayList<PersonBean> ();
        Connection connection = source.getConnection();

        try {
            PreparedStatement statement
                                  = connection.prepareStatement(CONDITIONAL_SELECT);
            statement.setString(1, "%" + id + "%");
            statement.setString(2, "%" + name + "%");
            statement.setString(3, "%" + email + "%");
            ResultSet result = statement.executeQuery();

            while (result.next()) {
            	PersonBean item = new PersonBean();
                item.setId(result.getString("id"));
                item.setName(result.getString("name"));
                item.setPassword(result.getString("password"));
                item.setEmail(result.getString("email"));
                itemList.add(item);
            }
            statement.close();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                connection.close();
            }
        }
        return itemList;
    }

    public PersonBean getPersonByName(String name) throws SQLException {

    	PersonBean person = new PersonBean();
        Connection connection = source.getConnection();

        try {
            PreparedStatement statement
                                  = connection.prepareStatement(NAME_SELECT);
            statement.setString(1, name);
            ResultSet result = statement.executeQuery();

            if (result.next()) {
            	person.setId(result.getString("id"));
            	person.setName(result.getString("name"));
            	person.setPassword(result.getString("password"));
            	person.setEmail(result.getString("email"));
            } else {
            	person = null;
            }
            statement.close();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                connection.close();
            }
        }
        return person;
    }

    public void insertPerson(String id, String name, String password, String email) throws SQLException {

        Connection connection = source.getConnection();

        try {
            PreparedStatement statement = connection.prepareStatement(INSERT);
            statement.setString(1, id);
            statement.setString(2, name);
            statement.setString(3, password);
            statement.setString(4, email);
            statement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                connection.close();
            }
        }
    }

    public void updatePerson(String id, String name, String password, String email, String oldId)
                                                                               throws SQLException {

        Connection connection = source.getConnection();

        try {
            PreparedStatement statement = connection.prepareStatement(UPDATE);
            statement.setString(1, id);
            statement.setString(2, name);
            statement.setString(3, password);
            statement.setString(4, email);
            statement.setString(5, oldId);
            statement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                connection.close();
            }
        }
    }

    public void deletePerson(String id) throws SQLException {

        Connection connection = source.getConnection();

        try {
            PreparedStatement statement = connection.prepareStatement(DELETE);
            statement.setString(1, id);
            statement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                connection.close();
            }
        }
    }

}