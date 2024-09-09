package com.example.demo.dto;

import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

@JsonPOJOBuilder
public record StudentResponse(Integer rollNo,String name,Integer score) {

}
