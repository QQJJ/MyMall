package com.mall.austen.controller;

import com.alipay.api.AlipayApiException;
import com.mall.austen.entity.ResultVO;
import com.mall.austen.service.PayService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

/**
 * @author qiaoshiyong@bshf360.com
 * @since 2018/7/10 17:31
 */
@Controller
@RequestMapping("pay")
public class PayController {

    private static final Logger logger = LoggerFactory.getLogger(PayController.class);

    @Autowired
    private PayService payService;

    @RequestMapping("pay/{id}")
    public void pay(@PathVariable Integer id, HttpServletResponse httpResponse){
        payService.pay(id, httpResponse);
    }

    @RequestMapping("wapPay/{id}")
    public void wapPay(@PathVariable Integer id, HttpServletResponse httpResponse) throws IOException, AlipayApiException {
        payService.wapPay(id, httpResponse);
    }

    @PostMapping("refund/{id}")
    @ResponseBody
    public ResultVO refund(@PathVariable Integer id) throws AlipayApiException {
        logger.info("---  进入支付宝退款流程  ---");
        return payService.refund(id);
    }

    @PostMapping("alipayQueryRefund/{id}")
    @ResponseBody
    public ResultVO queryRefund(@PathVariable Integer id) throws AlipayApiException {
        logger.info("---  进入支付宝退款查询流程  ---");
        return payService.queryRefund(id);
    }

    @RequestMapping("alipayQuery/{id}")
    @ResponseBody
    public ResultVO alipayQuery(@PathVariable Integer id) throws AlipayApiException {
        logger.info("---  进入支付宝交易查询流程  ---");
        return payService.alipayQuery(id);
    }

    @RequestMapping("alipayDataBillDownLoad")
    @ResponseBody
    public ResultVO alipayDataBillDownLoad() throws AlipayApiException {
        logger.info("---  进入支付宝对账单查询流程  ---");
        return payService.alipayBillQuery();
    }

    @RequestMapping("close/{id}")
    @ResponseBody
    public ResultVO close(@PathVariable Integer id) throws AlipayApiException {
        logger.info("---  进入支付宝订单关闭流程  ---");
        return payService.alipayClose(id);
    }

    @RequestMapping("alipayNotify")
    @ResponseBody
    public String alipayNotify(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException, AlipayApiException {
        logger.info("---  进入支付宝异步通知  ---");
        payService.alipayNotify(request, response);
        logger.info("---  支付宝异步通知完成  ---");
        return "success";
    }

    @RequestMapping("alipayReturn")
    public String alipayReturn(Model model ,HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException, AlipayApiException {
        logger.info("---  进入支付宝同步通知  ---");
        boolean flag = payService.alipayReturn(request, response);
        logger.info("---  支付宝同步结果:  " + flag);
        logger.info("---  支付宝同步通知完成  ---");
        model.addAttribute("PayFlag", flag);
        return "payResultPage";
    }

    ////////////////////////////ALIPAY APP支付///////////////////////////////////////

    @RequestMapping("appPay/{id}")
    public void appPay(@PathVariable Integer id, HttpServletResponse httpResponse) throws AlipayApiException {
        payService.appPay(id, httpResponse);
    }



}
