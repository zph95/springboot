package com.zph.programmer.springboot.service;

import com.zph.programmer.springboot.SpringBootApplicationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class MailServiceTest extends SpringBootApplicationTest {

    /**
     * 注入发送邮件的接口
     */
    @Autowired
    private MailService mailService;

    /**
     * 测试发送文本邮件
     */
    @Test
    public void sendmail() {
        mailService.sendSimpleMail("1113955004@qq.com", "主题：你好文本邮件", "内容：发送文本邮件");
    }

    @Test
    public void sendmailHtml() {
        mailService.sendHtmlMail("1113955004@qq.com", "主题：你好html网页邮件", "<h2>内容：第一封html网页邮件</h2>");
    }

    @Test
    void sendAttachmentsMail() {
    }
}