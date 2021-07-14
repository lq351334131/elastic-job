package com.etocrm.util;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.etocrm.dao.db2.EmailDao;

@Component
public class SendEmailUtil {


    private static Logger logger= LoggerFactory.getLogger(SendEmailUtil.class);
    
    
    @Autowired
    private EmailDao emailDao;
    //发件人的邮箱和密码
//    public static String myEmailAccount = "alerts@etocrm.com";
//    public static String myEmailPassword = "QtQqjzpdSrCpC9GR";

    //发件人邮箱的 SMTP服务器地址
    public static String myEmailSMTPHost = "smtp.exmail.qq.com";

    //收件人邮箱


    //public static String receiveMailAccount = "shengjie612@126.com";


    private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public void emailMessage(Map<String, String> params){
    	String myEmailAccount=emailDao.getId(1).get("email");
    	String myEmailPassword=emailDao.getId(1).get("pwd");
        logger.info("邮件通知请求参数：邮件信息"+params.get("content")+"...收件人邮箱"+params.get("list")+"....时间"+simpleDateFormat.format(new Date()));
        // 1. 创建参数配置, 用于连接邮件服务器的参数配置
        Properties properties = new Properties();

        //使用的协议
        properties.setProperty("mail.transport.protocol","smtp");
        //SMTP服务器地址
        properties.setProperty("mail.smtp.host",myEmailSMTPHost);
        //需要请求认证
        properties.setProperty("mail.smtp.auth","true");
        properties.setProperty("mail.debug", "true");


        try {
            // 2. 根据配置创建会话对象, 用于和邮件服务器交互
            Session session = Session.getInstance(properties);
            session.setDebug(true);
            List<String> userList = new ArrayList<>();
            if (params.get("list").contains(",")){
                String[] split = params.get("list").split(",");
                userList = Arrays.asList(split);
            }else {
                userList=Arrays.asList(params.get("list"));
            }

            String receiveMailAccount;
            for (String o : userList) {
                 receiveMailAccount=  o;
                //3.创建一封邮件
                MimeMessage message = createMimeMessage(session,myEmailAccount,receiveMailAccount,params.get("content"));
                // 4. 根据 Session 获取邮件传输对象
                Transport transport = session.getTransport();
                // 5. 使用 邮箱账号 和 密码 连接邮件服务器, 这里认证的邮箱必须与 message 中的发件人邮箱一致, 否则报错
                transport.connect(myEmailAccount,myEmailPassword);
                // 6. 发送邮件, 发到所有的收件地址, message.getAllRecipients() 获取到的是在创建邮件对象时添加的所有收件人, 抄送人, 密送人
                transport.sendMessage(message,message.getAllRecipients());
                //7. 关闭连接
                transport.close();
            }
        } catch (Exception e) {
            logger.error("发送邮件失败："+e.getMessage());
        }
        //3.创建一封邮件
//        MimeMessage message = createMimeMessage(session,myEmailAccount,receiveMailAccount,content);

        // 4. 根据 Session 获取邮件传输对象
//        Transport transport = session.getTransport();

        // 5. 使用 邮箱账号 和 密码 连接邮件服务器, 这里认证的邮箱必须与 message 中的发件人邮箱一致, 否则报错
//        transport.connect(myEmailAccount,myEmailPassword);

        // 6. 发送邮件, 发到所有的收件地址, message.getAllRecipients() 获取到的是在创建邮件对象时添加的所有收件人, 抄送人, 密送人
//        transport.sendMessage(message,message.getAllRecipients());

        //7. 关闭连接
//        transport.close();

    }

    /**
     * 创建一个邮件
     * @param session
     * @param myEmailAccount
     * @param receiveMailAccount
     * @param content
     * @return
     */
    private static MimeMessage createMimeMessage(Session session, String myEmailAccount, String receiveMailAccount,String content) throws UnsupportedEncodingException, MessagingException {

        // 1. 创建一封邮件
        MimeMessage message = new MimeMessage(session);

        // 2. From: 发件人
        message.setFrom(new InternetAddress(myEmailAccount, "匿名", "UTF-8"));

        // 3. To: 收件人（可以增加多个收件人、抄送、密送）
        //message.addRecipient(MimeMessage.RecipientType.CC, new InternetAddress(myEmailAccount, "匿名", "UTF-8"));
        message.addRecipient(MimeMessage.RecipientType.TO, new InternetAddress(receiveMailAccount, "匿名", "UTF-8"));

        // 4. Subject: 邮件主题
        message.setSubject("job的异常通知", "UTF-8");

        // 5. Content: 邮件正文（可以使用html标签）
        message.setContent(content, "text/html;charset=UTF-8");
        // 6. 设置发件时间
        message.setSentDate(new Date());

        // 7. 保存设置
        message.saveChanges();

        return message;

    }


}
