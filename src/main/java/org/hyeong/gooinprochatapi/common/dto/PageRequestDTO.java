package org.hyeong.gooinprochatapi.common.dto;


import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PageRequestDTO {
    @Builder.Default
    @Min(value = 1, message = "over 1")
    private int page = 1;

    @Builder.Default
    @Min(value = 10, message = "set over 10")
    @Max(value = 100, message = "cannot over 100")
    private int size = 10;

    //근로자 이름 & 주소 검색 시 필요 Query
    private String searchQuery;
    private String addressQuery;


}