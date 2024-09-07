package com.example.demo.dto;

import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

import jakarta.validation.constraints.NotNull;

@JsonPOJOBuilder
public record StudentDTO(String name,Integer score) {

}
