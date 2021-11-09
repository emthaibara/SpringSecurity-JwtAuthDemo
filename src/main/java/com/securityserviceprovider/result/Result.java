package com.securityserviceprovider.result;

/**
 * @Author:SCBC_LiYongJie
 * @time:2021/11/2
 *
 *              0:error
 *              1.success
 */
public class Result<T> {
    private Integer code ;
    private String codeMsg;
    private T data;

    private Result(CodeMsg codeMsg){
        this.code = codeMsg.getCode();
        this.data = null;
        this.codeMsg = codeMsg.getCodeMsg();
    }

    private Result(Integer code,String codeMsg){
        this.code = code;
        this.codeMsg = codeMsg;
        this.data = null;
    }

    private Result(T data) {
        code = 1 ;
        this.data = data;
        codeMsg= null;
    }

    public static <T>Result<T> success (T t){
        return new Result<>(t);
    }

    public static  <T>Result<T> error(CodeMsg codeMsg){
        return new Result<>(codeMsg);
    }

    public static Result<String> loginVerifySuccess(){
        return new Result<>(1,"登陆身份验证成功");
    }

    public static Result<String> loginVerifyFail(){
        return new Result<>(0,"登陆验证失败");
    }

    public int getCode() {
        return code;
    }
    public void setCode(int code) {
        this.code = code;
    }
    public String getCodeMsg() {
        return codeMsg;
    }
    public void setCodeMsg(String codeMsg) {
        this.codeMsg = codeMsg;
    }
    public T getData() {
        return data;
    }
    public void setData(T data) {
        this.data = data;
    }
}

