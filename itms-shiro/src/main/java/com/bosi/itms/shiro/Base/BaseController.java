package com.bosi.itms.shiro.Base;

/**
 * Created by zhz on 2017/9/22.
 */
public class BaseController {
    /*public PageInfo getPageInfoWithJQ(HttpServletRequest request) {
        //获取请求中的起始记录数
        int page = Integer.parseInt(request.getParameter("page") == null ? "0" : request.getParameter("page"));
        //获取每页中数据的数量
        int pageSize = Integer.parseInt(request.getParameter("pageSize") == null ? "10" : request.getParameter("pageSize"));
        page = (page-1)*pageSize;
        return new PageInfo();
    }

    public void writeForJSONP(HttpServletRequest request, HttpServletResponse response, int num, String str){
        try {
            // 指定允许其他域名访问
            response.addHeader("Access-Control-Allow-Origin","*");
            // 响应类型
            response.addHeader("Access-Control-Allow-Methods","POST");
            // 响应头设置
            response.addHeader("Access-Control-Allow-Headers","x-requested-with,content-type");
            String jsoncallback = request.getParameter("jsoncallback");
            response.setCharacterEncoding("utf-8");
            PrintWriter writer;
            writer = response.getWriter();
            String FAIL ="FAIL",SUCCESS="SUCCESS";
            //      String message = num == 1 ? "{\"result\":\"" + str +"\",\"resultType\":\""+FinalDictionary.FAIL+"\"}" : "{\"result\":"+str+",\"resultType\":\""+FinalDictionary.SUCCESS+"\"}";
            String message = num == 1 ? "{\"result\":\"" + str +"\",\"resultType\":\""+FAIL+"\"}" : "{\"result\":"+str+",\"resultType\":\""+SUCCESS+"\"}";
            System.out.println("------"+request.getServletPath());
            String path = request.getServletPath();
            String logContent = num == 1 ? "操作失败" : "操作成功";

            //添加到日志表
            //String frmId = path.substring(getCharacterPosition(path,1)+1, getCharacterPosition(path,2));
            //String actionId = path.substring(getCharacterPosition(path,2)+1,path.length());
            //User user = getLoginUser(request);
            //com.bosi.common.log.SysLogFactory.getInstance().log(frmId, "API", actionId, "1", logContent, getIpAddress(request),user.getUser_Id());
            //message = jsoncallback + "(" + message + ")";
            System.out.println("jsoncallback >> "+message);
            writer.print(message);
            writer.flush();
            writer.close();
        }catch (Exception e){
            CommonUtil.log(e);
        }
    }

    public User getLoginUser(HttpServletRequest request){
        User user = new User();
        try {
            String username = (String)request.getSession().getAttribute("username");
            if(!StringUtils.hasText(username)){username = "admin";};
            List<Map> result = new ArrayList();
            //result = sqlSessionTemplate.getAll("","");
            if(result.size()>0) {
                user = (User) ApiUtils.mapToObject(result.get(0), User.class);
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return user;
    }

    public Map<String, Object> setLike(Map<String, String> map){
        Map<String, Object> result = new HashMap<String, Object>();
        if(null != map){
            Set<String> set = map.keySet();
            for (String key : set) {
                String value = map.get(key);
                if(key.contains("_like") && StringUtils.hasText(value)){
                    result.put(key, "%" + value + "%");
                }else if(key.contains(".time") && StringUtils.hasText(value)){  //时间类型
                    if(value.trim().length()>10){
                        //匹配模式为 yyyy-MM-dd HH:mm:ss
                        result.put(key, new Timestamp(DateUtils.convertStringToDate("yyyy-MM-dd HH:mm:ss", value).getTime()));
                    }else{
                        //匹配模式为：yyyy-MM-dd
                        if(key.trim().contains(".end")){
                            //一天中的傍晚时间   23:59:59
                            value = value + " 23:59:59";
                            result.put(key, new Timestamp(DateUtils.convertStringToDate("yyyy-MM-dd HH:mm:ss", value).getTime()));
                        }else{
                            //一天中的凌晨时间   00:00:00
                            result.put(key, new Timestamp(DateUtils.convertStringToDate("yyyy-MM-dd", value).getTime()));
                        }
                    }
                }else if(key.contains(".int") && StringUtils.hasText(value)){ //int类型
                    result.put(key, Integer.parseInt(value));
                }else {
                    result.put(key, value);
                }
            }
        }
        return result;
    }*/
}
