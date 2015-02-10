package com.reed.pay.bill99;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class BillConfig {

    /** 付款账号 */
    @Value("${bill99.payerAcctCode}")
    private String payerAcctCode;

    /** 付款商户名称 */
    @Value("${bill99.merchantName}")
    private String merchantName;

    /** 商户编号*/
    @Value("${bill99.memberCode}")
    private String memberCode;

    /** 付款人姓名 */
    @Value("${bill99.payerName}")
    private String payerName;

    public String getPayerName() {
        return payerName;
    }

    public void setPayerName(String payerName) {
        this.payerName = payerName;
    }

    public String getPayerAcctCode() {
        return payerAcctCode;
    }

    public void setPayerAcctCode(String payerAcctCode) {
        this.payerAcctCode = payerAcctCode;
    }

    public String getMemberCode() {
        return memberCode;
    }

    public void setMemberCode(String memberCode) {
        this.memberCode = memberCode;
    }

    public String getMerchantName() {
        return merchantName;
    }

    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName;
    }
}