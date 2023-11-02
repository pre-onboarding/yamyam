package com.wanted.yamyam.api.location.dto;

import java.io.Serializable;

/**
 * 응답용 시군구 데이터 1개를 저장하기 위한 클래스입니다.
 * @param doSi 도 혹은 시
 * @param sgg 시/군/구
 * @param lat latitude(위도)
 * @param lon longitude(경도)
 * @author 정성국
 */
public record LocationResponse (
        String doSi,
        String sgg,
        double lat,
        double lon
) implements Serializable { }
