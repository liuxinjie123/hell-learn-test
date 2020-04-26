package com.springboot.hello.test.cashier.factory.cashsuper;

import com.springboot.hello.test.cashier.factory.condition.CashCondition;

/**
 * 打折收费，初始化时，必须输入折扣率
 *
 * 如8折，就是0.8
 */
public class CashRebate extends CashSuper {
    private CashCondition condition;

    public CashRebate() {}

    public CashRebate(CashCondition condition) {
        this.condition = condition;
    }

    @Override
    public double acceptCash(double money) {
        return condition.getResult(money);
    }
}
