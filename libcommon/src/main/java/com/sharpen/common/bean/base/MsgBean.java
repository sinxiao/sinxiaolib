package com.sharpen.common.bean.base;


import com.sharpen.common.consts.SignConst;

import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.List;


/**
 * 通用请求对象
 * @author sharpen-auto
 */

public class MsgBean<T> implements Serializable {

  private static final long serialVersionUID = 8995L;
  private MsgHead msgHead;
  private MsgBody<T> msgBody;

  public MsgBean(){}
  public MsgBean(MsgHead msgHead){
    this.msgHead = msgHead;
  }
  public MsgBean(MsgBody<T> msgBody){
    this.msgHead = new MsgHead();
    this.msgBody = msgBody;
  }
  public MsgBean(MsgHead msgHead, MsgBody<T> msgBody){
    this.msgHead = new MsgHead();
    this.msgBody = msgBody;
  }


  public static MsgBean success(){
    MsgHead msgHead = new MsgHead("0", "操作成功");
    return new MsgBean(msgHead);
  }
  public static boolean isSuccess(MsgBean bean){
    if(bean==null || bean.getMsgHead() == null || StringUtils.isBlank(bean.getMsgHead().getCode())){
      return false;
    }
    return StringUtils.equals(bean.getMsgHead().getCode(), SignConst.STR_ZERO);
  }

  public static boolean noEntity(MsgBean msgBean){
    return msgBean == null || msgBean.getMsgBody() == null || msgBean.getMsgBody().getEntity() == null;
  }

  public static MsgBean after(MsgBean old, MsgBean update){
    if(update==null){
      update = new MsgBean(new MsgHead(), new MsgBody());
    }
    if(update.getMsgHead() == null){
      update.setMsgHead(new MsgHead());
    }
    if(old!=null){
      if(old.getMsgHead()!=null){
        MsgHead head = update.getMsgHead();
        head.setReqTime(old.getMsgHead().getReqTime());
      }
    }
    update.getMsgHead().setRespTime(System.currentTimeMillis());
    update.getMsgBody().setBizSort(old.getMsgBody().getBizSort());
    return update;
  }

  public static MsgBean inst(String code, String msg){
    MsgHead msgHead = new MsgHead(code, msg);
    return new MsgBean(msgHead);
  }
  public static MsgBean inst(String code, String msg, String bizDesc){
    MsgHead msgHead = new MsgHead(code, msg, bizDesc);
    return new MsgBean(msgHead);
  }

  public static MsgBean instWithMsgStr(String msgStr){
    MsgBody msgBody = new MsgBody();
    msgBody.setMsgStr(msgStr);
    return new MsgBean(msgBody);
  }
  public static MsgBean instWithBody(MsgBody msgBody){
    return new MsgBean(msgBody);
  }
  public static MsgBean instWithBody(){
    return new MsgBean(new MsgBody());
  }


  public static MsgBean instWith(MsgBody msgBody) {
    MsgHead msgHead = new MsgHead("0", "操作成功");
    return new MsgBean(msgHead, msgBody);
  }

  public static MsgBean instWithBody(MsgHead msgHead, MsgBody msgBody) {
    return new MsgBean(msgHead, msgBody);
  }

  public static MsgBean instMsg(String msg){
    MsgBody msgBody = new MsgBody();
    msgBody.setMsgStr(msg);
    return new MsgBean(msgBody);
  }
  public static <T> MsgBean<T> instEntity(T t){
    MsgBody msgBody = new MsgBody();
    msgBody.setEntity(t);
    return new MsgBean(msgBody);
  }
  public static <T> MsgBean<T> instEntityList(List<T> t){
    MsgBody msgBody = new MsgBody();
    msgBody.setEntityList(t);
    return new MsgBean(msgBody);
  }


  public static <T> T entity(MsgBean<T> msgBean){
    return msgBean == null || msgBean.getMsgBody() == null? null : msgBean.getMsgBody().getEntity();
  }
  public static <T> List<T> entityList(MsgBean<T> msgBean){
    return msgBean == null || msgBean.getMsgBody() == null? null : msgBean.getMsgBody().getEntityList();
  }



  public static MsgBean successWithSesnId(String sesnId) {
    MsgHead msgHead = new MsgHead("0", "操作成功");
    msgHead.setSessionId(sesnId);
    return new MsgBean(msgHead);
  }

  public static MsgBean success(MsgBody msgBody) {
    MsgHead msgHead = new MsgHead("0", "操作成功");
    //msgBody =  new MsgBody();
    return new MsgBean(msgHead, msgBody);
  }

  public static MsgBean success(MsgHead msgHead, MsgBody msgBody) {
    return new MsgBean(msgHead, msgBody);
  }

  public static MsgBean successWithBody(){
    MsgHead msgHead = new MsgHead("0", "操作成功");
    return new MsgBean(msgHead, new MsgBody());
  }
  public static MsgBean successWithBody(MsgHead reqHead){
    MsgHead msgHead = new MsgHead("0", "操作成功");
    msgHead.setReqTime(reqHead.getReqTime());
    return new MsgBean(msgHead, new MsgBody());
  }

  public static MsgBean fail(){
    MsgHead msgHead = new MsgHead("0", "操作失败");
    return new MsgBean(msgHead);
  }
  public static MsgBean failWithMsg(String msg, String bizDesc){
    MsgHead msgHead = new MsgHead("-1", msg, bizDesc);
    return new MsgBean(msgHead);
  }


  public MsgHead getMsgHead() {
    return msgHead;
  }

  public void setMsgHead(MsgHead msgHead) {
    this.msgHead = msgHead;
  }

  public MsgBody<T> getMsgBody() {
    return msgBody;
  }

  public void setMsgBody(MsgBody<T> msgBody) {
    this.msgBody = msgBody;
  }
}
