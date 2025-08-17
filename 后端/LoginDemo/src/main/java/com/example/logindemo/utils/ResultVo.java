package com.example.logindemo.utils;

import lombok.Data;

@Data
public class ResultVo{ // 声明泛型类型参数 T
    private boolean flag;
    private String message;
    private Object data; // 使用泛型类型 T

    /**
     * 成功不添加data
     */
    public static  ResultVo ok(String message) {
        ResultVo resultVo = new ResultVo();
        resultVo.setMessage(message);
        resultVo.setFlag(true);
        return resultVo;
    }

    /**
     * 成功添加data
     */
    public static  ResultVo ok(Object data) {
        ResultVo resultVo = new ResultVo();
        resultVo.setData(data);
        resultVo.setFlag(true);
        return resultVo;
    }

    /**
     * 成功添加data和message
     */
    public static  ResultVo ok(Object data, String message) {
        ResultVo resultVo = new ResultVo();
        resultVo.setData(data);
        resultVo.setFlag(true);
        resultVo.setMessage(message);
        return resultVo;
    }

    /**
     * 失败
     */
    public static  ResultVo fail(String message) {
        ResultVo resultVo = new ResultVo();
        resultVo.setFlag(false);
        resultVo.setMessage(message);
        return resultVo;
    }

    /**
     * 失败返回状态数据
     */
    public static  ResultVo fail(String message, Object data) {
        ResultVo resultVo = new ResultVo();
        resultVo.setFlag(false);
        resultVo.setMessage(message);
        resultVo.setData(data);
        return resultVo;
    }

    /**
     * 异常
     */
    public static  ResultVo error(Exception e) {
        ResultVo resultVo = new ResultVo();
        resultVo.setMessage("系统异常:" + e.getMessage());
        resultVo.setFlag(false);
        return resultVo;
    }
}