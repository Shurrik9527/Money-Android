package com.moyacs.canary.network;

/**
 * @author heguogui
 * @version v 1.0.0
 * @describe
 * @date 2019/3/18
 * @email 252774645@qq.com
 */
public class RateResult<T>{

    private int success;
    private T result;

    public int getSuccess() {
        return success;
    }

    public void setSuccess(int success) {
        this.success = success;
    }

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }

    @Override
    public String toString() {
        return "RateResult{" +
                "success=" + success +
                ", result=" + result +
                '}';
    }
}
