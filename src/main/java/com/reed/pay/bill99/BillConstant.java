package com.reed.pay.bill99;

/**
 * Created by zhangxubo on 15-1-7.
 */
public class BillConstant {

    public static final String ENCODING = "utf-8";
    public static final String ACTION_APPLY = "fo.batch.settlement.pay";
    public static final String ACTION_COMPLEXQUERY = "fo.batch.settlement.complexquery";
    public static final String ACTION_BATCHIDQUERY = "fo.batch.settlement.batchidquery";
    public static final String URL = "https://sandbox.99bill.com/fo-batch-settlement/services";


    /** 付费方式  0:收款方付费 1:付款方付费*/
    public static final String  FEE_TYPE = "1";

    /** 交易币种 */
    public  static final String CUR = "RMB";

    /** 是否验证金额 0:不验证 1:验证 */
    public static final String  CHECK_AMT_CNT= "1";

    /** 是否整批失败 0:整批失败; 1:不整批失败 */
    public static final String BATCH_FAIL = "1";

    /** 充值方式 0:代扣,1:充值,2:垫资 */
    public static final  String RECHARGE_TYPE = "1";

    /** 是否自动退款 0:自动退款; 1:不自动退款*/
    public static final String  AUTO_REFUND = "0";

    public static final String  PERIOD = "0";

    /** 0:企业; 1:个人 */
    public static final String PAYEE_TYPE = "0";

    /** 是否短信通知  0:通知; 1:不通知*/
    public static final String PHONE_NOTE_FLAG = "0";

    /** 版本信息 */
    public static final String VERSION = "1.0";

    /** 加密类型 */
    public static  final String  FEATURE_CODE= "F889";

    /**显示详情 0:显示，1：不显示*/
    public static final String DETAIL_SHOW="0";
    
    public static final String PAGE="1";
    
    public static final String PAGE_SIZE="10000";

    private BillConstant(){}


}
