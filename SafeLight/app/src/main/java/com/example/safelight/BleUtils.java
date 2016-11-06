package com.example.safelight;

import java.util.BitSet;

/**
 * Created by changsu on 2015-03-28.
 * 각종 연산을 위한 class 임
 *  - 거리 계산
 *
 */
public class BleUtils {

    public static String HEX_CHARACTERS="0123456789ABCDEF";



    public BleUtils()
    {

    }

    // byte[] to char[] method
    public char[] ByteArrtoCharArr(byte[] a){
        StringBuilder buffer = new StringBuilder();
        for(int i = 0; i < a.length;i++){
            buffer.append(a[i]);
        }
        char[] b = buffer.toString().toCharArray();
        return b;
    }



    // String[] to char[]
    public char[] StringArrtoCharArr(String[] s){

        int length = 0;
        for (int i = 0; i < s.length; i++) {
            for (int j = 0; j < s[i].length(); j++) {
                length++;
            }
        }

        char [] c = new char[length];
        int k = 0;
        for (int i = 0; i < s.length; i++) {
            for (int j = 0; j < s[i].length(); j++) {
                c[k] = s[i].charAt(j);
                k++;
            }
        }
        return c;
    }


    // byte[] to string
    public String ByteArrayToString(byte[] scanRecord)
    {
        /*
            byte -> String (2byte로 계산됨)
            따라서 scanRecord.length * 2의 크기로 StringBuilder를 생성함
         */
        StringBuilder hex = new StringBuilder(scanRecord.length * 2);

        for(byte b : scanRecord)
        {
            hex.append(String.format("%02x", b));
        }

        return hex.toString();
    }


    // hexadecimal to decimal
    public static int hex2dec(String hexValue) {
        hexValue = hexValue.toUpperCase();
        int decimalResult = 0;

        for (int i = 0; i < hexValue.length(); i++) {
            char digit = hexValue.charAt(i);
            int digitValue = HEX_CHARACTERS.indexOf(digit);
            decimalResult = decimalResult * 16 + digitValue;
        }
        return decimalResult;
    }

    // int to int array to string
    public static String dec2str(int decValue){
        int q = 0, r = decValue % 10;
        if(decValue>=10){
            q = decValue / 10;
        }
        int[] res = new int[2];
        res[0] = q;
        res[1]= r;
        StringBuilder builder = new StringBuilder();
        for (int i : res) {
            builder.append(i);
        }
        String strValue = builder.toString();
        return strValue;
    }

    // 상태정보 1byte를 bitset으로 변환
    public static int[] byte2bitset(byte b) {
        byte[] temp = new byte[]{b};
        BitSet bitset = BitSet.valueOf(temp);
        int[] result = new int[bitset.length()];
        for (int i=0; i<bitset.length(); ++i) {
            result[i] = Boolean.compare(bitset.get(i),false);
        }

        return result;
    }

    // 상태정보 파싱하는 함수. input: int[], output: string
    public static String parseState(int[] intArray){
        for (int k = 0; k < intArray.length/2; k++) {
            int temp = intArray[k];
            intArray[k] = intArray[intArray.length-(1+k)];
            intArray[intArray.length-(1+k)] = temp;
        }
        StringBuilder temp = new StringBuilder(intArray.length);

        for(int i=0; i< intArray.length; i++){
            if(i==7){
                break;
            }
            if(i==1){                           // 이상점등
                if(intArray[i]==1){
                    temp.append(String.format("%d", 1));
                }
            }
            else if(i==2){                      // 이상소등
                if(intArray[i]==1){
                    temp.append(String.format("%d", 2));
                }
                else{
                    if(intArray[i-1]==0){
                        temp.append(String.format("%d", 0));
                    }
                }
            }
            else if(i==4){                      // 램프
                if(intArray[i]==1){
                    temp.append(String.format("%d", 1));
                }
            }
            else if(i==5){                      // 안정기
                if(intArray[i]==1){
                    if(intArray[i-1]==1){
                        temp.append(String.format("%d", 3));
                    }
                    else{
                        temp.append(String.format("%d", 2));
                    }
                }
                else{
                    if(intArray[i-1]==0){
                        temp.append(String.format("%d", 0));
                    }
                }
            }
            else{
                temp.append(String.format("%d", intArray[i]));
            }
        }
        String result = temp.toString();
        return result;
    }
}
