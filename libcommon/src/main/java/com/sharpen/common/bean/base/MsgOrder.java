package com.sharpen.common.bean.base;

import com.sharpen.common.consts.SignConst;
import com.sharpen.common.consts.SymbolConst;
import com.sharpen.common.util.StrTool;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Map;

import static com.sharpen.common.consts.SignConst.ORDERBY_ASC;
import static com.sharpen.common.consts.SignConst.ORDERBY_DESC;
import static com.sharpen.common.consts.SignConst.ORDERBY_VIEW_ASC;

public class MsgOrder {
  private static final long serialVersionUID = 893495L;

  // 排序列
  private String sidx;
  // 排序顺序
  private String sord;


  public MsgOrder(){}
  /**
   * 生成sql中的order by 子语句.
   *
   * @param prop2column
   * @param msgOrderList
   * @return
   */
  public static String toOrderBySql(Map<String, String> prop2column, List<MsgOrder> msgOrderList) {
    if (CollectionUtils.isEmpty(msgOrderList)) {
      return null;
    }
    StringBuilder sb = new StringBuilder();
    for (MsgOrder msgOrder : msgOrderList) {
      if (StringUtils.isBlank(msgOrder.sidx)) {
        continue;
      }
      String prop = prop2column.get(msgOrder.getSidx());
      prop = StringUtils.isBlank(prop) ? msgOrder.getSidx() : prop;
      if (StringUtils.isNotBlank(sb)) {
        sb.append(SymbolConst.COMMA);
      }
      String sort = msgOrder.sord == null ? null : msgOrder.sord.toLowerCase();
      sort = StrTool.inAny(sort, ORDERBY_ASC, ORDERBY_VIEW_ASC) ? ORDERBY_ASC : ORDERBY_DESC;
      sb.append(prop).append(SymbolConst.BLANK_SPACE).append(sort);
    }
    return sb.toString();
  }


  // 下面是生成的get, set方法

  public String getSidx() {
    return sidx;
  }

  public void setSidx(String sidx) {
    this.sidx = sidx;
  }

  public String getSord() {
    return sord;
  }

  public void setSord(String sord) {
    this.sord = sord;
  }
}
