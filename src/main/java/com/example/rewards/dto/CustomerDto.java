package com.example.rewards.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
/**
 * DTO carrying customer id and name.
 */
public class CustomerDto {
    private Long id;
    private String name;
}
