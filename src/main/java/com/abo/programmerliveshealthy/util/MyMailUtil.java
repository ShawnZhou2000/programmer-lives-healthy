package com.abo.programmerliveshealthy.util;

import cn.hutool.extra.mail.MailAccount;
import cn.hutool.extra.mail.MailUtil;
import com.abo.programmerliveshealthy.entities.EmailCode;
import com.abo.programmerliveshealthy.entities.User;
import com.abo.programmerliveshealthy.service.EmailCodeService;
import com.abo.programmerliveshealthy.service.UserService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Random;

/**
 * @author abo
 * @date 2020/7/3 15:33
 * @remarks
 **/
public class MyMailUtil {

    private static final String VERIFICATION_PASSWORD = "password";
    private static final String HOST = "smtp.qq.com";
    private static final String USER = "QQ号";
    private static final Integer PORT = 465;
    private static final String FROM = "发邮件的邮箱";
    private static final int CODE_LENGTH = 6;

    public void sendMail(String targetUser, String code){
        EmailCode emailCode = new EmailCode();
        emailCode.setGmtCreate(System.currentTimeMillis());
        emailCode.setCode(code);
        emailCode.setIsUsed(0);
        MailAccount account = new MailAccount();
        account.setHost(HOST);
        account.setUser(USER);
        account.setPass(VERIFICATION_PASSWORD);
        account.setPort(PORT);
        account.setFrom(FROM);
        account.setStarttlsEnable(true);
        account.setSslEnable(true);
        account.setSocketFactoryClass("javax.net.ssl.SSLSocketFactory");
        account.setSocketFactoryFallback(true);
        account.setSocketFactoryPort(465);
        String subject = "Programmer lives healthy官方邮件";
        String content = "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\"><html xmlns=\"http://www.w3.org/1999/xhtml\"><head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\" /><title>确认邮箱</title><meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\" /></head><body style=\"margin: 0; padding: 0;\"><table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" style=\" background-color: #0061f2;background-image: linear-gradient(135deg, #0061f2 0%, rgba(105, 0, 199, 0.8) 100%);\"><tr><td><table align=\"center\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"700\" style=\"border-collapse: collapse;margin-top:50px\"><tr style=\"height:80px;\"><td style=\"font-size:25px;line-height:45px;color: white;\">Programmer lives healthy official</td></tr><tr><td style=\"color:#ffffff;\"><div style=\"margin:15px auto;text-align:center;font-size:30px;font-weight:700;\">您的验证码是<br /><br /><div style=\"border:3px solid #ffffff;\">\n" +
                code + "\n" +
                "</div><br /><br />请将它输入到页面中</div><br />&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<br /><br /><br /></td></tr><tr><td style=\"border-top:3px solid #ffffff;\"><table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" style=\"margin-left:5px;margin-top:15px;margin-bottom:50px;color:#ffffff;\"><tr><td style=\"width:150px;\"><img src=\"https://s1.ax1x.com/2020/07/04/NxCmLV.png\" style=\"width:130px;height:170px;\"></td><td style=\"vertical-align: top;\"><strong style=\"font-size:25px;line-height:45px;\">Contact us</strong><br><strong style=\"font-size:18px;line-height:25px;font-weight:400;\">加入我们，在日常打卡中锻炼身体，提升自己</strong><br><strong style=\"font-size:18px;line-height:25px;font-weight:400;\">plh用户QQ群：12345678</strong><br><strong style=\"font-size:18px;line-height:25px;font-weight:400;\">官网网址：plh.helloworld.top</strong><br></td></tr></table></td></tr></table></td></tr></table></body></html>\n";
        MailUtil.send(account, targetUser, subject, content, true);

    }

    public String generateVerificationCode(){
        Random random = new Random();
        String ret = "";
        for(int i=0;i<CODE_LENGTH;i++){
            Integer number = random.nextInt(10);
            ret += number.toString();
        }
        return ret;
    }

}
