package model.dao;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import model.dao.util.DBHelper;
import model.dao.util.RowMapper;

public class GenericDAO<T> {
	private final RowMapper<T> rowMapper;

    public GenericDAO(RowMapper<T> rowMapper) {
        this.rowMapper = rowMapper;
    }

    public List<T> list(String sql, Object... params) {
        List<T> listData = new ArrayList<>();
        try {
            ResultSet rs = DBHelper.GetDBHelper().GetRecords(sql, params);
            while (rs.next()) {
                T obj = rowMapper.mapRow(rs);
                listData.add(obj);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            DBHelper.GetDBHelper().CloseConn();
        }
        return listData;
    }

    public T find(String sql, Object... params) {
        try {
            ResultSet rs = DBHelper.GetDBHelper().GetRecords(sql, params);
            if (rs.next()) {
                return rowMapper.mapRow(rs);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            DBHelper.GetDBHelper().CloseConn();
        }
        return null;
    }

    public Integer add(String sql, Object... params) {
        try {
            return DBHelper.GetDBHelper().addAndReturnId(sql, params);
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        } finally {
            DBHelper.GetDBHelper().CloseConn();
        }
    }

    public void updateOrDelete(String sql, Object... params) {
        try {
            DBHelper.GetDBHelper().ExecuteDB(sql, params);
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            DBHelper.GetDBHelper().CloseConn();
        }
    }
}
