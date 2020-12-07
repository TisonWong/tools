package com.ambit.app.task;

import com.sun.mail.util.MailSSLSocketFactory;
import javafx.concurrent.Task;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.util.Properties;

public class SendEmailTask extends Task<Boolean> {

    /**
     * 邮件主题
     */
    private String subject;


    @Override
    protected Boolean call() throws Exception {
        Properties prop = new Properties();
        prop.setProperty("mail.host", "smtp.u328.com");  //设置QQ邮件服务器
        prop.setProperty("mail.transport.protocol", "smtp"); // 邮件发送协议
        prop.setProperty("mail.smtp.auth", "true"); // 需要验证用户名密码
        prop.setProperty("mail.smtp.port", "1025");// 端口号
        prop.setProperty("mail.smtp.ssl.enable", "false");// 设置是否使用ssl安全连接 ---一般都使用
        prop.setProperty("mail.debug", "true");// 设置是否显示debug信息 true 会在控制台显示相关信息

        //1、创建定义整个应用程序所需的环境信息的 Session 对象
        Session session = Session.getDefaultInstance(prop, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                //传入发件人的姓名和授权码
                return new PasswordAuthentication("smtp@ambit-geospatial.com.hk","ntS5hAzU");
            }
        });

        //2、通过session获取transport对象
        Transport transport = session.getTransport();

        //3、通过transport对象邮箱用户名和授权码连接邮箱服务器
        transport.connect("smtp.u328.com","smtp@ambit-geospatial.com.hk","ntS5hAzU");

        //4、创建邮件,传入session对象
        MimeMessage mimeMessage = complexEmail(session);

        //5、发送邮件
        transport.sendMessage(mimeMessage,mimeMessage.getAllRecipients());

        //6、关闭连接
        transport.close();
        return true;
    }

    private MimeMessage complexEmail(Session session) throws MessagingException {
        //消息的固定信息
        MimeMessage mimeMessage = new MimeMessage(session);

        //发件人
        mimeMessage.setFrom(new InternetAddress("2020geosummit@ambit-geospatial.com.hk"));
        //收件人
        mimeMessage.setRecipient(Message.RecipientType.TO,new InternetAddress("ben.wong@ambit-geospatial.com.hk"));
        //邮件标题
        mimeMessage.setSubject(this.subject);

        //邮件内容
        //准备图片数据
//        MimeBodyPart image = new MimeBodyPart();
//        DataHandler handler = new DataHandler(new FileDataSource("E:\\IdeaProjects\\WebEmail\\resources\\测试图片.png"));
//        image.setDataHandler(handler);
//        image.setContentID("test.png"); //设置图片id

        //准备文本
        MimeBodyPart text = new MimeBodyPart();
        text.setContent("这是一段文本<img src='cid:test.png'>","text/html;charset=utf-8");

        //附件
        MimeBodyPart appendix = new MimeBodyPart();
        appendix.setDataHandler(new DataHandler(new FileDataSource("D:\\WebSoftwareDevelopment\\VSCodeProjects\\Summit-2020\\email\\Guideline of Joining 2020 Hong Kong 3D Geospatial Summit with Cisco Webex (Audience).pdf")));
        appendix.setFileName("Guideline of Joining 2020 Hong Kong 3D Geospatial Summit with Cisco Webex (Audience).pdf");

        //拼装邮件正文
        MimeMultipart mimeMultipart = new MimeMultipart();
//        mimeMultipart.addBodyPart(image);
        mimeMultipart.addBodyPart(text);
        mimeMultipart.setSubType("related");//文本和图片内嵌成功

        //将拼装好的正文内容设置为主体
        MimeBodyPart contentText = new MimeBodyPart();
        contentText.setContent(mimeMultipart);

        //拼接附件
        MimeMultipart allFile = new MimeMultipart();
        allFile.addBodyPart(appendix);//附件
        allFile.addBodyPart(contentText);//正文
        allFile.setSubType("mixed"); //正文和附件都存在邮件中，所有类型设置为mixed


        //放到Message消息中
        mimeMessage.setContent(allFile);
        mimeMessage.saveChanges();//保存修改

        return mimeMessage;
    }
}
