#include <string>
#include <vector>
#include <cmath>
#include <iostream>

using namespace std;

int solution(int h1, int m1, int s1, int h2, int m2, int s2) {
    //시침, 분침, 초침
    //초침이 시침, 분침과 겹칠 때마다 알람이 울리는 기능 존재
    //특정 시간동안 알람이 울린 횟수
    //h1, m1, s1 부터 h2,m2,s2까지 알람이 울리는 횟수를 센다
    //시침, 분침은 double 형식이지만, 서로 같은지만 검증하고 자연수로 처리해도 됨
    //초침은 자연수여도 괜찮을 것 같음. 다만 탐색 범위가 a <= 초침 < b 형식으로 계산해야 할 것 같음
    
    //그럼 그냥 시침 분침 초침 다 초로 바꿔서 계산하면 안되나?
    //그럼 시침, 분침, 초침 위치는 어떻게 계산하지
    //12시 6분 5초 = 43565
    //시침, 분침에 범위를 잡아두고 일치한다면 값 올리기?
    //시침 = (43565 * 5 / 3600) % 60
    //분침 = (43565 / 60) % 60
    //초침 = 43565 % 60
    
     int answer = 0;

    int start = h1 * 3600 + m1 * 60 + s1;
    int end = h2 * 3600 + m2 * 60 + s2;

    // 시작 시각에 이미 겹쳐 있는 경우
    if (start % 3600 == 0) {
        answer++;
    }

    for (int t = start; t < end; t++) {

        // 현재 시침, 분침, 초침 위치
        double h = fmod(t * 5.0 / 3600.0, 60.0);
        double m = fmod(t / 60.0, 60.0);
        double s = fmod(t, 60.0);

        // 1초 후 시침, 분침, 초침 위치
        double nextH = fmod((t + 1) * 5.0 / 3600.0, 60.0);
        double nextM = fmod((t + 1) / 60.0, 60.0);
        double nextS = fmod(t + 1, 60.0);

        // 초침이 한 바퀴 돌아 0으로 가는 경우
        if (nextS == 0) {
            nextS = 60.0;
        }

        // 시침을 추월했는지
        bool crossH = (s < h && nextS >= nextH);

        // 분침을 추월했는지
        bool crossM = (s < m && nextS >= nextM);

        // 각각 알람 추가
        if (crossH) answer++;
        if (crossM) answer++;

        // 12시 정각에 두 알람이 중복되면 하나 제거
        if (crossH && crossM && (t + 1) % (12 * 60 * 60) == 0) {
            answer--;
        }
    }

    return answer;
}
