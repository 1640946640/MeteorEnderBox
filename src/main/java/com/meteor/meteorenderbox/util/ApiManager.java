package com.meteor.meteorenderbox.util;

import net.milkbowl.vault.economy.*;
import org.black_ixx.playerpoints.*;
import org.black_ixx.playerpoints.api.*;

/**
 * API管理类
 * 负责管理经济系统和点券系统的API
 */
public class ApiManager
{
    /** 经济系统API */
    public static Economy econmy;
    /** 点券系统API */
    public static PlayerPointsAPI pointsAPI;
    
    /**
     * 检查经济系统是否可用
     * @return 是否可用
     */
    public static boolean isEconmy() {
        return ApiManager.econmy != null;
    }
    
    /**
     * 检查点券系统是否可用
     * @return 是否可用
     */
    public static boolean isPoints() {
        return ApiManager.pointsAPI != null;
    }
}