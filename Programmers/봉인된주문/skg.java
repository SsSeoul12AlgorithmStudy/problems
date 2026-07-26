import java.util.*;
class Solution {
    public String solution(long n, String[] bans) {
        String answer = "";
        
        long[] ban_nums = new long[bans.length];
        for (int i = 0; i < bans.length; i++) {
            ban_nums[i] = StringToNum(bans[i]);
        }
        Arrays.sort(ban_nums); // String 정렬은 문제에서처럼 안되므로 숫자로 바꿔서 정렬.
        long banned = 0;
        for (long num : ban_nums) {
            if (num <= n + banned) {    // 삭제되면 주문서가 앞으로 밀리니까
                banned++;
            }
        }
        answer = NumToString(n + banned);
        String ss = "z";
        System.out.printf("%d %s", StringToNum(ss), NumToString(StringToNum(ss)));
        
        return answer;
    }
    
    long StringToNum(String s) {
        char[] arr = s.toCharArray();
        long sum = 0;
        for (int i = 0; i < arr.length; i++) {
            sum *= 26l;
            sum += arr[i] - 'a' + 1;
        }
        
        return sum;
    }
    
    String NumToString(long n) {
        StringBuilder sb = new StringBuilder();
        while (n > 0) {
            n--;    // 1-based 변환 시 주의!
            char c = (char)('a' + (n % 26));
            sb.append(c);
            n /= 26;
        }
        
        return sb.reverse().toString(); // 문자열 뒤집는 법
    }
}
