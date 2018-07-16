package com.mall.austen.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayTradeAppPayModel;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.*;
import com.alipay.api.response.AlipayDataDataserviceBillDownloadurlQueryResponse;
import com.alipay.api.response.AlipayTradeAppPayResponse;
import com.mall.austen.dao.OrderDAO;
import com.mall.austen.dao.OrderItemDAO;
import com.mall.austen.entity.Order;
import com.mall.austen.entity.OrderItem;
import com.mall.austen.entity.ResultVO;
import com.mall.austen.service.PayService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.*;


/**
 * @author qiaoshiyong@bshf360.com
 * @since 2018/7/10 17:32
 */
@Service
@Transactional
public class PayServiceImpl implements PayService {

    private static final Logger logger = LoggerFactory.getLogger(PayServiceImpl.class);

    @Value("${流程说明.appId}")
    private String appId;
    @Value("${流程说明.gatewayUrl}")
    private String gatewayUrl;
    @Value("${流程说明.merchantPrivateKey}")
    private String privateKey;
    @Value("${流程说明.format}")
    private String format;
    @Value("${流程说明.charset}")
    private String charset;
    @Value("${流程说明.alipayPublicKey}")
    private String publicKey;
    @Value("${流程说明.signType}")
    private String signType;
    @Value("${流程说明.returnUrl}")
    private String returnUrl;
    @Value("${流程说明.notifyUrl}")
    private String notifyUrl;
    @Value("${流程说明.sysServiceProviderId}")
    private String sysServiceProviderId;

    @Autowired
    private OrderDAO orderDAO;
    @Autowired
    private OrderItemDAO orderItemDAO;

    @Override
    public void pay(Integer id, HttpServletResponse httpResponse){

        // 获取订单
        Order order = orderDAO.getById(id);
        List<OrderItem> list = orderItemDAO.getByOrderId(order.getId());
        order.setItems(list);

        //获得初始化的AlipayClient
        AlipayClient alipayClient = new DefaultAlipayClient(gatewayUrl, appId, privateKey, format, charset, publicKey, signType);

        //设置请求参数
        AlipayTradePagePayRequest alipayRequest = new AlipayTradePagePayRequest();
        alipayRequest.setReturnUrl(returnUrl);
        alipayRequest.setNotifyUrl(notifyUrl);

        //商户订单号，商户网站订单系统中唯一订单号，必填
        String out_trade_no = order.getOrderNo();
        //付款金额，必填
        String total_amount = order.getAmount().toString();
        //订单名称，必填
        String subject = order.getItems().get(0).getItemName();
        //商品描述，可空
        String body = order.getItems().get(0).getItemName();

        JSONObject json = new JSONObject();
        json.put("out_trade_no", out_trade_no);
        json.put("total_amount", total_amount);
        json.put("subject", subject);
        json.put("body", body);
        json.put("product_code", "FAST_INSTANT_TRADE_PAY");
//        alipayRequest.setBizContent("{\"out_trade_no\":\""+ out_trade_no +"\","
//                + "\"total_amount\":\""+ total_amount +"\","
//                + "\"subject\":\""+ subject +"\","
//                + "\"body\":\""+ body +"\","
//                + "\"product_code\":\"FAST_INSTANT_TRADE_PAY\"}");

        //请求
        String form = "";
        try {
            form = alipayClient.pageExecute(alipayRequest).getBody();
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        logger.info("生成支付页面: " + form);

        httpResponse.setContentType("text/html;charset=" + charset);
        try {
            httpResponse.getWriter().write(form);//直接将完整的表单html输出到页面
            httpResponse.getWriter().flush();
            httpResponse.getWriter().close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public ResultVO refund(Integer id) throws AlipayApiException {
        ResultVO resultVO = new ResultVO();

        // 退款
        Order order = orderDAO.getById(id);
        if(Objects.isNull(order)){
            resultVO.setMsg("订单不存在");
            resultVO.setCode(0);
            return resultVO;
        }

        //获得初始化的AlipayClient
        AlipayClient alipayClient = new DefaultAlipayClient(gatewayUrl, appId, privateKey, format, charset, publicKey, signType);

        //设置请求参数
        AlipayTradeRefundRequest alipayRequest = new AlipayTradeRefundRequest();

        //商户订单号，商户网站订单系统中唯一订单号
        String out_trade_no = order.getOrderNo();
        //支付宝交易号    支付宝交易号与商户订单号二选一
        //String trade_no = order.getTradeNo();
        //需要退款的金额，该金额不能大于订单金额，必填
        String refund_amount = order.getAmount().toString();
        //退款的原因说明
        String refund_reason = "用户退款";
        //标识一次退款请求，同一笔交易多次退款需要保证唯一，如需部分退款，则此参数必传
        //String out_request_no = new String(request.getParameter("WIDTRout_request_no").getBytes("ISO-8859-1"),"UTF-8");

        alipayRequest.setBizContent("{\"out_trade_no\":\""+ out_trade_no +"\","
                + "\"refund_amount\":\""+ refund_amount +"\","
                + "\"refund_reason\":\""+ refund_reason +"\"}");

       /* alipayRequest.setBizContent("{\"out_trade_no\":\""+ out_trade_no +"\","
                + "\"trade_no\":\""+ trade_no +"\","
                + "\"refund_amount\":\""+ refund_amount +"\","
                + "\"refund_reason\":\""+ refund_reason +"\","
                + "\"out_request_no\":\""+ out_request_no +"\"}");*/

        //请求
        String result = alipayClient.execute(alipayRequest).getBody();
        if(StringUtils.isBlank(result)){
            resultVO.setMsg("退款请求失败");
            resultVO.setCode(0);
            return resultVO;
        }
        JSONObject alipayTradeRefundResponse = JSON.parseObject(result).getJSONObject("alipay_trade_refund_response");

        String code = (String) alipayTradeRefundResponse.get("code");
        String msg = (String) alipayTradeRefundResponse.get("msg");
        if(Objects.equals(code, "10000") && msg.toLowerCase().equals("success")){
            // 退款成功
            orderDAO.editStatus(order.getOrderNo(), 5);
            resultVO.setCode(1);
            return resultVO;
        }
        return resultVO;
    }

    @Override
    public ResultVO queryRefund(Integer id) throws AlipayApiException {

        ResultVO resultVO = new ResultVO();

        // 退款
        Order order = orderDAO.getById(id);
        if(Objects.isNull(order)){
            resultVO.setMsg("订单不存在");
            resultVO.setCode(0);
            return resultVO;
        }

        //获得初始化的AlipayClient
        AlipayClient alipayClient = new DefaultAlipayClient(gatewayUrl, appId, privateKey, format, charset, publicKey, signType);

        //设置请求参数
        AlipayTradeFastpayRefundQueryRequest alipayRequest = new AlipayTradeFastpayRefundQueryRequest();

        //商户订单号，商户网站订单系统中唯一订单号
        //String out_trade_no = new String(request.getParameter("WIDRQout_trade_no").getBytes("ISO-8859-1"),"UTF-8");
        //支付宝交易号    支付宝交易号 与商户订单号  二选一
        String trade_no = order.getTradeNo();
        //请求退款接口时，传入的退款请求号，如果在退款请求时未传入，则该值为创建交易时的外部交易号，必填
        String out_request_no = order.getOrderNo();

        alipayRequest.setBizContent("{\"trade_no\":\""+ trade_no +"\","+"\"out_request_no\":\""+ out_request_no +"\"}");

//        alipayRequest.setBizContent("{\"out_trade_no\":\""+ out_trade_no +"\","
//                +"\"trade_no\":\""+ trade_no +"\","
//                +"\"out_request_no\":\""+ out_request_no +"\"}");
        //请求
        String result = alipayClient.execute(alipayRequest).getBody();

        if(StringUtils.isBlank(result)){
           throw new RuntimeException("请求失败");
        }
        JSONObject alipayTradeRefundResponse = JSON.parseObject(result).getJSONObject("alipay_trade_fastpay_refund_query_response");

        String code = (String) alipayTradeRefundResponse.get("code");
        String msg = (String) alipayTradeRefundResponse.get("msg");
        if(Objects.equals(code, "10000") && msg.toLowerCase().equals("success")){
            // 查询成功
            String tradeNo = (String) alipayTradeRefundResponse.get("trade_no");
            logger.info("退款查询: " + tradeNo);
            resultVO.setCode(1);
            return resultVO;
        }
        return resultVO;
    }

    @Override
    public ResultVO alipayQuery(Integer id) throws AlipayApiException {
        ResultVO resultVO = new ResultVO();

        // 退款
        Order order = orderDAO.getById(id);
        if(Objects.isNull(order)){
            resultVO.setMsg("订单不存在");
            resultVO.setCode(0);
            return resultVO;
        }

        //获得初始化的AlipayClient
        AlipayClient alipayClient = new DefaultAlipayClient(gatewayUrl, appId, privateKey, format, charset, publicKey, signType);

        //设置请求参数
        AlipayTradeQueryRequest alipayRequest = new AlipayTradeQueryRequest();

        //商户订单号，商户网站订单系统中唯一订单号
        String out_trade_no = order.getOrderNo();
        //支付宝交易号 商户订单号 二选一设置
       // String trade_no = order.getTradeNo();
        JSONObject json = new JSONObject();
        json.put("out_trade_no", out_trade_no);
        alipayRequest.setBizContent(json.toString());
        //alipayRequest.setBizContent("{\"out_trade_no\":\""+ out_trade_no +"\","+"\"trade_no\":\""+ trade_no +"\"}");
        //alipayRequest.setBizContent("{\"out_trade_no\":\""+ out_trade_no +"\"}");

        //请求
        String result = alipayClient.execute(alipayRequest).getBody();
        if(StringUtils.isBlank(result)){
            throw new RuntimeException("请求失败");
        }
        JSONObject alipayTradeQueryResponse = JSON.parseObject(result).getJSONObject("alipay_trade_query_response");
        String code = (String) alipayTradeQueryResponse.get("code");
        String msg = (String) alipayTradeQueryResponse.get("msg");
        if(Objects.equals(code, "10000") && msg.toLowerCase().equals("success")){
            // 查询成功
            String tradeStatus = (String) alipayTradeQueryResponse.get("trade_status");
            logger.info("支付状态: " + tradeStatus);
            resultVO.setCode(1);
            return resultVO;
        }
        return null;
    }

    @Override
    public ResultVO alipayClose(Integer id) throws AlipayApiException {
        ResultVO resultVO = new ResultVO();
        Order order = orderDAO.getById(id);

        // 关闭支付宝交易
        //获得初始化的AlipayClient
        AlipayClient alipayClient = new DefaultAlipayClient(gatewayUrl, appId, privateKey, format, charset, publicKey, signType);

        //设置请求参数
        AlipayTradeCloseRequest alipayRequest = new AlipayTradeCloseRequest();
        //商户订单号，商户网站订单系统中唯一订单号
        String out_trade_no = order.getOrderNo();
        //支付宝交易号 二选一设置
        //String trade_no = new String(request.getParameter("WIDTCtrade_no").getBytes("ISO-8859-1"),"UTF-8");

        //alipayRequest.setBizContent("{\"out_trade_no\":\""+ out_trade_no +"\"," +"\"trade_no\":\""+ trade_no +"\"}");
        alipayRequest.setBizContent("{\"out_trade_no\":\""+ out_trade_no +"\"}");

        //请求
        String result = alipayClient.execute(alipayRequest).getBody();
        if(StringUtils.isBlank(result)){
            throw new RuntimeException("请求失败");
        }
        JSONObject alipayTradeCloseResponse = JSON.parseObject(result).getJSONObject("alipay_trade_close_response");
        String code = (String) alipayTradeCloseResponse.get("code");
        String msg = (String) alipayTradeCloseResponse.get("msg");
        String tradeNo = (String) alipayTradeCloseResponse.get("trade_no"); // 支付宝交易号
        if(Objects.equals(code, "10000") && msg.toLowerCase().equals("success")){
            // 交易关闭成功
            String orderNo = (String) alipayTradeCloseResponse.get("out_trade_no");
            logger.info("交易关闭成功, 订单号: " + orderNo);
            resultVO.setCode(1);
            return resultVO;
        }
        return null;
    }

    @Override
    public ResultVO alipayBillQuery() throws AlipayApiException {
        // 支付宝对账单查询
        AlipayClient alipayClient = new DefaultAlipayClient(gatewayUrl, appId, privateKey, format, charset, publicKey, signType);
        // 设置请求参数
        AlipayDataDataserviceBillDownloadurlQueryRequest request = new AlipayDataDataserviceBillDownloadurlQueryRequest();
        // trade指商户基于支付宝交易收单的业务账单；signcustomer是指基于商户支付宝余额收入及支出等资金变动的帐务账单；
        String billType = "trade";
        String billDate = "2018-06";//日期需要小于当前月份
        JSONObject json = new JSONObject();
        json.put("bill_type", billType);
        json.put("bill_date", billDate);
        request.setBizContent(json.toString());
        AlipayDataDataserviceBillDownloadurlQueryResponse queryResponse = alipayClient.execute(request);
        logger.info("对账单下载链接: " + queryResponse.getBillDownloadUrl());
        ResultVO resultVO = new ResultVO();
        resultVO.setCode(1);
        resultVO.setData(queryResponse.getBillDownloadUrl());
        return resultVO;
    }

    @Override
    public void wapPay(Integer id, HttpServletResponse httpResponse) throws AlipayApiException, IOException {
        // 获取订单
        Order order = orderDAO.getById(id);
        List<OrderItem> list = orderItemDAO.getByOrderId(order.getId());
        order.setItems(list);

        // wap 支付
        //获得初始化的AlipayClient
        AlipayClient alipayClient = new DefaultAlipayClient(gatewayUrl, appId, privateKey, format, charset, publicKey, signType);
        //创建API对应的request
        AlipayTradeWapPayRequest alipayRequest = new AlipayTradeWapPayRequest();
        alipayRequest.setReturnUrl(returnUrl);
        alipayRequest.setNotifyUrl(notifyUrl);//在公共参数中设置回跳和通知地址

        JSONObject json = new JSONObject();
        json.put("out_trade_no", order.getOrderNo());
        json.put("total_amount", order.getAmount());
        json.put("subject", order.getItems().get(0).getItemName());
        json.put("product_code", "QUICK_WAP_PAY");
        logger.info("支付宝wap支付 入参: " + json.toString());
        alipayRequest.setBizContent(json.toString());

        //调用SDK生成表单
        String form = alipayClient.pageExecute(alipayRequest).getBody();

        httpResponse.setContentType("text/html;charset=" + charset);
        //直接将完整的表单html输出到页面
        httpResponse.getWriter().write(form);
        httpResponse.getWriter().flush();
        httpResponse.getWriter().close();
    }

    @Override
    public void appPay(Integer id, HttpServletResponse httpResponse) throws AlipayApiException {
        // APP 支付
        // 获取订单
        Order order = orderDAO.getById(id);
        List<OrderItem> list = orderItemDAO.getByOrderId(order.getId());
        order.setItems(list);

        //实例化客户端
        AlipayClient alipayClient = new DefaultAlipayClient(gatewayUrl, appId, privateKey, format, charset, publicKey, signType);

        //实例化具体API对应的request类,类名称和接口名称对应,当前调用接口名称：流程说明.trade.app.pay
        AlipayTradeAppPayRequest request = new AlipayTradeAppPayRequest();

        //SDK已经封装掉了公共参数，这里只需要传入业务参数。以下方法为sdk的model入参方式(model和biz_content同时存在的情况下取biz_content)。
        AlipayTradeAppPayModel model = new AlipayTradeAppPayModel();
        model.setBody(order.getItems().get(0).getItemName()); //对一笔交易的具体描述信息。如果是多种商品，请将商品描述字符串累加传给body。
        model.setSubject(order.getItems().get(0).getItemName());//商品的标题/交易标题/订单标题/订单关键字等
        model.setOutTradeNo(order.getOrderNo());// 商户网站唯一订单号
        model.setTimeoutExpress("30m");//该笔订单允许的最晚付款时间 1m～15d。m-分钟，h-小时，d-天，1c-当天（1c-当天的情况下，无论交易何时创建，都在0点关闭）。 该参数数值不接受小数点， 如 1.5h，可转换为 90m。
        model.setTotalAmount(order.getAmount().toString());//总金额
        model.setProductCode("QUICK_MSECURITY_PAY");//销售产品码，商家和支付宝签约的产品码，为固定值QUICK_MSECURITY_PAY
        request.setBizModel(model);
        request.setNotifyUrl(notifyUrl);
        try {
            //这里和普通的接口调用不同，使用的是sdkExecute
            AlipayTradeAppPayResponse response = alipayClient.sdkExecute(request);
            System.out.println(response.getBody());//就是orderString 可以直接给客户端请求，无需再做处理。
            //根据response中的结果继续业务逻辑处理
            // TODO



        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void alipayNotify(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
        //调用SDK验证签名
        boolean signVerified = rsaCheckV1(request);
        if(signVerified) {
            logger.info("支付宝SDK 验证成功");
            //商户订单号
            String out_trade_no = new String(request.getParameter("out_trade_no").getBytes("ISO-8859-1"),"UTF-8");

            //支付宝交易号
            String trade_no = new String(request.getParameter("trade_no").getBytes("ISO-8859-1"),"UTF-8");

            //交易状态
            String trade_status = new String(request.getParameter("trade_status").getBytes("ISO-8859-1"),"UTF-8");

            if(trade_status.equals("TRADE_FINISHED")){
                // 交易结束，不可退款
                logger.info("trade_status : TRADE_FINISHED");
                // 更新订单支付状态
                orderDAO.editStatusAndTradeNo(out_trade_no, 3, trade_no);
                logger.info("订单状态更新成功  订单状态: " + 3);
            }else if (trade_status.equals("TRADE_SUCCESS")){
                // 交易支付成功
                logger.info("trade_status : TRADE_SUCCESS");
                // 更新订单支付状态
                orderDAO.editStatusAndTradeNo(out_trade_no, 2, trade_no);
                logger.info("订单状态更新成功  订单状态: " + 2);
            }
        }else {//验证失败
            logger.info("验证失败");
        }
        logger.info("回调完成");
    }

    @Override
    public boolean alipayReturn(HttpServletRequest request, HttpServletResponse response){
        // 验签
        boolean flag = rsaCheckV1(request);
        if(flag) {
            // 同步通知处理成功
            logger.info("同步通知处理成功, 跳转页面");
        }else {
            // 同步通知处理失败
            logger.info("同步通知处理失败");
        }
        return flag;
    }

    /**
     * 检验支付宝签名
     */
    private boolean rsaCheckV1(HttpServletRequest request){
        // 获取支付宝POST发送数据
        Map<String,String> params = new HashMap<>();
        Map requestParams = request.getParameterMap();
        for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext();) {
            String name = (String) iter.next();
            String[] values = (String[]) requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i]
                        : valueStr + values[i] + ",";
            }
            params.put(name, valueStr);
        }
        //乱码解决，这段代码在出现乱码时使用
        // valueStr = new String(valueStr.getBytes("ISO-8859-1"), "utf-8");

        boolean flag = false;
        try {
            flag = AlipaySignature.rsaCheckV1(params, publicKey, charset, signType);
        } catch (AlipayApiException e) {
            logger.info("支付宝 验签异常");
            e.printStackTrace();
        }
        logger.info("支付宝 验签结果: " + flag);
        return flag;
    }
}
