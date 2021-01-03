package com.sharpen.common.util;

import org.apache.commons.collections4.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class MapTool {

    private static Logger log = LoggerFactory.getLogger(MapTool.class);

    public static boolean allNoEmpty(Map ...maps){
        for(Map map : maps){
            if(MapUtils.isEmpty(map)){
                return false;
            }
        }
        return true;
    }
    public static boolean hasEmpty(Map ...maps){
        return !allNoEmpty(maps);
    }

}
