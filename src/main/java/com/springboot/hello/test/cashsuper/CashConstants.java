package com.springboot.hello.test.cashsuper;

public class CashConstants {

    public static class CashSuperType {
        /**
         * 正常收费
         */
        public static final int NORMAL = 1;

        /**
         * 打折收费
         */
        public static final int REBATE = 2;

        /**
         * 满减
         */
        public static final int RETURN = 3;

    }

    public static class CashSuperRebate {
        /**
         * 0.8折
         */
        public static final double EIGHT = 0.8;

    }
}
