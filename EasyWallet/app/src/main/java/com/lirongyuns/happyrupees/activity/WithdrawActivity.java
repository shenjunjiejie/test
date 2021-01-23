package com.lirongyuns.happyrupees.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.lirongyuns.happyrupees.R;
import com.lirongyuns.happyrupees.annonation.Click;
import com.lirongyuns.happyrupees.annonation.SetActivity;
import com.lirongyuns.happyrupees.annonation.SetView;
import com.lirongyuns.happyrupees.bean.LoanDetailsJson;
import com.lirongyuns.happyrupees.conf.NetWorkConf;
import com.lirongyuns.happyrupees.dialog.LoadingDialog;
import com.lirongyuns.happyrupees.presenter.WithdrawPresenterImpl;
import com.lirongyuns.happyrupees.utils.ConstantUtils;
import com.lirongyuns.happyrupees.utils.ToastUtil;
import com.lirongyuns.happyrupees.view.WithdrawView;

@SetActivity(R.layout.activity_withdraw)
public class WithdrawActivity extends BaseActivity implements WithdrawView {

    @SetView(R.id.withdrawl_service_charge)
    private TextView withdrawl_service_charge;
    @SetView(R.id.withdrawl_taxes)
    private TextView withdrawl_taxes;
    @SetView(R.id.withdrawl_interest_rate)
    private TextView withdrawl_interest_rate;
    @SetView(R.id.withdrawl_borrowing_date)
    private TextView withdrawl_borrowing_date;
    @SetView(R.id.withdrawl_repayment_date)
    private TextView withdrawl_repayment_date;
    @SetView(R.id.withdrawl_borrow_amount)
    private TextView withdrawl_borrow_amount;
    @SetView(R.id.withdrawl_repayment_amount)
    private TextView withdrawl_repayment_amount;
    @SetView(R.id.withdrawl_apply)
    private TextView withdrawl_apply;

    private WithdrawPresenterImpl withdrawfopresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        withdrawfopresenter = new WithdrawPresenterImpl(this);
        LoadingDialog.open(this);
        withdrawfopresenter.getLoanDetailsFromServer(getApp().getMemberId(), "1");
        withdrawfopresenter.getUserInfoFromServer(getApp().getMemberId());
        appsflyerEvent(getApp().getMemberId(), "GET_WITHDRAWAL");
        setFirebaseEvent(getApp().getMemberId(), "GET_WITHDRAWAL");
        Bundle bundle = new Bundle();
        bundle.putString("member_id", getApp().getMemberId());
        setFaceBookEvent(bundle, "GET_WITHDRAWAL");
    }

    private String ucash_url;//合同地址
    private String ucash_loanbno;//账单号

    private int loanpid;

    @Click({R.id.withdrawl_apply})
    private void click(View view){
        switch (view.getId()){
            case R.id.withdrawl_apply:
                if(TextUtils.isEmpty(ucash_loanbno)){
                    ToastUtil.show(this, "No billing information was obtained");
                    return;
                }
                LoadingDialog.open(this);
                withdrawfopresenter.confirWithdrawToServer(getApp().getMemberId(), loanpid+"", ucash_loanbno);
                withdrawl_apply.setEnabled(false);

                Bundle bundle = new Bundle();
                bundle.putString("member_id", getApp().getMemberId());

                setFirebaseEvent(getApp().getPhoneNum(), "CONFIRM_WITHDRAWAL");
                appsflyerEvent(getApp().getPhoneNum(), "CONFIRM_WITHDRAWAL");
                setFaceBookEvent(bundle, "CONFIRM_WITHDRAWAL");
                break;
        }
    }

    @SetView(R.id.withdrawl_amount)
    private TextView withdrawl_amount;

    @Override
    public void setLoanDetailsUIDisplay(LoanDetailsJson.DataBean data) {
        withdrawl_apply.setEnabled(true);
        String applicationAmount = data.getApplicationAmount();
        withdrawl_amount.setText("Rs "+applicationAmount);
        withdrawl_borrow_amount.setText("Rs "+data.getLoanAmount());
        withdrawl_repayment_amount.setText("Rs "+data.getRepayable());//contract_address
        withdrawl_service_charge.setText(data.getServiceRate()+"%");

        withdrawl_repayment_date.setText(data.getOverdueTime());
        withdrawl_taxes.setText("Rs "+data.getGstTax());
        withdrawl_interest_rate.setText(data.getInterest() + "%");
        String contractAddress = data.getContractAddress();
        if(!TextUtils.isEmpty(contractAddress) && contractAddress.startsWith("/")){
            contractAddress = contractAddress.substring(1);
        }

        String creatTime = null;
        if(!TextUtils.isEmpty(data.getCreateTime()) && data.getCreateTime().contains(" ")){
            String[] s = data.getCreateTime().split(" ");
            creatTime = s[0];
        }else{
            creatTime = data.getCreateTime();
        }
        withdrawl_borrowing_date.setText(creatTime);

        contractAddress = NetWorkConf.getInstance().getHttpHost() +contractAddress ;
        ucash_url = contractAddress;
        ucash_loanbno = data.getId()+"";
        loanpid = data.getProductId();
    }

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 111:
                    Intent intent = new Intent(getBaseContext(), WaitActivity.class);
                    intent.putExtra("flag", "LoanDetails");
                    startActivity(intent);
                    LoadingDialog.close();
                    break;
            }
        }
    };

    @Override
    public void startAuditResultPage() {
        Message message = mHandler.obtainMessage();
        message.what = 111;
        mHandler.sendMessageDelayed(message, 3000);
        LoadingDialog.open(this);
    }

    @Override
    public void startNextPage(String msg) {
        if("5".equals(ConstantUtils.LOAN_STATUS) || "21".equals(ConstantUtils.LOAN_STATUS)){
            startActivity(new Intent(getBaseContext(), WaitActivity.class));
            finish();
            return;
        }

        if("1".equals(ConstantUtils.REPEATED_BORROWING) || "4".equals(ConstantUtils.REPEATED_BORROWING)){
            //不验卡
            finish();
        }else{
            //需要验卡
            Intent intent = new Intent(getBaseContext(), BindCardActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public void showToast(String msg) {
        ToastUtil.show(WithdrawActivity.this,msg);
    }
}