package com.pengguli.orderservice.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.wxpay.sdk.WXPayUtil;
import com.pengguli.orderservice.pojo.Order;
import com.pengguli.orderservice.pojo.PayLog;
import com.pengguli.orderservice.mapper.PayLogMapper;
import com.pengguli.orderservice.service.PayLogService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pengguli.orderservice.utils.HttpClient;
import com.pengguli.orderservice.utils.WeiXinUtils;
import com.pengguli.servicebase.exceptionhandler.GuliException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * 支付日志表 服务实现类
 * </p>
 *
 * @author peng
 * @since 2023-01-09
 */
@Service
public class PayLogServiceImpl extends ServiceImpl<PayLogMapper, PayLog> implements PayLogService {

    @Autowired
    private OrderServiceImpl orderService;

    // 1、生成微信支付二维码
    @Override
    public Map createNative(String orderNo) {
        try{
            //1 根据订单号，查询订单信息
            QueryWrapper<Order> wrapper = new QueryWrapper<>();
            wrapper.eq("order_no",orderNo);
            Order order = orderService.getOne(wrapper);

            //2 Map集合设置生成微微码所需要的参数
            Map m = new HashMap();
            m.put("appid",WeiXinUtils.WEI_XIN_OPPENID);
            m.put("mch_id", WeiXinUtils.PARTNER);
            m.put("nonce_str", WXPayUtil.generateNonceStr());
            m.put("body", order.getCourseTitle()); //课程标题
            m.put("out_trade_no", orderNo); //订单号
            m.put("total_fee", order.getTotalFee().multiply(new BigDecimal("100")).longValue()+"");
            m.put("spbill_create_ip", "127.0.0.1");
            m.put("notify_url", WeiXinUtils.NOTIFY_URL);
            m.put("trade_type", "NATIVE");

            //3 发送httpclient请求，传递xml格式，访问微信支付提供的固定地址
            HttpClient client = new HttpClient("https://api.mch.weixin.qq.com/pay/unifiedorder");
            //client设置参数
            client.setXmlParam(WXPayUtil.generateSignedXml(m, WeiXinUtils.PARTNER_KEY));
            client.setHttps(true);
            // 发送请求
            client.post();
            //4 得到发送请求返回结果
            //返回内容，是使用xml格式返回
            String xml = client.getContent();
            //把xml格式转换map集合，把map集合返回
            Map<String,String> resultMap = WXPayUtil.xmlToMap(xml);
            if ("SUCCESS".equals(resultMap.get("return_code")) && "SUCCESS".equals(resultMap.get("result_code"))) {
                //5 封装返回结果
                Map map = new HashMap<>();
                map.put("out_trade_no", orderNo);
                map.put("course_id", order.getCourseId());
                map.put("total_fee", order.getTotalFee());
                map.put("result_code", resultMap.get("result_code"));
                map.put("code_url", resultMap.get("code_url"));
                return map;
            }
            throw new GuliException(20001,"订单已支付");
        }catch (Exception e){
            e.printStackTrace();
            throw new GuliException(20001,"生成失败");
        }
    }

    // 更新订单状态，添加支付记录
    @Transactional
    @Override
    public void updateOrderStatus(Map<String, String> map) {
        // 获取订单id
        String orderNo = map.get("out_trade_no");
        // 根据订单id获取订单信息
        QueryWrapper<Order> wrapper = new QueryWrapper<>();
        wrapper.eq("order_no",orderNo);
        Order order = orderService.getOne(wrapper);
        if (order.getStatus().intValue() == 1) return;
        if (order.getStatus().intValue() != 1){
            // 更新支付状态
            order.setStatus(1);
            orderService.updateById(order);
        }

        //记录支付日志
        PayLog payLog=new PayLog();
        payLog.setOrderNo(order.getOrderNo());//支付订单号
        payLog.setPayTime(new Date());
        payLog.setPayType(1);//支付类型
        payLog.setTotalFee(order.getTotalFee());//总金额(分)
        payLog.setTradeState(map.get("trade_state"));//支付状态
        payLog.setTransactionId(map.get("transaction_id"));
        payLog.setAttr(JSONObject.toJSONString(map));
        baseMapper.insert(payLog);//插入到支付日志表
    }

    // 查询订单状态
    @Override
    public Map<String, String> queryPayStatus(String orderNo) {
        try{
            // 1、封装参数
            Map m = new HashMap<>();
            m.put("appid", WeiXinUtils.WEI_XIN_OPPENID);
            m.put("mch_id", WeiXinUtils.PARTNER);
            m.put("out_trade_no", orderNo);
            m.put("nonce_str", WXPayUtil.generateNonceStr());

            // 2、设置请求
            HttpClient client = new HttpClient("https://api.mch.weixin.qq.com/pay/orderquery");
            client.setXmlParam(WXPayUtil.generateSignedXml(m, "T6m9iK73b0kn9g5v426MKfHQH7X8rKwb"));
            client.setHttps(true);
            client.post();

            // 3、返回第三方的数据
            String content = client.getContent();
            System.out.println(content );
            Map<String, String> map = WXPayUtil.xmlToMap(content);
            return map;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
}
