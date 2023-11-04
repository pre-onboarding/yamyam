package com.wanted.yamyam.api.location.dto;

import java.io.Serializable;

/**
 * 클라이언트에 반환할 형식에 맞추어 전체 시군구 데이터를 저장하기 위한 클래스입니다.
 * @param locations 전체 시군구 데이터
 */
public record LocationsListResponse(
        LocationResponse[] locations
) implements Serializable { }
