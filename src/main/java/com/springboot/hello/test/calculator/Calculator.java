package com.springboot.hello.test.calculator;

import com.springboot.hello.exception.BusinessException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Calculator {
    public static void main(String[] args) {
        Calculator calculator = new Calculator();
        calculator.show();
    }

    /**
     * 展示界面
     */
    void show() {
        try {
            System.out.print(" 请输入数字1：");
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            int num1 = Integer.parseInt(br.readLine());
            System.out.print(" 请输入运算符：");
            br = new BufferedReader(new InputStreamReader(System.in));
            String operator = br.readLine();
            System.out.print(" 请输入数字2：");
            br = new BufferedReader(new InputStreamReader(System.in));
            int num2 = Integer.parseInt(br.readLine());
            System.out.println(" num1:" + num1 + ", operator:" + operator + ", num2:" + num2);
            String result = calculate(num1, operator, num2);
            System.out.println(" " + num1 + operator + num2 + " = " + result);
        } catch (NumberFormatException e1) {
            System.err.println(" 请输入正确的数字。");
        } catch (IOException e2) {
            e2.printStackTrace();
        }
    }

    /**
     * 计算
     */
    private String calculate(double num1, String operator, double num2) {
        Operation operation = getOperation(operator);
        operation.num1 = num1;
        operation.num2 = num2;
        return operation.getResult();

    }

    private Operation getOperation(String operator) {
        Operation operation = null;
        switch (operator) {
            case "+":
                operation = new OperationAdd();
                break;
            case "-":
                operation = new OperationSubtract();
                break;
            case "*":
                operation = new OperationMultiply();
                break;
            case "/":
                operation = new OperationDivide();
                break;
            default:
                throw new BusinessException("输入的运算符无效");
        }
        return operation;
    }
}
