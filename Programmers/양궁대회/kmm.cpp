
#include <string>
#include <vector>

using namespace std;

//어피치가 다 쏜 이후 라이언 n발 사격
//어피치 a발, 라이언 b발
//더 많은 화살을 k점에 맞힌 선수가 k점을 가져감
//a=b일 경우 어피치가 k점을 가져감
//k점을 여러발 맞혀도 k점보다 많은 점수 가져가는거 x, k점만 가져감
//a=b=0인경우 라이언과 어피치 모두 점수를 가져가지 못함
//모든 과녁 점수에 대하여 각 선수의 최종 점수 계산
//최종 점수가 높은 선수 우승자, 다만 동점이면 어피치 우승

//어피치가 이미 화살 n발 다 쏨
//라이언은 어피치를 큰 점수 차이로 이기길 위해 n발의 화살을 어디에 맞춰야 하는지 구할려고 함

//화살의 개수 n
//어피치가 맞힌 과녁 점수의 개수 info에 저장됨
//라이언이 가장 큰 점수차이로 우승하기 위해 n발의 화살을 어떡 과녁에 맞혀야 하는지 정수배열에 담기
//라이언이 이기는 수가 존재하지 않을경우 [-1] return
//라이언이 가장 큰 점수 차이로 우승할 수 있는 방법이 여러 가지 일 경우, 가장 낮은 점수를 더 많이 맞힌 경우를 return
//배열 10~0점까지

int maxScore = 0;
vector<int> answer;

//라운드 진행중 / 남은 화살 / 어피치 점수 / 라이언 점수
void sol(int round, int remains, const vector<int>& apeach, vector<int>& ryan) {
    //모든 과녁 점수를 끝냈을 때
    if(round == 10) {
        ryan[10] = remains;

        //현재 라이언 점수 계산
        int ap = 0, ry = 0;

        for(int i = 0; i < apeach.size(); i++) {
            if (apeach[i] == 0 && ryan[i] == 0) continue;

            if(apeach[i] >= ryan[i]) ap += (10 - i);
            else ry += (10 - i);
        }

        //어피치와 점수 차이 비교
        int diff = ry - ap;

        //max 계산 (서로 같은 경우일 때를 비교하지만, 0은 안됨)
        if(diff > 0 && maxScore <= diff) {

            //같으면 가장 낮은 점수를 더 많이 맞힌 경우 return
            if(maxScore == diff) {
                bool better = false;

                for(int i = 10; i >= 0; --i) {
                    if (ryan[i] == answer[i]) continue;

                    better = ryan[i] > answer[i];
                    break;
                }

                //기존이 더 낫다면
                //answer를 갱신하지 않고 넘어감
                if(better) {
                    answer = ryan;
                }

                //아니라면 계속 진행
            }
            else {
                //기존 최대 점수 차이보다 큰 경우
                answer = ryan;
                maxScore = diff;
            }
        }

        //ryan[10]도 원상복구
        ryan[10] = 0;
        return;
    }

    //어피치에게 지는 경우
    sol(round + 1, remains, apeach, ryan);

    //어피치를 이기는 경우
    if (remains >= apeach[round] + 1) {
        //남은 화살
        int nr = remains - (apeach[round] + 1);

        //라이언 화살 등록
        ryan[round] = apeach[round] + 1;

        sol(round + 1, nr, apeach, ryan);

        //백트래킹
        ryan[round] = 0;
    }
}

vector<int> solution(int n, vector<int> info) {
    //초기화
    answer.clear();
    maxScore = 0;

    vector<int> tmp(11, 0);

    //성공할것인가 아닌가를 기준으로 완탐진행(DFS)
    sol(0,n, info, tmp);

    //정답 객체가 계속 비어있으면 [-1] return
    if(answer.empty()) return {-1};

    //정답 그냥 return
    return answer;
}
