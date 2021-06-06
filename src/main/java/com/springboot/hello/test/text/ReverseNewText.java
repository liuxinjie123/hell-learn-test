package com.springboot.hello.test.text;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * 给一个文本文件 A，请将文件A中的内容按以下规则处理后输出至文件 AReverse：
 *
 * a.如果A中有换行（不在引号内），要把该行的字符按行内次序反转，
 * b.如果A中有多个换行（不在引号内），要把多个行的内容按行间次序反转，
 * c.如果A中有内容（包括换行）在引号内，那么该部分视为整体不需转换，
 * 如果A中有内容不符合上述条件，该部分不需转换直接输出。
 *
 */
public class ReverseNewText {
    public static void main(String[] args) {
        /** 存储需要反转的行 */
        Stack<StrObj> tarStack = new Stack<>();
        /** 存储不需要反转的行 */
        List<StrObj> tarList = new ArrayList<>();
        /** 反转行 和 非反转行 顺序 */
        List<Sequence> seqList = new ArrayList<>();

        String oldFilePath = "src/main/resources/A.txt";
        File oldFile = new File(oldFilePath);
        checkFile(oldFile);

        /** 读取文件信息 */
        if (!readTxt(oldFile, tarStack, tarList, seqList)) {
            System.out.println("读取文件信息失败");
            return;
        }

        String newFilePath = "src/main/resources/AReverse.txt";
        File newFile = new File(newFilePath);
        /** 删除老文件 */
        if (newFile.exists()) {
            newFile.delete();
        }

        /** 写入 AReverse.txt */
        writeTxt(newFilePath, tarStack, tarList, seqList);
    }

    /**
     * 判断文件大小，不能超过1G
     */
    private static void checkFile(File file) {
        if (!file.exists()) {
            throw new RuntimeException("文件不存在");
        }
        if (!file.isFile()) {
            throw new RuntimeException("不是合法的文件");
        }

        double bytes = file.length();
        double kilobytes = (bytes / 1024);
        double megabytes = (kilobytes / 1024);
        double gigabytes = (megabytes / 1024);
        if (gigabytes > 1) {
            throw new RuntimeException("只能处理大小不超过1G的文件");
        }
    }

    /**
     * 传入txt路径读取txt文件
     */
    public static boolean readTxt(File file, Stack<StrObj> tarStack, List<StrObj> tarList, List<Sequence> seqList) {
        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            String text = null;
            /** 总行数 */
            int count = 0;
            /** 存储在 stack 中的个数 */
            int stackCount = 0;
            /** 存储在 list 中的个数 */
            int listCount = 0;
            /** 当前是否在引号内 */
            boolean inQuotation = false;
            while ((text = bufferedReader.readLine()) != null) {
                if (inQuotation) {
                    if (text.startsWith("'") && getStringIndexCount(text, "'") == 1) {
                        StrObj obj = new StrObj();
                        encapuStrObj(obj, text, 2);
                        tarList.add(obj);
                        seqList.add(new Sequence(count, 2, listCount, false));
                        listCount++;
                        inQuotation = true;
                    } else if (getStringIndexCount(text, "'") % 2 == 1) {
                        StrObj obj = new StrObj();
                        encapuStrObj(obj, text, 2);
                        tarList.add(obj);
                        seqList.add(new Sequence(count, 2, listCount, inQuotation));
                        listCount++;
                        inQuotation = false;
                    } else {
                        StrObj obj = new StrObj();
                        obj.content = text;
                        tarList.add(obj);
                        seqList.add(new Sequence(count, 2, listCount, inQuotation));
                        listCount++;
                        inQuotation = true;
                    }
                } else {
                    if (text.startsWith("'") && getStringIndexCount(text, "'") == 1) {
                        StrObj obj = new StrObj();
                        obj.content = text;
                        tarList.add(obj);
                        seqList.add(new Sequence(count, 2, listCount, false));
                        listCount++;
                        inQuotation = true;
                    } else {
                        StrObj obj = new StrObj();
                        encapuStrObj(obj, text, 1);
                        tarStack.add(obj);
                        seqList.add(new Sequence(count, 1, stackCount, false));
                        stackCount++;
                        inQuotation = false;
                    }
                    count++;
                }
            }
            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    /**
     * 将 text 信息封装到 obj 中
     */
    private static void encapuStrObj(StrObj obj, String text, int type) {
        String[] arr = text.split("'");
        Stack<String> stack = new Stack<>();
        for (int i=0; i<arr.length; i++) {
            if (type == 1) {
                if (i % 2 == 1) {
                    stack.add("'" + arr[i] + "'");
                } else {
                    String content = reverseStr(arr[i]);
                    stack.add(content);
                }
            } else {
                if (i % 2 == 0) {
                    if (i != 0) {
                        stack.add("'" + arr[i] + "'");
                    } else {
                        stack.add(arr[i] + "'");
                    }
                } else {
                    String content = reverseStr(arr[i]);
                    stack.add(content);
                }
            }
        }
        obj.stack = stack;
    }

    private static String reverseStr(String str) {
        if (null == str || "".equals(str)) {
            return "";
        }
        Stack<Character> stack = new Stack<>();
        for (char ch : str.toCharArray()) {
            stack.add(ch);
        }
        StringBuilder strBuilder = new StringBuilder();
        while (!stack.isEmpty()) {
            strBuilder.append(String.valueOf(stack.pop()));
        }
        return strBuilder.toString();
    }

    /**
     * 写入文件
     */
    private static void writeTxt(String newFilePath, Stack<StrObj> tarStack, List<StrObj> tarList, List<Sequence> seqList) {
        int listTCount=0;
        for (int i=0; i<seqList.size(); i++) {
            Sequence seq = seqList.get(i);
            String content = "";
            /**
             * type == 1, 栈类型
             */
            if (seq.type == 1) {
                StrObj obj = tarStack.pop();
                if (null != obj.content && !"".equals(obj.content)) {
                    content = obj.content;
                } else {
                    while (!obj.stack.isEmpty()) {
                        content += obj.stack.pop();
                    }
                }
                write(newFilePath, content);
                if (i != seqList.size()-1) {
                    write(newFilePath, "\n");
                }
            } else if (seq.type == 2) {
                /**
                 * type == 2, list 类型
                 */
                StrObj obj = tarList.get(listTCount);
                listTCount++;
                if (null != obj.content && !"".equals(obj.content)) {
                    content = obj.content;
                } else {
                    while (!obj.stack.isEmpty()) {
                        content += obj.stack.pop();
                    }
                }
                write(newFilePath, content);
                if (i != seqList.size() - 1) {
                    write(newFilePath, "\n");
                }
            }
        }
    }

    /**
     * 查询 key 在 str 中出现的次数
     */
    private static int getStringIndexCount(String str, String key) {
        if(str == null || key == null || "".equals(str.trim()) || "".equals(key.trim())){
            return 0;
        }
        int count = 0;
        int index = 0;
        while ((index=str.indexOf(key, index)) != -1) {
            index = index + key.length();
            count++;
        }
        return count;
    }

    private static void write(String filePath, String conent) {
        BufferedWriter out = null;
        try {
            /** true,进行追加到文件末尾 **/
            out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filePath, true)));
            out.write(conent);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static class StrObj {
        /**
         * 内容，不需要反转时 此字段有值
         */
        String content;

        /**
         * 栈，存储需要反转的内容
         */
        Stack<String> stack;

    }

    private static class Detail {
        /**
         * 1 - stack
         * 2 - list
         */
        int type;

        /**
         * 栈中元素个数
         */
        int length;
    }

    private static class Sequence {
        /**
         * 顺序，
         * 1，2，3
         */
        public int sequence;

        /**
         * 类型
         * 1-stack
         * 2-list
         */
        public int type;

        /**
         * 是栈 或 队列中的 第几个元素
         */
        public int num;

        /**
         * 是否在引号内
         */
        public boolean inQuotation;

        public Sequence() {}

        public Sequence(int sequence, int type, int num, boolean inQuotation) {
            this.sequence = sequence;
            this.type = type;
            this.num = num;
            this.inQuotation = inQuotation;
        }
    }
}
