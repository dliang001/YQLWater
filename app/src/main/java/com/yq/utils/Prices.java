package com.yq.utils;

import android.text.TextUtils;

import com.yq.model.Cbj;

import java.text.DecimalFormat;

/**
 * Created by gbh on 16/12/3.
 */

public class Prices {


    /**
     * 拿到"用户性质"对应的 -- 价格
     * @param ysxz
     * @return
     */
    public static double getPrice(String ysxz) {
        if (null == ysxz) return 0;
        if (ysxz.startsWith("生活")) {
            return 1.7;
        } else if (ysxz.startsWith("服务")) {
            return 2.0;
        } else if (ysxz.startsWith("工业")) {
            return 2.0;
        } else if (ysxz.startsWith("石桥")) {
            return 1.8;
        } else if (ysxz.startsWith("行政")) {
            return 1.8;
        } else if (ysxz.startsWith("源水")) {
            return 0.8;
        } else if (ysxz.startsWith("奶牛")) {
            return 1.1;
        } else if (ysxz.startsWith("罐头")) {
            return 1.3;
        } else if (ysxz.startsWith("线厂")) {
            return 1.4;
        } else if (ysxz.startsWith("宏裕")) {
            return 1.5;
        } else if (ysxz.startsWith("职工")) {
            return 1.7;
        } else if (ysxz.startsWith("大埫")) {
            return 2.8;
        } else if (ysxz.startsWith("特惠")) {
            return 0.0;
        }
        return 0.0;

    }


    /**
     * 实际收水费
     *
     * @param sl
     * @return
     */
    public static Double getMoney(int sl, Cbj cbj) {
        int temp = 0;
        double res = 0;
        int index = 0;
        int fen = 0;

        // 底吨数 -- 如果为"空"  设置为 0
        temp = Integer.parseInt(TextUtils.isEmpty(cbj.getDds()) ? "0" : cbj.getDds());

        if (sl < temp) {                                    // 底吨数 > 实抄数量
            temp = 2 * Integer.parseInt(cbj.getDds());      // 底吨数 * 2
        } else
            temp = sl;

        String[] array = cbj.getYsxz().split("%");          // 用水性质 --  大埫100% ？

        if (array.length == 1)                              // 如果没有 "%"
            res = temp * getPrice(cbj.getYsxz());

        else {                                              // 如果有 "%"  -- // 拿到数字部分
            int[] fens = new int[array.length];
            String strNumber = "1234567890";

            for (int j = 0; j < array.length; j++) {
                String s = array[j];
                char[] ch = s.toCharArray();
                for (int i = 0; i < ch.length; i++) {
                    Character c = ch[i];
                    if (strNumber.contains(c.toString())) { // 拿到数字部分
                        index = i;
                        break;
                    }
                }

                fen = Integer.parseInt(s.substring(index, s.length()));
                fens[j] = fen;
                System.out.println("=========" + fens[j]);
            }

            double num = 0;
            for (int i = 0; i < fens.length; i++) {
                num = num + (fens[i] / 100 * getPrice(array[i]));
            }
            res = num;
        }
        return res;

    }

    private static double getIndex(String ysxz, double fen, int index) {
        if (null == ysxz) return 0.0;
        String strNumber = "1234567890";
        if (!ysxz.endsWith("100%")) {
            char[] ch = ysxz.toCharArray();
            for (int i = 0; i < ch.length; i++) {
                Character c = ch[i];
                if (strNumber.contains(c.toString())) {
                    index = i;
                    break;
                }
            }
            fen = Double.parseDouble(ysxz.substring(index, ysxz.indexOf("%")));
        }
        return fen;
    }


    public static String m2(Double d) {
        if (d == 0)
            return "0.0";

        DecimalFormat df = new DecimalFormat("#0.00");
        return df.format(d);
    }

//    public static String m2Balance(Double d) {
//        if (d == 0)
//            return "0.0";
//
//        DecimalFormat df = new DecimalFormat("#.00");
//        return d<0 ? "-" + df.format(d) : df.format(d);
//    }

}
