package com.sharpen.common.bean.base;

import java.io.Serializable;

/**
 * 消息头。一般用于保存请求参数
 *
 * @author sharpen-auto
 */
public class MsgHead implements Serializable {

  private String code = "0";
  private String msg = "响应正确";
  /**
   * 业务描述
   */
  private String bizDesc;
  private Long reqTime = System.currentTimeMillis();
  private Long respTime = null;

  /**
   * 请求流水号
   */
  private String requestId = null;

  /**
   * 服务流水号
   */
  private String serviceId = null;

  /**
   * 会话标识，如果需要的话
   */
  private String sessionId = null;
  /**
   * 客户端版本
   */
  private String clientVer = null;
  /**
   * 每个手机唯一标识。CDMA设备的标识；imei GSM、WCDMA设备的标识；
   */
  private String meid = null;


  /**
   * 16进制token, 例：ABCDEF1234567890
   */
  private String token = null;
  /**
   * 16进制形式
   */
  private String pubKey = null;
  private String cipherKey = null;

  /**
   * 是否间接调用。默认为N，如果是Y，需要转到另一个系统
   */
  private String indirectCall = null;
  /**
   * 间接调用的系统。根据间接调用的系统，找到相应的转发service
   */
  private String indirectSys = null;

  /**
   * 客户端要求加密。Y为要求加密，即使后台没有配置加密
   */
  private String clientCipher = null;


  public MsgHead(){}
  public MsgHead(String code, String msg) {
    this.code = code;
    this.msg = msg;
  }

  public MsgHead(String code, String msg, String bizDesc) {
    this.code = code;
    this.msg = msg;
    this.bizDesc = bizDesc;
  }


  // 下面是生成的get, set方法
  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public String getMsg() {
    return msg;
  }

  public void setMsg(String msg) {
    this.msg = msg;
  }

  public String getBizDesc() {
    return bizDesc;
  }

  public void setBizDesc(String bizDesc) {
    this.bizDesc = bizDesc;
  }

  public Long getReqTime() {
    return reqTime;
  }

  public void setReqTime(Long reqTime) {
    this.reqTime = reqTime;
  }

  public Long getRespTime() {
    return respTime;
  }

  public void setRespTime(Long respTime) {
    this.respTime = respTime;
  }

  public String getRequestId() {
    return requestId;
  }

  public void setRequestId(String requestId) {
    this.requestId = requestId;
  }

  public String getServiceId() {
    return serviceId;
  }

  public void setServiceId(String serviceId) {
    this.serviceId = serviceId;
  }

  public String getSessionId() {
    return sessionId;
  }

  public void setSessionId(String sessionId) {
    this.sessionId = sessionId;
  }

  public String getClientVer() {
    return clientVer;
  }

  public void setClientVer(String clientVer) {
    this.clientVer = clientVer;
  }

  public String getMeid() {
    return meid;
  }

  public void setMeid(String meid) {
    this.meid = meid;
  }

  public String getToken() {
    return token;
  }

  public void setToken(String token) {
    this.token = token;
  }

  public String getPubKey() {
    return pubKey;
  }

  public void setPubKey(String pubKey) {
    this.pubKey = pubKey;
  }

  public String getCipherKey() {
    return cipherKey;
  }

  public void setCipherKey(String cipherKey) {
    this.cipherKey = cipherKey;
  }

  public String getIndirectCall() {
    return indirectCall;
  }

  public void setIndirectCall(String indirectCall) {
    this.indirectCall = indirectCall;
  }

  public String getIndirectSys() {
    return indirectSys;
  }

  public void setIndirectSys(String indirectSys) {
    this.indirectSys = indirectSys;
  }

  public String getClientCipher() {
    return clientCipher;
  }

  public void setClientCipher(String clientCipher) {
    this.clientCipher = clientCipher;
  }
}
