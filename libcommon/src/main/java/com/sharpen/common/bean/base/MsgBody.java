package com.sharpen.common.bean.base;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 消息头。一般用于保存请求参数
 *
 * @author sharpen-auto
 */
public class MsgBody<T> implements Serializable {

  private static final long serialVersionUID = 895L;
  /**
   * 业务分类。分：add,addBatch,nullify,nullifyBatch,nullifyPhysical,modify,primaryQuery,listQuery
   */
  private String bizSort = null;

  /**
   * 一般用于放主键之类的标识
   */
  private String mark;
  private String identify;
  private String msgStr;
  private Boolean processResult;
  private T entity;
  private List<T> entityList;
  /**
   * 扩展
   */
  private Map<String, Object> extMap;

  /**
   * jqgrid,如果没有下面的变量，就会出现Bad Request的请求错误
   */
  private String search;

  private String nd;

  private List<MsgOrder> msgOrders;

  /**
   * 分页类的参数
   */

  /**
   * 查询第几页。从1开始
   */
  private long reqPageNum = 1;

  /**
   * 每页显示多少条
   */
  private int reqRecordNum = 15;

  /**
   * 共有多少条记录
   */
  private long respRecordCount = 0;

  /**
   * 共有多少条页
   */
  private long respPageCount = 0;


  public MsgBody(){}
  public static <T> MsgBody<T> withEntityList(List<T> list){
    MsgBody body = new MsgBody<>();
    body.setEntityList(list);
    return body;
  }
  public static <T> MsgBody<T> withEntity(T entity){
    MsgBody body = new MsgBody<>();
    body.setEntity(entity);
    return body;
  }


  // 下面是生成的get, set方法


  public String getBizSort() {
    return bizSort;
  }

  public void setBizSort(String bizSort) {
    this.bizSort = bizSort;
  }

  public String getMark() {
    return mark;
  }

  public void setMark(String mark) {
    this.mark = mark;
  }

  public String getIdentify() {
    return identify;
  }

  public void setIdentify(String identify) {
    this.identify = identify;
  }

  public String getMsgStr() {
    return msgStr;
  }

  public void setMsgStr(String msgStr) {
    this.msgStr = msgStr;
  }

  public Boolean getProcessResult() {
    return processResult;
  }

  public void setProcessResult(Boolean processResult) {
    this.processResult = processResult;
  }

  public T getEntity() {
    return entity;
  }

  public void setEntity(T entity) {
    this.entity = entity;
  }

  public List<T> getEntityList() {
    return entityList;
  }

  public void setEntityList(List<T> entityList) {
    this.entityList = entityList;
  }

  public Map<String, Object> getExtMap() {
    return extMap;
  }

  public void setExtMap(Map<String, Object> extMap) {
    this.extMap = extMap;
  }

  public String getSearch() {
    return search;
  }

  public void setSearch(String search) {
    this.search = search;
  }

  public String getNd() {
    return nd;
  }

  public void setNd(String nd) {
    this.nd = nd;
  }

  public List<MsgOrder> getMsgOrders() {
    return msgOrders;
  }

  public void setMsgOrders(List<MsgOrder> msgOrders) {
    this.msgOrders = msgOrders;
  }

  public long getReqPageNum() {
    return reqPageNum;
  }

  public void setReqPageNum(long reqPageNum) {
    this.reqPageNum = reqPageNum;
  }

  public int getReqRecordNum() {
    return reqRecordNum;
  }

  public void setReqRecordNum(int reqRecordNum) {
    this.reqRecordNum = reqRecordNum;
  }

  public long getRespRecordCount() {
    return respRecordCount;
  }

  public void setRespRecordCount(long respRecordCount) {
    this.respRecordCount = respRecordCount;
  }

  public long getRespPageCount() {
    return respPageCount;
  }

  public void setRespPageCount(long respPageCount) {
    this.respPageCount = respPageCount;
  }
}
