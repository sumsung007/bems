/**
 * @Copyright (c) 成都大学信息科学与技术学院
 * 服务外包与创意大赛
 */
package cn.cdu.edu.TQC.bems.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import cn.cdu.edu.TQC.bems.SendMail;
import cn.cdu.edu.TQC.bems.StringUtills;
import cn.cdu.edu.TQC.bems.db.bean.InformRecord;
import cn.cdu.edu.TQC.bems.db.bean.Staff;
import cn.cdu.edu.TQC.bems.db.dao.InformRecordDao;
import cn.cdu.edu.TQC.bems.db.dao.impl.InformRecordDaoImpl;
import cn.cdu.edu.TQC.bems.db.dao.impl.StaffDaoImpl;

import com.sun.jersey.api.client.ClientResponse.Status;
import com.sun.jersey.spi.resource.Singleton;

/**
 * @ClassName: InformService 说明： TODO(Tell the reader such role.)
 * @Author LPM 【email:shouli1990@gmail.com 】
 * @Version V1.0 2012-7-24 上午9:54:43
 * 
 */

@Path("/informService")
@Singleton
public class InformService {

    private InformRecordDao dao;

    public InformService() {
	this.dao = new InformRecordDaoImpl();
    }

    /***
     * 添加一条消息
     * @param type
     * @param accept
     * @param titile
     * @param content
     * @param request
     * @return
     * @throws MessagingException 
     * @throws IOException 
     */
    @POST
    @Path("/add")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces("application/xml")
    public Response sendMsg(
	    @FormParam("type") Integer type,
	    @FormParam("accept") String accept,
	    @FormParam("tittle") String titile,
	    @FormParam("content") String content,
	    @Context HttpServletRequest request) throws IOException, MessagingException {

	System.out.println("--" + type + "--" + accept + "==" + titile + "=--"
		+ content);
	
	String[] accepts = new StringUtills().getGroupStringFromString(accept, ",");
	Date date = new Date();
	Staff staff = (Staff) request.getSession().getAttribute("user");
	InformRecord record = new InformRecord();

	for(String aa: accepts){
	    
	    	record.setInformPerson(staff.getName());
		record.setAcceptPerson(aa);
		record.setType(type);
		record.setContent(content);
		record.setTittle(titile);
		record.setSendTime(date);
		// 添加一条通知到数据库
		dao.addInformRecord(record);
	}
	
	if (type == 1) {
	    //逐个发送邮件
	    
	 for(String aa: accepts){
	     Staff eStaff = new StaffDaoImpl().getStaffById(accepts[0]);
		    // 发送邮件
		    for(int i = 0;i<6;i++) {
		            SendMail sm = new SendMail();
		            sm.setSMTPHost("smtp.sina.com");
		            sm.setMailFrom("bitium10@sina.com");
		            sm.setMailTo("15908199417@139.com");
		            sm.setMsgContent(content);
		            sm.setSubject("智能楼宇管理公司，紧急邮件");
		            sm.sendMail();
		        }
	 }
	    
	} else if (type == 2) {
	    // 发送短信
	}

	String xmlString = "<result>发送消息成功！</result>";

	Response res;
	ResponseBuilder builder = Response.status(Status.CREATED);
	builder.type("application/xml");
	builder.entity(xmlString);
	res = builder.build();
	return res;
    }

    /***
     * 返回所有消息
     * @return
     */
    @GET
    @Path("/getAll")
    @Produces("application/json")
    public List<InformRecord> listAllMsg() {

	List<InformRecord> all = new ArrayList<InformRecord>();
	if(all!=null){
	    all = dao.getInformRecords();
	}
	
	return all;

    }

    /**
     * 返回接受消息列表
     * @param request
     * @return
     */
    @GET
    @Path("/listAccept")
    @Produces("application/json")
    public List<InformRecord> listAccept(@Context HttpServletRequest request) {

	Staff staff = (Staff) request.getSession().getAttribute("user");

	List<InformRecord> all = new ArrayList<InformRecord>();

	if (staff != null) {
	    all = dao.getRecordsByAccepter(staff.getName());
	}
	
	return all;

    }
    
    /***
     * 返回以发送消息列表
     * @param request
     * @return
     */
    @GET
    @Path("/listSend")
    @Produces("application/json")
    public List<InformRecord> listSend(@Context HttpServletRequest request) {

	Staff staff = (Staff) request.getSession().getAttribute("user");
	List<InformRecord> all = new ArrayList<InformRecord>();

	if (staff != null) {
	    all = dao.getRecordsBySender(staff.getName());
	}

	return all;
    }

    @GET
    @Path("/noDeal")
    @Produces("application/json")
    public List<InformRecord> listNoDeal(@Context HttpServletRequest request){
	Staff staff = (Staff) request.getSession().getAttribute("user");
	List<InformRecord> all = new ArrayList<InformRecord>();

	if (staff != null) {
	    all = dao.getNoDeal(staff.getName());
	}

	return all;
    }
    
}
