import java.io.*;
import java.util.*;

class Solution {
    static String alpha = " abcdefghijklmnopqrstuvwxyz";
    
    public String solution(long n, String[] bans) {
        String answer = "";
        
        ArrayList<Long> banN = new ArrayList<>();
        for(int i = 0; i < bans.length; i++){
            Long tmp = banToNum(bans[i]);
            banN.add(tmp);
        }
        Collections.sort(banN);
        
        //금지된 주문을 포함해서 몇 번째 주문을 만들어야하는지 확인
        for(int i = 0; i < banN.size(); i++){
            if(banN.get(i) <= n){
                n++;
            }
            else break;
        }

        answer = makeSpell(n);
        return answer;
    }
    
    //번호에 맞는 주문 만드는 메소드
    static String makeSpell(long n){
        int len = 1; //주문의 길이
        long cnt = 26; //몇자리인지 판별

        while(n > cnt){
            n -= cnt;
            cnt *= 26;
            len++;
        }
        n--;

        long[] pow=new long[len];
        pow[0]=1;

        for(int i=1;i<len;i++) pow[i]=pow[i-1]*26;


        StringBuilder sb=new StringBuilder();

        for(int i = len-1; i>=0; i--){
            int x = (int)(n / pow[i]);
            sb.append(alpha.charAt(x+1));
            n %= pow[i];
        }

        return sb.toString();
    }

    //금지된 주문을 번호로 변환하는 메소드
    static Long banToNum(String ban){
        int n = ban.length();
        long result = 0;

        for(int i = 0; i < n; i++){
            char c = ban.charAt(n-1-i);
            int cI = alpha.indexOf(c);
            result += (long)(cI * Math.pow(26, i));
        }

        return result;
    }
}
