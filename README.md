## 프로젝트 소개
<p align="center"><img src="/image/mainLogo2.png"></p>

- SUM(Smart+Untact+Meeting)은 군 생활을 하는 장병 및 군무원들을 대상으로 각 분야의 인재(전문가)와 비대면 소통할 수 있는 커뮤니티 앱 플랫폼입니다. 사용자가 선택한 분야와 개인관계지수를 고려하여, 개인에게 필요한 맞춤 전문가를 찾아주어 업무를 수행함에 있어 적시에 실질적인 도움을 받을 수 있는 커뮤니티 플랫폼을 목표로 하고있습니다. 

<a href="https://youtu.be/_ZvgSkNEopI"><p align="center"><img src="/video.PNG"></p></a>

## 기능 설계

### 어플리케이션 화면
<table>
  <tbody>
    <tr>
      <td><img src="/image/1.jpg"></td>
      <td><img src="/image/2.jpg"></td>
      <td><img src="/image/3.jpg"></td>
      <td><img src="/image/4.jpg"></td>
      <td><img src="/image/5.jpg"></td>
    </tr>
    <tr>
      <td><p align="center">도움이 필요한 <br> 분야 선택</p></td>
      <td><p align="center">분야별 전문가 <br> Reranking</p></td>
      <td><p align="center">개인관계지수를 <br> 고려한 추천</p></td>
      <td><p align="center">전문분야, 개인관계 <br> 맞춤 분석 추천</p></td>
      <td><p align="center">비대면 소통으로 <br> 신속한 도움</p></td>
    </tr>
  </tbody>
</table>

### 다양한 추천 방식 및 카테고리 적용
* 다양한 추천 방식
  * 개인관계지수(사용자간의 관계만을 고려)
  * 전문가지수(해당분야의 전문성만 고려)
  * 복합지수(개인관계지수와 전문가 지수를 모두고려)
* 카테고리별 최적의 전문가 검색 및 재정렬(Re Ranking!!)
  * 초기버전 적용분야 : 어학, 전투능력, 전산, 행정, 법
  * 추후 다양한 분야 추가 예정
### 추천받은 전문가의 프로필을 통한 관계도, 전문성을 고려한 실시간 소통

## 추천 알고리즘
 - Topic Sensitive_Social Relation Rank Algorithm
 
   * 사용자들이 갖는 내재적 속성과 토픽 간에 상호 연관성을 고려하여 소셜관계 지수 계산 및 전문가 추천
   <p align="center"><img src="/image/mat.PNG"></p>
   
   * 내재적 속성이 영향을 받으면 '1', 영향을 받지 않으면 '0' 부여
   <p align="center"><img src="/image/sick.PNG"></p>
   
   * 토픽에 영향을 받을 때 사용자 간의 소셜관계 랭크 및 전문가 추천 식 적용
   <p align="center"><img src="/algor.png"></p>

## 기대 효과

### AI기반 원천기술 확보
  * 국방인사정보체계(국방부), 인터넷 공개자료(SNS, 블로그 등) 수집 및 연동하여 빅데이터 분석가능

### 인재획득
  * 추후 확보된 원천 기술을 통해서 적격자 추천 및 선발 결과를 자동 분석가능

### 전역지원
  * 취업역량 강화, 전역장병 만족도 제고

### 인재 양성/운영
  * 맞춤식 자기개발 추천 및 인사관리를 통해 필요한 인재육성 및 적재적소 운영가능
  
### <AI기반 스마트 인재관리시스템 발판 마련>


## 어플리케이션 구동 환경
* 최소 사양 : Android 4.1 (Jelly Bean)
* APK 설치파일  : <a href="/Installation_SUM.apk" download>SUM 설치파일</a>

## 기술 스택
<p align="center"><img src="/image/STACK.PNG"></p>

### Back-End
* Firebase Authentication
* Cloud Function for Firebase
* Firebase Realtime Database
* Firebase Cloud Messaging
* NodeJs

### Front-End
* Android(JAVA)
* Glide
* Retrofit
* PhotoView

### MNG
* GitHub

## 팀 정보 (Team Information)
- 팀장 김영인 duddls4471 duddls4471@naver.com
- 팀원 배성진 SharkBSJ sjbea1996@naver.com
- 팀원 박정선 sunimooni jspark7373@naver.com
- 팀원 구예빈 beenduri gyb0534@naver.com

## 저작권 및 사용권 정보 (Copyleft / End User License)
* 라이센스 : <a href="/LICENSE">MIT</a>
