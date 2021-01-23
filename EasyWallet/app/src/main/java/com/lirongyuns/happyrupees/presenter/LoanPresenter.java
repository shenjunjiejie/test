package com.lirongyuns.happyrupees.presenter;

public interface LoanPresenter
{
    void getConfig();


    void submitCredit7(String memberId);

    void submitCredit14(String memberId);

    /**
     * 提交个人信息到服务器
     * @param id id
     * @param memberId memberId
     * @param serialNumber 手机系列号
     * @param imeiIsSame 是否与登录时一致 0 否 1是
     * @param brandModel 手机型号
     * @param age 年龄
     * @param sex 性别 0 女 1男
     * @param applyTime 申请时间
     * @param salaryRange 薪资
     * @param marryState 婚姻状态
     * @param jobPosition 职位
     * @param education 学历
     * @param phoneNumber1 联系电话
     * @param phoneNumber2 联系电话
     * @param contact1 联系人
     * @param contact2 联系人
     * @param email 邮箱
     * @param phoneTelNum 本机号码
     */
    void commitPersonalInfoToServer(long id, long memberId, String serialNumber,
                                    int imeiIsSame,  String brandModel,
                                    int age, int sex,String applyTime,int salaryRange,
                                    int marryState, int jobPosition,  int education,
                                    String phoneNumber1, String phoneNumber2, String contact1,
                                    String contact2, String email, String phoneTelNum, String language, int religion);
}
