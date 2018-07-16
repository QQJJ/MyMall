package com.mall.austen.service;

import com.alipay.api.AlipayApiException;
import com.mall.austen.entity.ResultVO;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

/**
 * @author qiaoshiyong@bshf360.com
 * @since 2018/7/10 17:32
 */
public interface PayService {

    void pay(Integer id, HttpServletResponse httpResponse);

    void alipayNotify(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException, AlipayApiException;

    boolean alipayReturn(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException, AlipayApiException;

    ResultVO refund(Integer id) throws AlipayApiException;

    ResultVO queryRefund(Integer id) throws AlipayApiException;

    ResultVO alipayQuery(Integer id) throws AlipayApiException;

    ResultVO alipayClose(Integer id) throws AlipayApiException;

    ResultVO alipayBillQuery() throws AlipayApiException;

    void wapPay(Integer id, HttpServletResponse httpResponse) throws AlipayApiException, IOException;

    void appPay(Integer id, HttpServletResponse httpResponse) throws AlipayApiException;
}
