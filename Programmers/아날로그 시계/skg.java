/*

초침 - 시침
초침 - 분침

시간을 초 단위로 표현하면
시 * 60 * 60 + 분 * 60 + 초
h * 3600 + m * 60 + s
(0 <= h <= 11) (0 <= m <= 59) (0 <= s <= 59)
s는 60이 되면 0이 되어 m에 +1이 되고
m도 60이 되면 0이 되어 h에 +1이 된다.
s == h or s == m

흠.. 각도로 접근해야 하나?
0도 ~ 360도
시간 : 12시간 = 12*3600초 동안 360도가 돈다.
=> 1초에 360/12*3600도 1/120도 
분 : 60분 = 60 * 60초 동안 360도가 돈다.
=> 1총 360/60*60도 = 0.1도
초 : 60초동안 360도가 돈다.
=> 1초에 360/60도 = 6도

정확도를 위해 정수형을 사용 -> 360도를 360*120도로 설정
시침은 1초에 1단위 이동
분침은 1초에 12단위 이동
초침은 1초에 720단위 이동

시침은 1/720초에 1/720 이동
분침은 1/720초에 12/720 이동
초침은 1/720초에 1 이동
=> 1/720 
360 * 120단위 이동하면 0단위로 초기화

<브루트포스>
1초씩 증가시키면서 초침이 분침/시침을 지나치는 시간을 체크한다.
시간복잡도 : 0 ~ 24 * 3600 = 86400

문제: 1초 단위로 진행하기 때문에 0.몇초 단위에서 만나는 경우는 체크할 수 없다. -> 지나쳐버림.. 

<직선 그래프 접근>
시간을 x로, 각도를 y로 두고 그래프를 그려보면
초침 각도 직선 그래프는 60초마다 반복되는 직선 그래프고,
분침 `` `` 60 * 60초마다 ``
시침 `` `` 12 * 60 * 60초마다 ``
60초 단위로 계속 확인
시작 시 분침이나 시침이 0보다 크면 다음 60초 이전에 한번씩 만난다.
and 종료 시 분침이나 시침이 종료 시 초침보다 작으면 종료 전에 만남.

결국, 60초 단위로 끊어서 생각해야 한다.

쥐쥐 못하겄다~

직선 그래프 접근 다시 시작해보자
측정할 구간의 시작 상태와 마지막 상태를 구해서 비교하면 교점의 개수를 얻을 수 있다.
시작 상태 = Math.max(curTime, 0);
마지막 상태 = Math.min(curTime, 60);

++해서 curTime % 60 == 0이 되면 구간 검사
마지막에 curTime % 60 == 0이 아니면 구간 검사 한 번 더.(끝 구간이 딱 안 맞아떨어지는 경우)

1. 시침이랑 중간에서 만나나요?
2. 분침이랑 중간에서 만나나요?
ㄴ 둘 다 처리되면 끝
3. 끝에서 만나는 건 둘이 만나는 거나 셋이 만나는 거 뿐..
(양쪽에서 만나는 경우는 없다)
4. 특이 케이스 : 동시에 만나는 경우 -> 12시 뿐

0. 시작 시간에 겹치는지 검사
1. !시작 ~ 끝 시간에 겹치는지 검사
1-1. 끝 시간에 세 개 모두 겹치는지 검사 -> 그럼 한 번만 알람
2. 반복

*/

class Solution {
    final int MAX_DEGREE = 360 * 120;
    final int HOUR_DEGREE = 1;
    final int MINUTE_DEGREE = 12;
    final int SECOND_DEGREE = 720;
    
    public int solution(int h1, int m1, int s1, int h2, int m2, int s2) {
        int answer = 0;
        
        int hDegree, mDegree, sDegree;
        int startSecond = h1 * 60 * 60 + m1 * 60 + s1;
        int endSecond = h2 * 60 * 60 + m2 * 60 + s2;
            
        hDegree = getDegree('h', startSecond);
        mDegree = getDegree('m', startSecond);
        sDegree = getDegree('s', startSecond);
        
        System.out.println("startSecond: " + startSecond);
        System.out.println("h: " + (hDegree/120.0) + ", m: " + (mDegree/120.0) + ", s: " + (sDegree/120.0));
        
        // 시작 시간에 초침과 시침/분침 사이 상관관계 계산
        int prevHDegree = hDegree;
        int prevMDegree = mDegree; 
        int prevSDegree = sDegree; 
        
        int curSecond = startSecond;
        // 시작 시간에 초침과 시침/분침이 겹치는지 확인
        if (hDegree == sDegree || mDegree == sDegree) {
            answer++;
        }
        
        while (++curSecond <= endSecond) {
            if (curSecond % 60 != 0)  continue;

            hDegree = getDegree('h', curSecond);
            mDegree = getDegree('m', curSecond);
            sDegree = getDegree('s', curSecond);
        

            int postHDegree = hDegree == 0 ? MAX_DEGREE : hDegree;
            int postMDegree = mDegree == 0 ? MAX_DEGREE : mDegree;
            int postSDegree = sDegree == 0 ? MAX_DEGREE : sDegree;
            
            int sum = 0;
            // 시침 겹치는지 검사
            if (prevHDegree > prevSDegree && postHDegree <= postSDegree) {
                sum++;
            }
            
            // 분침 겹치는지 검사
            if (prevMDegree >  prevSDegree && postMDegree <= postSDegree) {
                sum++;
            }
            
            if (postHDegree == postMDegree && postMDegree == postSDegree)  sum = 1;
            
            prevHDegree = hDegree; 
            prevMDegree = mDegree; 
            prevSDegree = sDegree;             
            
            answer += sum;
        }
        
        // 만약 60초로 딱 떨어지지 않는 시간에 종료되면 마지막으로 검사
        if (endSecond % 60 != 0) {            
            hDegree = getDegree('h', endSecond);
            mDegree = getDegree('m', endSecond);
            sDegree = getDegree('s', endSecond);
            
            int postHDegree = hDegree == 0 ? MAX_DEGREE : hDegree;
            int postMDegree = mDegree == 0 ? MAX_DEGREE : mDegree;
            int postSDegree = sDegree == 0 ? MAX_DEGREE : sDegree;
            
            int sum = 0;
            
            // 시침 겹치는지 검사
            if (prevHDegree >  prevSDegree && postHDegree <= postSDegree) {
                sum++;
            }
            
            // 분침 겹치는지 검사
            if (prevMDegree >  prevSDegree && postMDegree <= postSDegree) {
                sum++;
            }
            
            if (postHDegree == postMDegree && postMDegree == postSDegree)  sum = 1;          
            answer += sum;
        }
                
        return answer;
    }
    
    // 현재 초를 받아서 몇 단위인지 반환하는 함수
    public int getDegree(char method, int second) {
        switch (method) {
            case 'h':
                return (second * HOUR_DEGREE) % MAX_DEGREE;
            case 'm':
                return (second * MINUTE_DEGREE) % MAX_DEGREE;
            case 's':
                return (second * SECOND_DEGREE) % MAX_DEGREE;
        }
        return 0;
    } 
    
    // 초를 시간으로 변경
    public String secToTime(int cur) {
        int hour = cur / (60 * 60);
        cur %= (60* 60);
        int minute = cur / 60;
        cur %= 60;
        int second = cur;
        
        return new String(hour + ":" + minute + ":" + second);
    }
    
    
}
