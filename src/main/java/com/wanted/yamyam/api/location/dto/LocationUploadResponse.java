package com.wanted.yamyam.api.location.dto;

/**
 * 시군구 데이터 업로드 후 총 저장 갯수를 반환하기 위한 DTO 입니다.
 * @param countTotal
 * @author 정성국
 */
public record LocationUploadResponse(
        int countTotal
) {
}
