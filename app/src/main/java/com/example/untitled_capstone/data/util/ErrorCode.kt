package com.example.untitled_capstone.data.util

object ErrorCode {
    const val FAIL_OOOOO = "FAIL_OOOOO" //실패하였습니다
    const val COMMON500 = "COMMON500" //서버 에러, 관리자에게 문의 바랍니다.
    const val COMMON400 = "COMMON400" //잘못된 요청입니다.
    const val COMMON401 = "COMMON401" //인증이 필요합니다.
    const val COMMON403 = "COMMON403" //금지된 요청입니다.
    const val JWT4001 = "JWT4001" //권한이 없습니다.
    const val JWT4002 = "JWT4002" //유효하지 않은 토큰입니다.
    const val JWT4003 = "JWT4003" //JWT 토큰을 넣어주세요.
    const val JWT4004 = "JWT4004" //만료된 토큰입니다.
    const val JWT4005 = "JWT4005" //RefreshToken이 일치하지 않습니다.
    const val TOKEN_400_1 = "TOKEN_400_1" //헤더에 토큰이 존재하지 않음
    const val TOKEN_400_2 = "TOKEN_400_2" //토큰을 추출할 수 없음
    const val TOKEN_401_1 = "TOKEN_401_1" //토큰이 만료됨
    const val TOKEN_401_2 = "TOKEN_401_2" //토큰이 유효하지 않음
    const val TOKEN_401_3 = "TOKEN_401_3" //지원하지 않는 토큰 타입임
    const val TOKEN_400_3 = "TOKEN_400_3" //id_token 이 만료되었거나 유효하지 않음.
    const val USER4004 = "USER4004" //사용자를 찾을 수 없습니다.
    const val MEMBER4002 = "MEMBER4002" //닉네임 중복입니다.
    const val PAGE400 = "PAGE400" //잘못된 페이지 값입니다. 1 이상의 정수로 입력해주세요.
    const val INGREDIENT404 = "INGREDIENT404" //식재료를 찾을 수 없습니다.
    const val COMMON200 = "COMMON200" //성공하였습니다.
}