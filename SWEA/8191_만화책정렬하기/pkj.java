import java.io.*;
import java.util.*;
 
public class Solution {
    static boolean[] exist;
    static int answer;
     
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int T = Integer.parseInt(br.readLine());
         
        for(int test_case = 1; test_case <= T; test_case++) {
            StringTokenizer st;
            int N = Integer.parseInt(br.readLine());
            answer = 0;
            exist = new boolean[N+1];
             
            st = new StringTokenizer(br.readLine());
            for(int i = 0; i < N; i++) {
                int x = Integer.parseInt(st.nextToken());
                sortBook(x);
            }
             
            System.out.println("#" + test_case + " " + answer);
        }
    }
     
    static void sortBook(int x) {
        exist[x] = true;
         
        //나의 바로 앞번호가 나보다 왼쪽에 있다면, 앞번호랑 나는 같은 턴에 정리됨
        if(exist[x-1]) { 
            return;
        }
        //나의 바로 앞번호가 나보다 오른쪽에 있다면, 같은 턴에 정리되지 못함
        else answer++;
    }
}
 
/*
while문 하나만 돌려도 시초남...
나의 바로 앞번호가 나보다 왼쪽에 존재한다면 걔랑 나는 같은 턴에 처리 가능
사이에 어떤 수가 있던지는 중요하지 않음 걘 다른 턴에 정리될거니까
 
예를 들어,
4 3 5 1 2 6 의 경우
{4,5,6} {1,2} {3} 이렇게 묶음으로 같은 턴에 정리됨
첫턴에선 1,2를 빼서 앞으로 정렬하고 그다음 턴에선 3을 앞으로 정렬 마지막 턴에선 4,5,6을 정렬하기 때문
*/
