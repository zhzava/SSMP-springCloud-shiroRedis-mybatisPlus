package com.bosi.itms.shiro.Base;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * service层公用方法接口
 * 2013-4-13
 */

public interface IBaseDao <K,V> {

    public boolean add(Object entity) throws Exception;

    public boolean update(Object entity) throws Exception;

    public boolean delete(long id) throws Exception ;

    public boolean delete(String classMethod, String id) throws Exception ;

    public boolean delete(String classMethod, Object entity) throws Exception;

    public boolean deleteBatchById(String classMethod, String[] ids)throws Exception ;

    public Object get(String classMethod, long id);

    public Object get(String classMethod, String id);

    public List getAll(String classMethod, Object entity);

    public List getBatchById(String classMethod, String[] ids) ;

    public int getAllCount(String classMethod);

    public int getAllCount(String classMethod, Object entity);

    //public PageList getPageList(String statementName,String statementAll, Object parameterObject ,int pageNum, int pageSize);

    //public boolean addLogMarkSigin(String logtype,String source,String cz) throws Exception;

    //public boolean isValidate(User user, String menusString);

    public void message(HttpServletResponse response);

    public void writeForJSONP(HttpServletRequest request, HttpServletResponse response, int num, String str);

    public void writeForJSONPGBK(HttpServletRequest request, HttpServletResponse response, int num, String str);

    //public boolean addLogsin(String type, String level,String content,String f_id,String f_pid );

    //public boolean operateLog(String type, String level,String content,String f_id,String f_pid) throws Exception;
    //违法子系统专用日志处理方法
    //public boolean illegalOperateLog(String type, String level,String content,String f_id,String f_pid) throws Exception;

    /**
     * 实现数据的远程验证
     * @param classMethod
     * @param entity
     * @return
     * @throws Exception
     */
    public boolean checkData(String classMethod, Object entity)throws Exception;

}
